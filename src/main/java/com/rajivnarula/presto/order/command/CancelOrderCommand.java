package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

public class CancelOrderCommand implements Command {

    private final UUID orderId;

    public CancelOrderCommand(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID aggregateId() {
        return orderId;
    }


	@Override
	public String toString() {
		return "CancelOrderCommand [orderId=" + orderId + "]";
	}
    

    
}