package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

public class AddLineItemCommand implements Command {

    private final UUID orderId;
    private final String sku ;
    private final long quantiy ;

    public AddLineItemCommand(UUID orderId, String sku, long quantiy) {
		if ((sku == null) || (sku == null) || (sku.isEmpty()) || (quantiy <= 0)) {
			throw new IllegalArgumentException ();
		}
        this.orderId = orderId;
        this.sku = sku;
        this.quantiy = quantiy ;
    }

    public UUID aggregateId() {
        return orderId;
    }

    public String sku() {
        return sku;
    }

    public long quantiy() {
        return quantiy;
    }
    
	@Override
	public String toString() {
		return "AddItemCommand [orderId=" + orderId + ", sku=" + sku + "]";
	}
    

    
}