package com.rajivnarula.presto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * The Event that will be persisted to the Database
 * 
 * */

@Entity
public class PersistedEvent {



	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String theEvent;
    private String eventName;
    private String objectId;
    private Date eventDate ;
    private int version ;

    protected PersistedEvent() {}

    public PersistedEvent(Object instance, String eventName, String objectId, Date eventDate, int version) {
		super();
		Gson gson = new GsonBuilder().create();

		this.theEvent = gson.toJson(instance);
		this.eventName = eventName;
		this.objectId = objectId;
		this.eventDate = eventDate;
		this.version = version;
		
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
    
    public Date getEventDate() {
		return eventDate;
	}
    
	public int getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "PersistedEvent [id=" + id + ", theEvent=" + theEvent + ", eventName=" + eventName + ", objectId="
				+ objectId + ", eventDate=" + eventDate + ", version=" + version + "]";
	}



}