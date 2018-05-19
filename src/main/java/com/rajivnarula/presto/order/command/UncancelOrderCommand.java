
package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

/**
 * Un-cancel Order Command
 * 
 * */

public class UncancelOrderCommand implements Command {

    private final UUID orderId;

    public UncancelOrderCommand(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID aggregateId() {
        return orderId;
    }


	@Override
	public String toString() {
		return "UncancelOrderCommand [orderId=" + orderId + "]";
	}
    

    
}