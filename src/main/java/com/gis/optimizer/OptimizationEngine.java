package com.gis.optimizer;

import com.gis.config.DmatrixConfiguration;
import com.gis.database.model.Municipality;
import com.gis.database.model.Pmedian;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
import com.gis.database.service.pmedian.PmedianService;
import com.gis.database.service.pmedian.PmedianServiceImpl;
import com.gis.optimizer.evaluator.SolutionEvaluator;
import com.gis.optimizer.factory.OperationFactory;
import com.gis.optimizer.factory.PopulationFactory;
import com.gis.optimizer.logger.EvolutionLogger;
import com.gis.optimizer.model.BasicGenome;
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

import java.util.*;


@Service
public class OptimizationEngine {


    private Random rng;
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationEngine.class);

    @Autowired
    private MunicipalityServiceImpl municipalityService;

    @Autowired
    private PmedianServiceImpl pmedianService;


    public boolean evolve() throws Exception {

        rng = new XORShiftRNG();

        List<Municipality> municipalities = municipalityService.getAll();
        LOGGER.info("Municipalities are loaded...");

        List<Municipality> initialSeed = getRandomInitialSeed(rng,municipalities, 20);

        Table distanceMatrix = DmatrixConfiguration.getDistanceMatrix();

        //Generating random population
        CandidateFactory<List<BasicGenome>> candidateFactory = new PopulationFactory<>(
                municipalities,
                initialSeed
        );


        //Configuration of selection strategy
        SelectionStrategy<Object> selection = new TournamentSelection(
                new AdjustableNumberGenerator<>(new Probability(0.99d))
        );


        //Configuration of operation pipeline
        OperationFactory Operations = new OperationFactory();
        EvolutionaryOperator<List<BasicGenome>> pipeline = Operations.createEvolutionPipeline(rng, municipalities, distanceMatrix);


        //Configuration of solution evaluator
        SolutionEvaluator evaluator = new SolutionEvaluator(distanceMatrix);


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


        //Running evolutionary algorithm
        List<BasicGenome> result = engine.evolve(
                100,
                20,
                new Stagnation(500,false)
        );


        //Cleaning up database
        pmedianService.deleteAll();
        LOGGER.info("Cleaning up database...");

        //Inserting result into the database
        LOGGER.info("Adding result to the database...");
        for(BasicGenome genome: result){
            Pmedian pmedian = new Pmedian();
            pmedian.setDemandId(genome.getDemandID());
            pmedian.setFacilityId(genome.getFacilityID());
            pmedianService.insert(pmedian);
        }

        calculateFacilityCapacity(result);
        LOGGER.info("End of processing ...");

        return result.size() > 0;
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


    private void calculateFacilityCapacity(List<BasicGenome> genomes){

        Map<String,Integer> result = new HashMap<>();
        for(BasicGenome genome: genomes){
            if(result.containsKey(genome.getFacilityID())){
                result.put(genome.getFacilityID(),result.get(genome.getFacilityID()) + 1);
            }else
                result.put(genome.getFacilityID(), 1);
        }

        for(String str: result.keySet()){
            LOGGER.info(str + " -> " + result.get(str));
        }
    }

}
