package com.rajivnarula.presto.order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.order.command.CancelOrderCommand;
import com.rajivnarula.presto.order.command.ChangeOrderNameCommand;
import com.rajivnarula.presto.order.command.CreateOrderCommand;
import com.rajivnarula.presto.order.event.OrderCanceledEvent;
import com.rajivnarula.presto.order.event.OrderChangedEvent;
import com.rajivnarula.presto.order.event.OrderCreatedEvent;

public class OrderAggregate {
	
    private UUID orderId;
    private String name ;
    private final List<Event> mutatingEvents ;
    private OrderStatus status = OrderStatus.NONE;
    private String reasonForCancelation ;
    
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

	public List<Event> mutatingEvents() {
	    return mutatingEvents;
	}
	
	
	
	public UUID getOrderId() {
		return orderId;
	}


	public String getName() {
		return name;
	}


	private void handle (CreateOrderCommand createOrderCommand) {
		if (status != OrderStatus.NONE) {
			throw new RuntimeException ("Invalid command sequence. Order is already created");
		}
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent (createOrderCommand.aggregateId(), createOrderCommand.name()); 
		mutatingEvents.add(orderCreatedEvent);
	}

	public List <Event> handle (ChangeOrderNameCommand changeOrderNameCommand) {
		if ((status == OrderStatus.NONE)|| (status == OrderStatus.CANCELED)) {
			throw new RuntimeException ("Invalid command sequence. ");
		}

		OrderChangedEvent orderChangedEvent = new OrderChangedEvent (changeOrderNameCommand.aggregateId(), changeOrderNameCommand.newName()); 
		mutatingEvents.add(orderChangedEvent);
		apply (orderChangedEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderChangedEvent);
		return newEvents;
	}

	public List <Event> handle (CancelOrderCommand cancelOrderCommand) {
		if ((status == OrderStatus.NONE)) {
			throw new RuntimeException ("Invalid command sequence. ");
		}

		OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent (cancelOrderCommand.aggregateId(), cancelOrderCommand.reason()); 
		mutatingEvents.add(orderCanceledEvent);
		apply (orderCanceledEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderCanceledEvent);
		return newEvents;
	}
	
	
	
	private void apply (Event event) {
		if (event instanceof OrderCreatedEvent) {
			apply ((OrderCreatedEvent)event);
		}else if (event instanceof OrderChangedEvent) {
			apply ((OrderChangedEvent)event);
		}else if (event instanceof OrderCanceledEvent) {
			apply ((OrderCanceledEvent)event);
		}else {
			throw new RuntimeException ();
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
		reasonForCancelation = orderCanceledEvent.reason();
		System.out.println("Apply OrderCanceledEvent>>>>");
	}
	
	private void apply (List<Event> eventStream) {
	    for (final Event anEvent : eventStream) {
	    		apply (anEvent);
	    }
		
	}

	
}
