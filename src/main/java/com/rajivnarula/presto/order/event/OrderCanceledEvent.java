package com.rajivnarula.presto.order.event;
import java.util.UUID;

import com.rajivnarula.presto.Event;

public class OrderCanceledEvent implements Event{
	
    private final UUID orderId;
    private final String reason ;
    
	public OrderCanceledEvent(UUID orderId, String reason) {
		super();
		this.orderId = orderId;
		this.reason = reason;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String reason() {
		return reason;
	}

	@Override
	public String toString() {
		return "OrderChangedEvent [orderId=" + orderId + ", reason=" + reason + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
		OrderCanceledEvent other = (OrderCanceledEvent) obj;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

    
    

}
