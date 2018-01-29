package com.gis.database.dao.pmedian;


import com.gis.database.model.Municipality;
import com.gis.database.model.Pmedian;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PmedianDao extends CrudRepository<Pmedian, Long> {}
