package com.rajivnarula.presto;

import java.util.Date;
import java.util.UUID;


/**
 * All events will implement this interface.
 * In future- this can have more common functionality
 * 
 * */

public interface Event {
	
	public UUID getOrderId();
	public Date getEventDate ();
	public int getVersion ();
	
}
