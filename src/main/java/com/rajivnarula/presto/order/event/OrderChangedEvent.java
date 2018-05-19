package com.rajivnarula.presto.order.event;

import java.util.Date;
import java.util.UUID;

import com.rajivnarula.presto.Event;

/**
 * Order Changed Event
 * 
 * */

public class OrderChangedEvent implements Event{
	
    private final UUID orderId;
    private final String newName ;
    private final Date eventDate ;
    private final int version ;
    
	public OrderChangedEvent(UUID orderId, String newName, int version) {
		super();
		this.orderId = orderId;
		this.newName = newName;
		this.eventDate = new Date ();
		this.version = version ;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String newName() {
		return newName;
	}
	
	@Override
	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public int getVersion() {
		return version;
	}

	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + ((newName == null) ? 0 : newName.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderChangedEvent other = (OrderChangedEvent) obj;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (newName == null) {
			if (other.newName != null)
				return false;
		} else if (!newName.equals(other.newName))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderChangedEvent [orderId=" + orderId + ", newName=" + newName + ", eventDate=" + eventDate
				+ ", version=" + version + "]";
	}

    

}
