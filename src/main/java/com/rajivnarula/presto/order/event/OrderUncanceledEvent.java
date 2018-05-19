package com.rajivnarula.presto.order.event;
import java.util.Date;
import java.util.UUID;

import com.rajivnarula.presto.Event;

/**
 * Order Uncanceled Event
 * 
 * */

public class OrderUncanceledEvent implements Event{
	
    private final UUID orderId;
    private final Date eventDate ;
    private final int version ;
    
	public OrderUncanceledEvent(UUID orderId, int version) {
		super();
		this.orderId = orderId;
		this.eventDate = new Date ();
		this.version = version ;
	}

	public UUID getOrderId() {
		return orderId;
	}


	@Override
	public String toString() {
		return "OrderUncanceledEvent [orderId=" + orderId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		OrderUncanceledEvent other = (OrderUncanceledEvent) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	@Override
	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public int getVersion() {
		return version;
	}

    
    

}
