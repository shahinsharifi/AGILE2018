package com.gis.optimizer.factory;

import com.gis.optimizer.model.BasicGenome;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PopulationFactory <T>  extends AbstractCandidateFactory<List<T>>{

    private final List<String> facilities;
    private final List<String> demands;



    public PopulationFactory(List<String> facilities, List<String> demands) {
        this.facilities = facilities;
        this.demands = demands;
    }


    public List<T> generateRandomCandidate(Random rng) {
        List<T> candidate = new ArrayList<>();
        int j = 0;
        for (int i = j; i < facilities.size(); i++) {
            candidate.add((T) new BasicGenome(facilities.get(i), facilities.get(j)));
        }
        return candidate;
    }


    private class CustomComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(T a, T b) {
            return a.compareTo(b);
        }
    }

}
