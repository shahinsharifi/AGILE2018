package com.gis.database.service.pmedian;



import com.gis.database.dao.municipality.MunicipalityDao;
import com.gis.database.dao.pmedian.PmedianDao;
import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.database.model.Pmedian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class PmedianServiceImpl implements PmedianService {

    @Autowired
    private PmedianDao pmedianDao;

    @Override
    public void insert(Pmedian pmedian) {
        pmedianDao.save(pmedian);
    }

    @Override
    public List<Pmedian> getAll() {
        List<Pmedian> target = new ArrayList<>();
        pmedianDao.findAll().iterator().forEachRemaining(target::add);
        return target;
    }

    @Override
    public void deleteAll() {
        pmedianDao.deleteAll();
    }
}