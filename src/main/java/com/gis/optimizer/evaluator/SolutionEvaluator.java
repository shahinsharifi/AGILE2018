package com.gis.optimizer.evaluator;

import com.gis.database.model.Municipality;
import com.gis.optimizer.OptimizationEngine;
import com.gis.optimizer.model.BasicGenome;
import com.google.common.collect.Table;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.logging.Logger;

public class SolutionEvaluator implements FitnessEvaluator<List<BasicGenome>> {

    private final Table distanceMatrix;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SolutionEvaluator.class);

    public SolutionEvaluator(Table distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }


    @Override
    public double getFitness(List<BasicGenome> basicGenomes, List<? extends List<BasicGenome>> list) {

        double totalCost = 0;
        try {
            for (BasicGenome genome : basicGenomes) {
                Long demandWeight = genome.getWeight();
                Long distanceCost = (Long) distanceMatrix.get(genome.getDemandID(), genome.getFacilityID());
                if (demandWeight != null && distanceCost != null)
                    totalCost += demandWeight * distanceCost;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        return totalCost;
    }


    @Override
    public boolean isNatural() {
        return false;
    }
}
