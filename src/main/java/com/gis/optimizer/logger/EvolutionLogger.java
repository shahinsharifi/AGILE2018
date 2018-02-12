package com.gis.optimizer.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EvolutionLogger<T> implements IslandEvolutionObserver<T> {

    long threadID;
    private Logger Logger = LoggerFactory.getLogger(EvolutionLogger.class);
    private static Map<Integer,Long> progress = new HashMap<>();

    public EvolutionLogger(long threadID){
        this.threadID = threadID;
    }


    public void populationUpdate(PopulationData<? extends T> data) {
        try {
            if(data.getGenerationNumber() % 100 == 0) {
                progress.put(data.getGenerationNumber() , (long) data.getBestCandidateFitness());
                Logger.info("Generation " + data.getGenerationNumber() + ": " + data.getBestCandidateFitness());
            }
        } catch (Exception ex) {
            Logger.error(ex.toString());
        }
    }


    public void islandPopulationUpdate(int islandIndex, PopulationData<? extends T> populationData) {
        // Do nothing.
    }

    public static Map<Integer, Long> getProgress() {
        return new TreeMap<Integer, Long>(progress);
    }
}


