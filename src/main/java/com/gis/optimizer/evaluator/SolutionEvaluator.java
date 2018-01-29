package com.gis.optimizer.evaluator;

import com.gis.database.model.Municipality;
import com.gis.optimizer.model.BasicGenome;
import com.google.common.collect.Table;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class SolutionEvaluator implements FitnessEvaluator<List<BasicGenome>> {

    private final Table distanceMatrix;
    private final List<Municipality> municipalities;


    public SolutionEvaluator(Table distanceMatrix, List<Municipality> municipalities) {
        this.distanceMatrix = distanceMatrix;
        this.municipalities = municipalities;
    }


    @Override
    public double getFitness(List<BasicGenome> basicGenomes, List<? extends List<BasicGenome>> list) {
        for(BasicGenome genome:basicGenomes){

        }
        return 0;
    }

    @Override
    public boolean isNatural() {
        return false;
    }
}
