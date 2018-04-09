package com.gis.config;


import com.gis.database.model.Dmatrix;
import com.gis.database.service.dmatrix.DmatrixServiceImpl;
import com.gis.optimizer.math.Statistics;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySources(value = {@PropertySource("classpath:/application.properties")})
public class DmatrixConfiguration {


    @Autowired
    private DmatrixServiceImpl dmatrixManagerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DmatrixConfiguration.class);

    private final static Table<String, String, List<Long>> distanceMatrix = HashBasedTable.create();


    @Bean
    public boolean initDistanceMatrix() {

        List<Dmatrix> dmatrix = dmatrixManagerService.getAll();
        LOGGER.info("Distances are loaded...");

        List<Double> sDeviations = new ArrayList<>();

        LOGGER.info("Start initializing distance table...");
        for(Dmatrix distance: dmatrix){
            if(!distance.getStartNodeId().equals(distance.getEndNodeId())) {
                List<Long> travelTimes = new ArrayList<Long>() {{
                    add(distance.getEuclidean().longValue());
                  //  add(distance.getMon12());
                  //  add(distance.getMon17());
                }};
                distanceMatrix.put(distance.getStartNodeId(), distance.getEndNodeId(), travelTimes);
                Statistics statistics = new Statistics(travelTimes);
                double sdv = statistics.calculateStandardDeviation();
                sDeviations.add(sdv);
            }
        }
        LOGGER.info("Distance table is initialized...");

        Statistics statistics = new Statistics(sDeviations);
        LOGGER.info("Mean is: " + statistics.calculateMean());
        LOGGER.info("Destroying the initial distance matrix...");
        dmatrix.clear();

        return true;
    }

    public static Table getDistanceMatrix(){
        return distanceMatrix;
    }

}
