package com.gis.optimizer.evaluator;

import com.gis.optimizer.model.BasicGenome;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class SolutionEvaluator implements FitnessEvaluator<List<BasicGenome>> {


    @Override
    public double getFitness(List<BasicGenome> basicGenomes, List<? extends List<BasicGenome>> list) {
        return 0;
    }

    @Override
    public boolean isNatural() {
        return false;
    }
}
