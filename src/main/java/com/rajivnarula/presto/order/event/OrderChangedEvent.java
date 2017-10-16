package com.rajivnarula.presto.order.event;

import java.util.UUID;

import com.rajivnarula.presto.Event;

public class OrderChangedEvent implements Event{
	
    private final UUID orderId;
    private final String newName ;
    
	public OrderChangedEvent(UUID orderId, String newName) {
		super();
		this.orderId = orderId;
		this.newName = newName;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String newName() {
		return newName;
	}

	@Override
	public String toString() {
		return "OrderChangedEvent [orderId=" + orderId + ", newName=" + newName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newName == null) ? 0 : newName.hashCode());
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
		OrderChangedEvent other = (OrderChangedEvent) obj;
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
		return true;
	}

    
    

}
