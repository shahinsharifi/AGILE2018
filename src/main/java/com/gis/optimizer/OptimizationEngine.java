package com.gis.optimizer;

import com.gis.config.DmatrixConfiguration;
import com.gis.database.model.Facility;
import com.gis.database.model.Municipality;
import com.gis.database.model.Pmedian;
import com.gis.database.service.facility.FacilityServiceImpl;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
import com.gis.database.service.pmedian.PmedianService;
import com.gis.database.service.pmedian.PmedianServiceImpl;
import com.gis.optimizer.evaluator.SolutionEvaluator;
import com.gis.optimizer.factory.OperationFactory;
import com.gis.optimizer.factory.PopulationFactory;
import com.gis.optimizer.logger.EvolutionLogger;
import com.gis.optimizer.model.BasicGenome;
import com.gis.optimizer.util.FileUtils;
import com.google.common.collect.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uncommons.maths.number.AdjustableNumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.maths.random.XORShiftRNG;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.selection.TournamentSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;


@Service
public class OptimizationEngine {


    private Random rng;
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationEngine.class);

    @Autowired
    private MunicipalityServiceImpl municipalityService;

    @Autowired
    private PmedianServiceImpl pmedianService;

    @Autowired
    private FacilityServiceImpl facilityService;


    public boolean evolve(int seedSize) throws Exception {

        rng = new XORShiftRNG();

        List<Municipality> municipalities = municipalityService.getAll();
        LOGGER.info("Municipalities are loaded...");

        List<Municipality> initialSeed = getRandomInitialSeed(rng, municipalities, seedSize);
        LOGGER.info("Initial seed with size '" + seedSize + "' has been created...");

        Table distanceMatrix = DmatrixConfiguration.getDistanceMatrix();

        //Generating random population
        CandidateFactory<List<BasicGenome>> candidateFactory = new PopulationFactory<>(
                municipalities,
                initialSeed
        );


        //Configuration of selection strategy
        SelectionStrategy<Object> selection = new TournamentSelection(
                new AdjustableNumberGenerator<>(new Probability(0.5d))
        );


        //Configuration of operation pipeline
        OperationFactory Operations = new OperationFactory();
        EvolutionaryOperator<List<BasicGenome>> pipeline = Operations.createEvolutionPipeline(rng, municipalities, distanceMatrix);


        //Configuration of solution evaluator
        SolutionEvaluator evaluator = new SolutionEvaluator(municipalities, distanceMatrix);


        //Instantiation of evolutionary engine
        GenerationalEvolutionEngine<List<BasicGenome>> engine = new GenerationalEvolutionEngine<>(
                candidateFactory,
                pipeline,
                evaluator,
                selection,
                rng);


        //Configuration of evolutionary engine
        engine.addEvolutionObserver(new EvolutionLogger<>(1));
        engine.setSingleThreaded(false);

        long start = System.currentTimeMillis();
        //Running evolutionary algorithm
        List<BasicGenome> locations = engine.evolve(
                100,
                50,
             //  new GenerationCount(500)
                new Stagnation(2000, false)
        );
        long end = System.currentTimeMillis();


        //Cleaning up database
        LOGGER.info("Cleaning up database...");
        pmedianService.deleteAll();
        facilityService.deleteAll();


        //Inserting result into the database
        LOGGER.info("Adding location result to the database...");
        for (BasicGenome genome : locations) {
            Facility facility = new Facility();
            facility.setFacilityId(genome.getFacilityID());
            facilityService.insert(facility);
        }

        List<BasicGenome> allocations = getNearestFacility(distanceMatrix, municipalities, locations);


        LOGGER.info("Adding allocation result to the database...");
        for (BasicGenome genome : allocations) {
            Pmedian pmedian = new Pmedian();
            pmedian.setDemandId(genome.getDemandID());
            pmedian.setFacilityId(genome.getFacilityID());
            pmedianService.insert(pmedian);
        }

        calculateFacilityCapacity(allocations, seedSize);
        LOGGER.info("End of processing ...");
        LOGGER.info("Exe. time: " + (end - start)/1000);
        FileUtils.printAlgorithmProgress(EvolutionLogger.getProgress() , "progress1" + seedSize);

        return locations.size() > 0;
    }


    private List<Municipality> getRandomInitialSeed(Random rng, List<Municipality> municipalities, int seedSize) throws Exception {

        List<Municipality> initialSeed = new ArrayList<>();
        int start = 0;
        int end = municipalities.size() - 1;
        int cur = 0;
        int remaining = end - start;
        for (int i = start; i < end && seedSize > 0; i++) {
            double probability = rng.nextDouble();
            if (probability < ((double) seedSize) / (double) remaining) {
                seedSize--;
                initialSeed.add(cur++, municipalities.get(i));
            }
            remaining--;
        }

        return initialSeed;
    }


    private void calculateFacilityCapacity(List<BasicGenome> genomes, int seedSize) {

        Map<String, Map<String, Integer>> result = new HashMap<>();
        for (BasicGenome genome : genomes) {
            long timeIndex = genome.getWeight();
            if(timeIndex>0) {
                if (result.containsKey(genome.getFacilityID())) {
                    Map<String, Integer> tmp = result.get(genome.getFacilityID());
                    if (tmp.containsKey("t" + timeIndex)) {
                        tmp.put("t" + timeIndex, tmp.get("t" + timeIndex) + 1);
                        result.put(genome.getFacilityID(), tmp);
                    } else
                        tmp.put("t" + timeIndex, 1);
                } else {
                    Map<String, Integer> tmp = new HashMap<>();
                    tmp.put("t" + timeIndex, 1);
                    result.put(genome.getFacilityID(), tmp);
                }
            }
        }

        FileUtils.printAlgorithmAggregation(result, "aggregation1" + seedSize);
    }


    private List<BasicGenome> getNearestFacility(Table distanceMatrix, List<Municipality> municipalities, List<BasicGenome> chromosome) {

        List<BasicGenome> result = new ArrayList<>();
        long totalCost = 0l;
        for(Municipality municipality: municipalities) {
            Long minCost = Long.MAX_VALUE;
            long timeIndex = 0;
            String minMunID = "";
            for (BasicGenome genome : chromosome) {
                List<Long> travelTimes = (List<Long>)  distanceMatrix.get(municipality.getMunId(), genome.getFacilityID());
                if(travelTimes!= null && travelTimes.size() > 0) {
                    int counter = 1;
                    for (Long travelTime : travelTimes) {
                        if (travelTime == null) {
                            minCost = 0l;
                            minMunID = municipality.getMunId();
                        } else if (travelTime < minCost) {
                            minMunID = genome.getFacilityID();
                            minCost = travelTime;
                            timeIndex = counter;
                        }
                        counter++;
                    }
                }else {
                    minMunID = municipality.getMunId();
                    minCost = 0l;
                }
            }
            totalCost += minCost * municipality.getWeight();
            result.add(new BasicGenome(municipality.getMunId(), minMunID,timeIndex));
        }
        LOGGER.info("Total Cost -> " + ((long)(totalCost / 60)));
        return result;
    }

}
