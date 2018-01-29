package com.gis.optimizer.factory;

import com.gis.database.model.Municipality;
import com.gis.optimizer.model.BasicGenome;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PopulationFactory <T>  extends AbstractCandidateFactory<List<T>> {

    private final List<Municipality> demands;
    private final List<Municipality> facilities;


    public PopulationFactory(List<Municipality> demands, List<Municipality> facilities) {
        this.demands = demands;
        this.facilities = facilities;
    }


    public List<T> generateRandomCandidate(Random rng) {

        List<T> candidate = new ArrayList<>();

        for (Municipality municipality : this.demands) {
            int randomIndex = rng.nextInt(this.facilities.size());
            candidate.add((T) new BasicGenome(municipality.getMunId(), this.facilities.get(randomIndex).getMunId()));
        }

        return candidate;
    }
}
