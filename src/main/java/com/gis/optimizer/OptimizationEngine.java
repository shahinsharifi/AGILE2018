package com.gis.optimizer;

import com.gis.config.DmatrixConfiguration;
import com.gis.database.model.Municipality;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
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
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class OptimizationEngine {


    private Random rng;
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationEngine.class);

    @Autowired
    private MunicipalityServiceImpl municipalityService;


    public int evolve() throws Exception {

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
        SolutionEvaluator evaluator = new SolutionEvaluator();


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
                15,
                2,
                new TargetFitness(100, false)
        );


        return result.size();
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

}
