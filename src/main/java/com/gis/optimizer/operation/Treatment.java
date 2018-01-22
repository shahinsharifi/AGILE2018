package com.gis.optimizer.operation;

import com.gis.optimizer.model.BasicGenome;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

public class Treatment implements EvolutionaryOperator<BasicGenome> {

    public Treatment() {}

    @Override
    public List<BasicGenome> apply(List<BasicGenome> list, Random random) {
        return null;
    }
}
