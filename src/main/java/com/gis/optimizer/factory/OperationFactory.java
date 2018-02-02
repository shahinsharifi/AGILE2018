package com.gis.optimizer.factory;

import com.gis.database.model.Municipality;
import com.gis.optimizer.model.BasicGenome;
import com.gis.optimizer.operation.CrossOver;
import com.gis.optimizer.operation.DemandMutation;
import com.google.common.collect.Table;
import org.uncommons.maths.number.AdjustableNumberGenerator;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListOperator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class OperationFactory {

    public EvolutionaryOperator<List<BasicGenome>> createEvolutionPipeline(
            Random rng,
            List<Municipality> municipalities,
            Table dMatrix) {

        List<EvolutionaryOperator<List<BasicGenome>>> operators = new LinkedList<>();

        //Installing crossover operator
        operators.add(
                new CrossOver<>(
                        new ConstantGenerator<>(1),
                        new AdjustableNumberGenerator<>(new Probability(0.9d))
                )
        );

        //Installing mutation operator
        operators.add(
                new ListOperator<>(
                        new DemandMutation(
                                new AdjustableNumberGenerator<>(new Probability(0.00001d)),
                                new GaussianGenerator(0, 3, rng),
                                municipalities,
                                dMatrix
                        )
                )
        );

        return new EvolutionPipeline<>(operators);
    }

}
