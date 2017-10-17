package com.rajivnarula.presto.order.event;

import java.util.UUID;

import com.rajivnarula.presto.Event;

public class LineitemAddedEvent implements Event{
	
    private final UUID orderId;
    private final String sku ;
    private final long quantiy ;
    
	public LineitemAddedEvent(UUID orderId, String sku, long quantiy) {
		super();
		this.orderId = orderId;
		this.sku = sku;
		this.quantiy = quantiy;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public String sku() {
		return sku;
	}

    public long quantiy() {
        return quantiy;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + (int) (quantiy ^ (quantiy >>> 32));
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
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
		LineitemAddedEvent other = (LineitemAddedEvent) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (quantiy != other.quantiy)
			return false;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemAddedEvent [orderId=" + orderId + ", sku=" + sku + ", quantiy=" + quantiy + "]";
	}
    
    

}
