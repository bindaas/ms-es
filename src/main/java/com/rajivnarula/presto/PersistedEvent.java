package com.rajivnarula.presto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
public class PersistedEvent {



	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String theEvent;
    private String eventName;
    private String objectId;

    protected PersistedEvent() {}

    public PersistedEvent(Object instance, String eventName, String objectId) {
		super();
		Gson gson = new GsonBuilder().create();

		this.theEvent = gson.toJson(instance);
		this.eventName = eventName;
		this.objectId = objectId;
	}
    
    
    public String getTheEvent() {
		return theEvent;
	}

	public String getEventName() {
		return eventName;
	}

	public String getObjectId() {
		return objectId;
	}
    
    
    @Override
	public String toString() {
		return "EventPersisted [id=" + id + ", theEvent=" + theEvent + ", eventName=" + eventName + ", orderId="
				+ objectId + "]";
	}


}