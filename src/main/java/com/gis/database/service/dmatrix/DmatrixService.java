package com.gis.database.service.dmatrix;



import com.gis.database.model.Dmatrix;

import java.util.List;

public interface DmatrixService {

    public void insert(Dmatrix dmatrix);

    public List<Dmatrix> getAll();

    public boolean existByStartIdAndEndId(String start, String end);

}