package com.rajivnarula.presto.order;

import static org.junit.Assert.*;

import java.util.UUID;
import java.util.* ;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rajivnarula.presto.order.command.* ;
import com.rajivnarula.presto.order.event.* ;
import com.rajivnarula.presto.order.* ;
import com.rajivnarula.presto.* ;


public class OrderAggregateTest {
	private static final Logger logger = LoggerFactory.getLogger(OrderAggregateTest.class);	

	@Test
	public void testOrderCreateCommand() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		CreateOrderCommand createOrderCommand = new CreateOrderCommand( orderId,  name);
		
		OrderAggregate orderAggregate = new OrderAggregate (createOrderCommand);
		List <Event> events = orderAggregate.mutatingEvents();
	    assertEquals(events.size(), 1);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "testName");
	    
	    
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		assertEquals(events, expectedEventStream);
	    
	}
	
	@Test
	public void testOrderAggragateConstructor() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent( orderId,  name);
		List<Event> eventStream = new ArrayList <Event>();
		eventStream.add(orderCreatedEvent);
		
		OrderAggregate orderAggregate = new OrderAggregate (orderId,eventStream);
		List <Event> events = orderAggregate.mutatingEvents();
	    assertEquals(events.size(), 1);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "testName");
	    
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		assertEquals(events, expectedEventStream);
	    
	}

	@Test
	public void testChangeNameCommand() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent( orderId,  name);
		List<Event> eventStream = new ArrayList <Event>();
		eventStream.add(orderCreatedEvent);
		OrderAggregate orderAggregate = new OrderAggregate (orderId,eventStream);
		
		ChangeOrderNameCommand changeOrderNameCommand = new ChangeOrderNameCommand( orderId,  "noname");
		orderAggregate.handle(changeOrderNameCommand);

		List <Event> events = orderAggregate.mutatingEvents();
		
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		expectedEventStream.add(new OrderChangedEvent (orderId , "noname"));

		assertEquals(events, expectedEventStream);
		
	    assertEquals(events.size(), 2);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "noname");
	}
	
	
	
}
