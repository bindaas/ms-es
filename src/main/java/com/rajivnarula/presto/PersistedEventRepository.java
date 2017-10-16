package com.rajivnarula.presto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PersistedEventRepository extends CrudRepository<PersistedEvent, Long>{
	
    List<PersistedEvent> findByObjectId(String objectId);


}




