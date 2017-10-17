package com.rajivnarula.presto.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.order.command.AddLineItemCommand;
import com.rajivnarula.presto.order.command.CancelOrderCommand;
import com.rajivnarula.presto.order.command.ChangeOrderNameCommand;
import com.rajivnarula.presto.order.command.CreateOrderCommand;
import com.rajivnarula.presto.order.command.ShipOrderCommand;
import com.rajivnarula.presto.order.event.LineitemAddedEvent;
import com.rajivnarula.presto.order.event.OrderCanceledEvent;
import com.rajivnarula.presto.order.event.OrderChangedEvent;
import com.rajivnarula.presto.order.event.OrderCreatedEvent;
import com.rajivnarula.presto.order.event.OrderShippedEvent;

public class OrderAggregate {
	
    private UUID orderId;
    private String name ;
    private final List<Event> mutatingEvents ;
    private OrderStatus status = OrderStatus.NONE;
    private String reasonForCancelation ;
    private  Map<String, Long> lineItems = new HashMap<String,Long> ();
    
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
	
	
	
	public OrderStatus getStatus() {
		return status;
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

	public List <Event> handle (ShipOrderCommand shipOrderCommand) {
		if (status != OrderStatus.INITIALIZED) {
			throw new RuntimeException ("Invalid command sequence. ");
		}

		OrderShippedEvent orderShippedEvent = new OrderShippedEvent (shipOrderCommand.aggregateId()); 
		mutatingEvents.add(orderShippedEvent);
		apply (orderShippedEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(orderShippedEvent);
		return newEvents;
		
	}

	public List <Event> handle (AddLineItemCommand addItemCommand) {
		if ((status == OrderStatus.NONE)||(status == OrderStatus.CANCELED)) {
			throw new RuntimeException ("Invalid command sequence. ");
		}

		LineitemAddedEvent itemAddedEvent = new LineitemAddedEvent (addItemCommand.aggregateId(), addItemCommand.sku(), addItemCommand.quantiy()); 
		mutatingEvents.add(itemAddedEvent);
		apply (itemAddedEvent);
		List <Event> newEvents = new ArrayList<Event> ();
		newEvents.add(itemAddedEvent);
		return newEvents;
	}
	
	
	private void apply (Event event) {
		if (event instanceof OrderCreatedEvent) {
			apply ((OrderCreatedEvent)event);
		}else if (event instanceof OrderChangedEvent) {
			apply ((OrderChangedEvent)event);
		}else if (event instanceof OrderCanceledEvent) {
			apply ((OrderCanceledEvent)event);
		}else if (event instanceof OrderShippedEvent) {
			apply ((OrderShippedEvent)event);
		}else if (event instanceof LineitemAddedEvent) {
			apply ((LineitemAddedEvent)event);
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
		reasonForCancelation = orderCanceledEvent.reason();
		System.out.println("Apply OrderCanceledEvent>>>>");
	}

	private void apply (LineitemAddedEvent itemAddedEvent) {
		status = OrderStatus.INITIALIZED ;
		addLineItem (itemAddedEvent.sku(), itemAddedEvent.quantiy());
		System.out.println("Apply ItemAddedEvent>>>>");
	}

	private void apply (OrderShippedEvent orderShippedEvent) {
		status = OrderStatus.SHIPPED ;
		System.out.println("Apply OrderShippedEvent>>>>");
	}
	
	private void addLineItem (String sku, long qty) {
		Long quantity = lineItems.get(sku) ;
		if (quantity == null) {
			quantity = new Long(qty);
		}else {
			quantity += new Long(qty);
		}
		lineItems.put(sku,quantity) ;
	}
	
	public Long getQuantity (String sku) {
		return lineItems.get(sku) ;
	}
	
	private void apply (List<Event> eventStream) {
	    for (final Event anEvent : eventStream) {
	    		apply (anEvent);
	    }
		
	}

	
}
