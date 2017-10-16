package com.rajivnarula.presto.order.command;

import java.util.UUID;

import com.rajivnarula.presto.Command;

public class CancelOrderCommand implements Command {

    private final UUID orderId;
    private final String reason ;

    public CancelOrderCommand(UUID orderId, String reason) {
		if ((reason == null) || (reason == null) || (reason.isEmpty())) {
			throw new IllegalArgumentException ();
		}
        this.orderId = orderId;
        this.reason = reason;
    }

    public UUID aggregateId() {
        return orderId;
    }

    public String reason() {
        return reason;
    }

	@Override
	public String toString() {
		return "ChangeOrderNameCommand [orderId=" + orderId + ", reason=" + reason + "]";
	}
    

    
}