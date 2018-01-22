package com.gis.optimizer.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

public class EvolutionLogger<T> implements IslandEvolutionObserver<T> {

    long threadID;
    private Logger LOG = LoggerFactory.getLogger(EvolutionLogger.class);

    public EvolutionLogger(long threadID){
        this.threadID = threadID;
    }


    public void populationUpdate(PopulationData<? extends T> data) {
        try {

            //Let log something here

        } catch (Exception ex) {
            LOG.error(ex.toString());
        }
    }


    public void islandPopulationUpdate(int islandIndex, PopulationData<? extends T> populationData) {
        // Do nothing.
    }
}


