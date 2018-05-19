package com.rajivnarula.presto.order;

import java.util.List;


/**
 * Repository to read-write OrderSnapshots
 * 
 * */


import org.springframework.data.repository.CrudRepository;

public interface OrderSnapshotRepository extends CrudRepository<OrderSnapshot, Long>{

	public List<OrderSnapshot> findTop1ByOrderByVersionDesc();
	public List<OrderSnapshot> findTop1ByObjectIdOrderByVersionDesc(String objectId);


	
}