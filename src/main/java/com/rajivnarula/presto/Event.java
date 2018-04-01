package com.rajivnarula.presto;

import java.util.Date;
import java.util.UUID;

public interface Event {
	
	public UUID getOrderId();
	public Date getEventDate ();
}
