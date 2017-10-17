package com.rajivnarula.presto.order.event;
import java.util.UUID;

import com.rajivnarula.presto.Event;


public class OrderShippedEvent implements Event{
	
    private final UUID orderId;
    
	public OrderShippedEvent(UUID orderId) {
		super();
		this.orderId = orderId;
	}

	public UUID getOrderId() {
		return orderId;
	}


	@Override
	public String toString() {
		return "OrderShippedEvent [orderId=" + orderId +  "]";
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
		OrderShippedEvent other = (OrderShippedEvent) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

    
    

}
