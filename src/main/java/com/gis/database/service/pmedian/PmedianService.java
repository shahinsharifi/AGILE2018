package com.gis.database.service.pmedian;


import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.database.model.Pmedian;

import java.util.List;

public interface PmedianService {

    public void insert(Pmedian dmatrix);

    public List<Pmedian> getAll();

    public void deleteAll();

}