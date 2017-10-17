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
		List <Event> mutatingEvents = orderAggregate.mutatingEvents();
	    assertEquals(mutatingEvents.size(), 1);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "testName");
	    assertEquals(orderAggregate.getStatus(), OrderStatus.CREATED);
	    
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		assertEquals(mutatingEvents, expectedEventStream);
	    
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
		List <Event> mutatingEvents = orderAggregate.mutatingEvents();
	    assertEquals(mutatingEvents.size(), 1);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "testName");
	    
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		assertEquals(mutatingEvents, expectedEventStream);
	    assertEquals(orderAggregate.getStatus(), OrderStatus.CREATED);
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
		List <Event> newEvents =  orderAggregate.handle(changeOrderNameCommand);
	    assertEquals(newEvents.size(), 1);

		List <Event> mutatingEvents = orderAggregate.mutatingEvents();
		
		List<Event> expectedEventStream = new ArrayList <Event>();
		expectedEventStream.add(new OrderCreatedEvent (orderId , "testName"));
		expectedEventStream.add(new OrderChangedEvent (orderId , "noname"));

		assertEquals(mutatingEvents, expectedEventStream);
		
	    assertEquals(mutatingEvents.size(), 2);
	    assertEquals(orderAggregate.getOrderId(), orderId);
	    assertEquals(orderAggregate.getName(), "noname");
	}
	
	@Test
	public void testCancelOrderCommand() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		CreateOrderCommand createOrderCommand = new CreateOrderCommand( orderId,  name);
		
		OrderAggregate orderAggregate = new OrderAggregate (createOrderCommand);
		
	    assertEquals(orderAggregate.getStatus(), OrderStatus.CREATED);
	    CancelOrderCommand cancelOrderCommand = new CancelOrderCommand( orderId,  "Test Reason");
	    List <Event> newEvents =  orderAggregate.handle(cancelOrderCommand);
	    assertEquals(newEvents.size(), 1);
	    
		List <Event> mutatingEvents = orderAggregate.mutatingEvents();
	    assertEquals(mutatingEvents.size(), 2);
	    assertEquals(orderAggregate.getStatus(), OrderStatus.CANCELED);
	    
		ChangeOrderNameCommand changeOrderNameCommand = new ChangeOrderNameCommand( orderId,  "noname");
		try {
			orderAggregate.handle(changeOrderNameCommand);
			fail ("Should not be called");
		}catch (Exception ex) {
			assertTrue (true);
		}
		
		ShipOrderCommand shipOrderCommand = new ShipOrderCommand( orderId);
		try {
			orderAggregate.handle(shipOrderCommand);
			fail ("Should not be called");
		}catch (Exception ex) {
			assertTrue (true);
		}
	    
	}

	
	@Test
	public void testShipOrderCommand() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		CreateOrderCommand createOrderCommand = new CreateOrderCommand( orderId,  name);
		
		OrderAggregate orderAggregate = new OrderAggregate (createOrderCommand);
		
	    assertEquals(orderAggregate.getStatus(), OrderStatus.CREATED);
	    CancelOrderCommand cancelOrderCommand = new CancelOrderCommand( orderId,  "Test Reason");
	    List <Event> newEvents =  orderAggregate.handle(cancelOrderCommand);
	    assertEquals(newEvents.size(), 1);
	    
		ShipOrderCommand shipOrderCommand = new ShipOrderCommand( orderId);
		try {
			orderAggregate.handle(shipOrderCommand);
			fail ("Should not be called");
		}catch (Exception ex) {
			assertTrue (true);
		}
	    
	}
	
	@Test
	public void testAddLineitemCommand() {
		String name = "testName" ;
		byte[] nbyte = {10,20,30};
	       
		UUID orderId = UUID.nameUUIDFromBytes(nbyte); 
		CreateOrderCommand createOrderCommand = new CreateOrderCommand( orderId,  name);
		
		OrderAggregate orderAggregate = new OrderAggregate (createOrderCommand);
		assertEquals(orderAggregate.getStatus(), OrderStatus.CREATED);
	    
		AddLineItemCommand addLineItemCommand = new AddLineItemCommand( orderId,  "Sugar", 100);
	    List <Event> newEvents =  orderAggregate.handle(addLineItemCommand);
	    assertEquals(newEvents.size(), 1);
		assertEquals(orderAggregate.getStatus(), OrderStatus.INITIALIZED);
		assertEquals(orderAggregate.getQuantity("Sugar"), new Long(100));
		
		addLineItemCommand = new AddLineItemCommand( orderId,  "Sugar", 50);
		newEvents = orderAggregate.handle(addLineItemCommand);
	    assertEquals(newEvents.size(), 1);
		assertEquals(orderAggregate.getStatus(), OrderStatus.INITIALIZED);
		assertEquals(orderAggregate.getQuantity("Sugar"), new Long(150));
		
		
		ShipOrderCommand shipOrderCommand = new ShipOrderCommand( orderId);
		try {
			orderAggregate.handle(shipOrderCommand);
			assertTrue (true);
		}catch (Exception ex) {
			fail ("Should not be called");
		}
	    
		
	}
	
}
