package com.gis.database.dao.municipality;


import com.gis.database.model.Municipality;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MunicipalityDao extends CrudRepository<Municipality, Long> {

    List<Municipality> findByMunId(String munId);

}
