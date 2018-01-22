package com.gis.optimizer.operation;

import com.gis.optimizer.model.BasicGenome;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;


public class Mutation implements EvolutionaryOperator<BasicGenome> {

    private final NumberGenerator<Double> mutationAmount;
    private final NumberGenerator<Probability> mutationProbability;


    public Mutation(NumberGenerator<Probability> mutationProbability, NumberGenerator<Double> mutationAmount) {
        this.mutationProbability = mutationProbability;
        this.mutationAmount = mutationAmount;
    }


    @Override
    public List<BasicGenome> apply(List<BasicGenome> list, Random random) {
        return list;
    }


    private BasicGenome mutateTerritory(BasicGenome genome, Random rng){

        if (mutationProbability.nextValue().nextEvent(rng))
        {
            return genome;
        }
        else
        {
            return genome;
        }
    }

}
