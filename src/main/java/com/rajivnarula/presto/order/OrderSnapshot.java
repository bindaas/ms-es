package com.rajivnarula.presto.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rajivnarula.presto.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Entity
public class OrderSnapshot {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String theSnapshot;
    private String objectId;
    private int version ;
    private Date snapshotDate ;

    protected OrderSnapshot() {}

    public OrderSnapshot(OrderAggregate instance) {
		super();
		Gson gson = new GsonBuilder().create();

		this.theSnapshot = gson.toJson(instance);
		this.objectId = instance.getOrderId().toString();
		this.version = instance.getVersion();
		this.objectId = objectId;
		this.snapshotDate = instance.getEventStreamDate();
	}
    
    
    public String getSnapshot() {
		return theSnapshot;
	}

	public String getObjectId() {
		return objectId;
	}
    
	public int getVersion() {
		return version;
	}
	
    public Date getSnapshotDate() {
		return snapshotDate;
	}

	public OrderAggregate getOrderAggregate() {
	    Gson gson = new GsonBuilder().create();
	    OrderAggregate orderAggregate = (OrderAggregate)gson.fromJson(theSnapshot, OrderAggregate.class);
    	
		return orderAggregate;
	}

	@Override
	public String toString() {
		return "OrderSnapshot [id=" + id + ", theSnapshot=" + theSnapshot + ", objectId=" + objectId + ", version="
				+ version + ", snapshotDate=" + snapshotDate + "]";
	}
	
	

}