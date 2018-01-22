package com.gis.optimizer;

import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.database.service.dmatrix.DmatrixServiceImpl;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
import com.gis.optimizer.evaluator.SolutionEvaluator;
import com.gis.optimizer.factory.OperationFactory;
import com.gis.optimizer.factory.PopulationFactory;
import com.gis.optimizer.helper.DistanceMatrixHelper;
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


    @Autowired
    private DmatrixServiceImpl dmatrixManagerService;

    @Autowired
    private MunicipalityServiceImpl municipalityService;


    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationEngine.class);


    public int evolve() throws Exception {

        List<Dmatrix> dmatrix = dmatrixManagerService.getAll();
        LOGGER.info("Distances are loaded...");

        List<Municipality> municipalities = municipalityService.getAll();
        LOGGER.info("Municipalities are loaded...");

        DistanceMatrixHelper distanceMatrixHelper = new DistanceMatrixHelper(dmatrix);
        Table distanceMatrix = distanceMatrixHelper.getDistanceTable();


        Random rng = new XORShiftRNG();


        //Generating random population
        CandidateFactory<List<BasicGenome>> candidateFactory = new PopulationFactory<>(
                new ArrayList<>(),
                new ArrayList<>()
        );


        //Configuration of selection strategy
        SelectionStrategy<Object> selection = new TournamentSelection(
                new AdjustableNumberGenerator<>(new Probability(0.99d))
        );


        //Configuration of operation pipeline
        OperationFactory Operations = new OperationFactory();
        EvolutionaryOperator<List<BasicGenome>> pipeline = Operations.createEvolutionPipeline(rng);


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


        //Running evolutionry algorithm
        List<BasicGenome> result  = engine.evolve(
                15,
                2,
                new TargetFitness(100, false)
        );


        return result.size();
    }

}
