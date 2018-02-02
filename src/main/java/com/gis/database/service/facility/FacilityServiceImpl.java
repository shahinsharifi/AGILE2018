package com.gis.database.service.facility;



import com.gis.database.dao.facility.FacilityDao;
import com.gis.database.dao.municipality.MunicipalityDao;
import com.gis.database.model.Facility;
import com.gis.database.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityDao facilityDao;

    @Override
    public void insert(Facility facility) {
        facilityDao.save(facility);
    }

    @Override
    public void deleteAll() {
        facilityDao.deleteAll();
    }
}