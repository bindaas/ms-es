package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

public class ShipOrderCommand implements Command {

    private final UUID orderId;

    public ShipOrderCommand(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID aggregateId() {
        return orderId;
    }


	@Override
	public String toString() {
		return "ShipOrderCommand [orderId=" + orderId + "]";
	}
    

    
}