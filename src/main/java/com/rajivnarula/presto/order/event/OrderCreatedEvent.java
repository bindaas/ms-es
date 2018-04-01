package com.rajivnarula.presto.order.event;

import java.util.Date;
import java.util.UUID;

import com.rajivnarula.presto.Event;

public class OrderCreatedEvent implements Event{
	
    private final UUID orderId;
    private final String name ;
    private final Date eventDate ;
    
	public OrderCreatedEvent(UUID orderId, String name) {
		super();
		this.orderId = orderId;
		this.name = name;
		this.eventDate = new Date ();
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String getName() {
		return name;
	}
	@Override
	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
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
		OrderCreatedEvent other = (OrderCreatedEvent) obj;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderCreatedEvent [orderId=" + orderId + ", name=" + name + ", eventDate=" + eventDate + "]";
	}

    

}
