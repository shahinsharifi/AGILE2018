package com.gis.database.service.facility;


import com.gis.database.model.Dmatrix;
import com.gis.database.model.Facility;
import com.gis.database.model.Municipality;

import java.util.List;

public interface FacilityService {

    public void insert(Facility facility);

    public void deleteAll();

}