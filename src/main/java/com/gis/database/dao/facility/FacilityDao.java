package com.gis.database.dao.facility;


import com.gis.database.model.Facility;
import com.gis.database.model.Municipality;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FacilityDao extends CrudRepository<Facility, Long> {

}
