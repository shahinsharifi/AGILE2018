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
    private final List<Municipality> municipalities;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SolutionEvaluator.class);

    public SolutionEvaluator(List<Municipality> municipalities, Table distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.municipalities = municipalities;
    }


    @Override
    public double getFitness(List<BasicGenome> basicGenomes, List<? extends List<BasicGenome>> list) {

        double totalCost = 0;
        try {
            for (Municipality municipality : municipalities) {
                totalCost += getNearestFacilityDistance(basicGenomes, municipality.getMunId());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        /*try {
            for (BasicGenome genome : basicGenomes) {
                Long demandWeight = genome.getWeight();
                Long distanceCost = (Long) distanceMatrix.get(genome.getDemandID(), genome.getFacilityID());
                if (demandWeight != null && distanceCost != null)
                    totalCost += demandWeight * distanceCost;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }*/
        return totalCost;
    }


    private long getNearestFacilityDistance(List<BasicGenome> chromosome, String demandID) {

        Long minDistance = Long.MAX_VALUE;
        for (BasicGenome genome : chromosome) {
            Long distance = (Long) distanceMatrix.get(demandID, genome.getFacilityID());
            if(distance == null) {
                minDistance = 0l;
            }else if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }


    @Override
    public boolean isNatural() {
        return false;
    }
}
