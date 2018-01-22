package com.gis.database.dao.dmatrix;



import com.gis.database.model.Dmatrix;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DmatrixDao extends CrudRepository<Dmatrix, Long> {

    boolean existsDmatrixByStartNodeIdAndEndNodeId(String startNodeId, String endNodeId);
}
