package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

/**
 * Change Order Name Command
 * 
 * */


public class ChangeOrderNameCommand implements Command {

    private final UUID orderId;
    private final String newName ;

    public ChangeOrderNameCommand(UUID orderId, String newName) {
		// Validate for input
    		if ((orderId == null) || (newName == null) || (newName.isEmpty())) {
			throw new IllegalArgumentException ();
		}
        this.orderId = orderId;
        this.newName = newName;
    }

    public UUID aggregateId() {
        return orderId;
    }

    public String newName() {
        return newName;
    }

	@Override
	public String toString() {
		return "ChangeOrderNameCommand [orderId=" + orderId + ", newName=" + newName + "]";
	}
    

    
}