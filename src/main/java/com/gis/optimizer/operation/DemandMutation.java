package com.gis.optimizer.operation;

import com.gis.database.model.Municipality;
import com.gis.optimizer.model.BasicGenome;
import com.google.common.collect.Table;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;


public class DemandMutation implements EvolutionaryOperator<BasicGenome> {

    private final NumberGenerator<Double> mutationAmount;
    private final NumberGenerator<Probability> mutationProbability;
    private final List<Municipality> municipalities;
    private final Table dMatrix;


    public DemandMutation(NumberGenerator<Probability> mutationProbability, NumberGenerator<Double> mutationAmount,
                          List<Municipality> municipalities, Table dMatrix) {

        this.mutationProbability = mutationProbability;
        this.mutationAmount = mutationAmount;
        this.municipalities = municipalities;
        this.dMatrix = dMatrix;
    }


    @Override
    public List<BasicGenome> apply(List<BasicGenome> list, Random random) {
        return list;
    }


    private BasicGenome mutateTerritory(BasicGenome genome, Random rng) {

        if (mutationProbability.nextValue().nextEvent(rng)) {
            return genome;
        } else {
            return genome;
        }
    }

}
