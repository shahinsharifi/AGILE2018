package com.gis.database.service.municipality;


import com.gis.database.model.Municipality;

import java.util.List;

public interface MunicipalityService {

    public List<Municipality> getAll();
    public List<Municipality> getByID(String id);


}