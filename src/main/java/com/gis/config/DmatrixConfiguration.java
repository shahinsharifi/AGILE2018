package com.gis.config;


import com.gis.database.model.Dmatrix;
import com.gis.database.service.dmatrix.DmatrixServiceImpl;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

@Configuration
@PropertySources(value = {@PropertySource("classpath:/application.properties")})
public class DmatrixConfiguration {


    @Autowired
    private DmatrixServiceImpl dmatrixManagerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DmatrixConfiguration.class);

    private final static Table<String, String, Long> distanceMatrix = HashBasedTable.create();


    @Bean
    public boolean initDistanceMatrix() {

        List<Dmatrix> dmatrix = dmatrixManagerService.getAll();
        LOGGER.info("Distances are loaded...");

        LOGGER.info("Start initializing distance table...");
        for(Dmatrix distance: dmatrix){
            if(!distance.getStartNodeId().equals(distance.getEndNodeId()))
                distanceMatrix.put(distance.getStartNodeId(),distance.getEndNodeId(),distance.getDistance());
        }
        LOGGER.info("Distance table is initialized...");


        LOGGER.info("Destroying the initial distance matrix...");
        dmatrix.clear();

        return true;
    }

    public static Table getDistanceMatrix(){
        return distanceMatrix;
    }

}
