package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

public class CreateOrderCommand implements Command {

    private final UUID orderId;
    private final String name ;

    public CreateOrderCommand(UUID orderId, String name) {
    		if ((orderId == null) || (name == null) || (name.isEmpty())) {
    			throw new IllegalArgumentException ();
    		}
        this.orderId = orderId;
        this.name = name;
    }

    public UUID aggregateId() {
        return orderId;
    }

    public String name() {
        return name;
    }

	@Override
	public String toString() {
		return "CreateOrderCommand [orderId=" + orderId + ", name=" + name + "]";
	}
    

    
}