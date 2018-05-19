package com.rajivnarula.presto.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.order.command.CancelOrderCommand;
import com.rajivnarula.presto.order.command.ChangeOrderNameCommand;
import com.rajivnarula.presto.order.command.CreateOrderCommand;
import com.rajivnarula.presto.order.command.UncancelOrderCommand;
import com.rajivnarula.presto.order.event.OrderCanceledEvent;
import com.rajivnarula.presto.order.event.OrderChangedEvent;
import com.rajivnarula.presto.order.event.OrderCreatedEvent;
import com.rajivnarula.presto.order.event.OrderUncanceledEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * This represents the "Aggregate" for the Events
 * OrderAggregate can be created in 3 ways. From-
 * 	An OrderCommand
 *  A list of events
 *  A Snapshot and events
 * 
 * */


public class OrderAggregate {
	
    private UUID orderId;
    private String name ;
    transient private final List<Event> mutatingEvents ;
    private OrderStatus status = OrderStatus.NONE;
    private Date eventStreamDate ;
    
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		super();
		mutatingEvents = new ArrayList<Event> ();
		handle (createOrderCommand);
		apply (mutatingEvents);
	}

	
	public OrderAggregate(UUID orderId, List<Event> eventStream) {
		super();
		this.orderId = orderId ;
		mutatingEvents = eventStream ;
		apply (mutatingEvents);
	}

	public OrderAggregate() {
		mutatingEvents = new ArrayList<Event> ();
	}

	public OrderAggregate(OrderSnapshot orderSnapshot, List<Event> eventStream) {
		OrderAggregate aggragtefromSnapshot = orderSnapshot.getOrderAggregate();
		this.orderId =  aggragtefromSnapshot.getOrderId();
		this.name = aggragtefromSnapshot.getName();
		this.status = aggragtefromSnapshot.getStatus();
		mutatingEvents = eventStream ;
		apply (mutatingEvents);
	}


	public List<Event> mutatingEvents() {
	    return mutatingEvents;
	}
	
	
	
	public OrderStatus getStatus() {
		return status;
	}


	public UUID getOrderId() {
		return orderId;
	}


	public String getName() {
		return name;
	}

	public int getVersion() {
		return mutatingEvents.size()-1;
	}
	
	
	public Date getEventStreamDate() {
		return eventStreamDate;
	}


	private void handle (CreateOrderCommand createOrderCommand) {
		if (status != OrderStatus.NONE) {
			throw new RuntimeException ("Invalid command sequence. Order is already created");
		}
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent (createOrderCommand.aggregateId(), createOrderCommand.name(),0); 
		mutatingEvents.add(orderCreatedEvent);
	}

	public List <Event> handle (ChangeOrderNameCommand changeOrderNameCommand) {
		if ((status == OrderStatus.NONE)|| (status == OrderStatus.CANCELED)) {
			throw new RuntimeException ("Invalid command sequence. ");
		}

		OrderChangedEvent orderChangedEvent = new OrderChangedEvent (changeOrderNameCommand.aggregateId(), changeOrderNameCommand.newName(), mutatingEvents.size()); 
		mutatingEvents.add(orderChangedEvent);
		apply (orderChangedEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderChangedEvent);
		return newEvents;
	}

	public List <Event> handle (CancelOrderCommand cancelOrderCommand) {
		if ((status == OrderStatus.NONE)) {
			throw new RuntimeException ("CancelOrderCommand.handle: Invalid command sequence. ");
		}

		OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent (cancelOrderCommand.aggregateId(), mutatingEvents.size()); 
		mutatingEvents.add(orderCanceledEvent);
		apply (orderCanceledEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderCanceledEvent);
		return newEvents;
	}

	public List <Event> handle (UncancelOrderCommand unCancelOrderCommand) {
		if ((status != OrderStatus.CANCELED)) {
			throw new RuntimeException ("UncancelOrderCommand.handle: Invalid command sequence. ");
		}
	
		OrderUncanceledEvent orderUncanceledEvent = new OrderUncanceledEvent (unCancelOrderCommand.aggregateId(), mutatingEvents.size()); 
		mutatingEvents.add(orderUncanceledEvent);
		apply (orderUncanceledEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderUncanceledEvent);
		return newEvents;
	}


	private void apply (Event event) {
		eventStreamDate = event.getEventDate() ;
		if (event instanceof OrderCreatedEvent) {
			apply ((OrderCreatedEvent)event);
		}else if (event instanceof OrderChangedEvent) {
			apply ((OrderChangedEvent)event);
		}else if (event instanceof OrderCanceledEvent) {
			apply ((OrderCanceledEvent)event);
		}else if (event instanceof OrderUncanceledEvent) {
			apply ((OrderUncanceledEvent)event);
		}else {
			throw new RuntimeException ("Unexpected event");
		}
	}

	private void apply (OrderCreatedEvent orderCreatedEvent) {
		orderId = orderCreatedEvent.getOrderId();
		name = orderCreatedEvent.getName();
		status = OrderStatus.CREATED ;
		System.out.println("Apply OrderCreatedEvent>>>>");
	}

	private void apply (OrderChangedEvent orderChangedEvent) {
		name = orderChangedEvent.newName();
		System.out.println("Apply OrderChangedEvent>>>>");
	}

	private void apply (OrderCanceledEvent orderCanceledEvent) {
		status = OrderStatus.CANCELED ;
		System.out.println("Apply OrderCanceledEvent>>>>");
	}

	private void apply (OrderUncanceledEvent orderUncanceledEvent) {
		status = OrderStatus.CREATED ;
		System.out.println("Apply OrderUncanceledEvent>>>>");
	}

	private void apply (List<Event> eventStream) {
	    for (final Event anEvent : eventStream) {
	    		apply (anEvent);
	    }
		
	}
	
}
