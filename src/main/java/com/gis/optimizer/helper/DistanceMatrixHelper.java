package com.gis.optimizer.helper;

import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.optimizer.OptimizationEngine;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DistanceMatrixHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceMatrixHelper.class);

    private final static Table<String, String, Long> distanceMatrix = HashBasedTable.create();


    public DistanceMatrixHelper(List<Dmatrix> distances) throws Exception{

        LOGGER.info("Start initializing distance table...");
        for(Dmatrix distance: distances){
            if(!distance.getStartNodeId().equals(distance.getEndNodeId()))
                distanceMatrix.put(distance.getStartNodeId(),distance.getEndNodeId(),distance.getDistance());
        }
        LOGGER.info("Distance table is initialized...");
    }

    public Table getDistanceTable(){
        return distanceMatrix;
    }
}
