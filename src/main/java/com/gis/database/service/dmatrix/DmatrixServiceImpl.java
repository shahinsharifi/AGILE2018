package com.gis.database.service.dmatrix;


import com.gis.database.dao.dmatrix.DmatrixDao;
import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DmatrixServiceImpl implements DmatrixService {

    @Autowired
    private DmatrixDao dmatrixDao;

    @Override
    public void insert(Dmatrix dmatrix) {
        dmatrixDao.save(dmatrix);
    }

    @Override
    public List<Dmatrix> getAll() {
        List<Dmatrix> target = new ArrayList<>();
        dmatrixDao.findAll().iterator().forEachRemaining(target::add);
        return target;
    }

    @Override
    public boolean existByStartIdAndEndId(String start, String end) {
        return dmatrixDao.existsDmatrixByStartNodeIdAndEndNodeId(start,end);
    }
}