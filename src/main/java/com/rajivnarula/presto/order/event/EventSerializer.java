package com.rajivnarula.presto.order.event;

import java.util.ArrayList;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.PersistedEvent;

public class EventSerializer {

	public static List <Event> deserialize (List <PersistedEvent> persistedEvents ) throws Exception{
		List<Event> list = new ArrayList <Event>(persistedEvents.size());
	    for (final PersistedEvent aPersistedEvent : persistedEvents) {
	    		String className = aPersistedEvent.getEventName();
	    		String theEvent = aPersistedEvent.getTheEvent();
	    		Event e = deserilazeEvent (theEvent, className); 
	    		list.add(e);
	    }
		return list ;
	}

	public static List <PersistedEvent> serialize (List <Event> events ){
		List<PersistedEvent> list = new ArrayList <PersistedEvent>(events.size());
	    for (final Event anEvent : events) {
	    		Class eventClass = anEvent.getClass() ;
			String eventName = eventClass.getName() ;
			String objectId = anEvent.getOrderId().toString();
			PersistedEvent persistedEvent = new PersistedEvent(anEvent, eventName, objectId);
			list.add(persistedEvent);
	    }
		
		return list ;
	}
	
	private static Event deserilazeEvent (String json, String type) throws Exception {
		Class c = Class.forName(type);
	    Gson gson = new GsonBuilder().create();
		Event event = (Event)gson.fromJson(json, c);
	    return event;
		
	}
	
	
}
