package com.rajivnarula.presto.order;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OrderSnapshotRepository extends CrudRepository<OrderSnapshot, Long>{

	public List<OrderSnapshot> findTop1ByOrderByVersionDesc();
	public List<OrderSnapshot> findTop1ByObjectIdOrderByVersionDesc(String objectId);
    //public List<OrderSnapshot> findByObjectId(String objectId);


	
}