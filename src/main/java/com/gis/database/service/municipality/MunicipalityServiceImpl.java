package com.gis.database.service.municipality;



import com.gis.database.dao.municipality.MunicipalityDao;
import com.gis.database.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MunicipalityServiceImpl implements MunicipalityService {

    @Autowired
    private MunicipalityDao municipalityDao;

    @Override
    public List<Municipality> getAll() {
        List<Municipality> target = new ArrayList<>();
        municipalityDao.findAll().iterator().forEachRemaining(target::add);
        return target;
    }

    @Override
    public List<Municipality> getByID(String id) {
        List<Municipality> target = new ArrayList<>();
        municipalityDao.findByMunId(id).iterator().forEachRemaining(target::add);
        return target;
    }
}