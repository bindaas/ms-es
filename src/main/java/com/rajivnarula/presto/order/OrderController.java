package com.rajivnarula.presto.order;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.PersistedEvent;
import com.rajivnarula.presto.PersistedEventRepository;
import com.rajivnarula.presto.order.command.CancelOrderCommand;
import com.rajivnarula.presto.order.command.ChangeOrderNameCommand;
import com.rajivnarula.presto.order.command.CreateOrderCommand;
import com.rajivnarula.presto.order.command.UncancelOrderCommand;
import com.rajivnarula.presto.order.event.EventSerializer;
import com.rajivnarula.presto.order.event.OrderCreatedEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OrderController {

    @Autowired
    private PersistedEventRepository persistedEventRepository ;

    @Autowired
    private OrderSnapshotRepository orderSnapshotRepository ;
	
    @RequestMapping(value = "/createOrderCommand" , method = RequestMethod.POST)
    public String createOrderCommand(@RequestParam(value="name", defaultValue="any name") String name) {
		System.out.println("createOrderCommand>>>>");

		UUID orderId = UUID.randomUUID();
		CreateOrderCommand orderCommand = new CreateOrderCommand (orderId, name);
		OrderAggregate orderAggregate = new OrderAggregate (orderCommand);
		List<Event> events = orderAggregate.mutatingEvents();
		
		List<PersistedEvent> newPersistedEvents = EventSerializer.serialize(events);
		persistedEventRepository.save(newPersistedEvents);
		
        return orderId.toString();
    }

    
    @RequestMapping("/changeOrderNameCommand")
    public String updateOrderCommand(@RequestParam(value="newName") String newName , @RequestParam(value="orderId", defaultValue="any orderId") String orderId)  throws Exception{
		System.out.println("updateOrderCommand>>>>");
    		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectId(orderId);
    		
    		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
    		UUID orderUUID = UUID.fromString(orderId);
    		
    		ChangeOrderNameCommand changeOrderNameCommand = new ChangeOrderNameCommand (orderUUID, newName);
    		OrderAggregate orderAggregate = new OrderAggregate (orderUUID,eventStream);
    		List<Event> events  = 	orderAggregate.handle (changeOrderNameCommand);
    		List<PersistedEvent> newPersistedEvents = EventSerializer.serialize(events);
    		persistedEventRepository.save(newPersistedEvents);
        return orderId.toString();
    }

    @RequestMapping("/cancelOrderCommand")
    public String cancelOrderCommand(@RequestParam(value="orderId") String orderId)  throws Exception{
		System.out.println("cancelOrderCommand>>>>");
    		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectId(orderId);
    		
    		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
    		UUID orderUUID = UUID.fromString(orderId);
    		
    		CancelOrderCommand cancelOrderCommand = new CancelOrderCommand (orderUUID);
    		OrderAggregate orderAggregate = new OrderAggregate (orderUUID,eventStream);
    		List<Event> events  = 	orderAggregate.handle (cancelOrderCommand);
    		List<PersistedEvent> newPersistedEvents = EventSerializer.serialize(events);
    		persistedEventRepository.save(newPersistedEvents);
        return orderId.toString();
    }

    @RequestMapping("/uncancelOrderCommand")
    public String uncancelOrderCommand(@RequestParam(value="orderId") String orderId)  throws Exception{
		System.out.println("cancelOrderCommand>>>>");
    		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectId(orderId);
    		
    		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
    		UUID orderUUID = UUID.fromString(orderId);
    		
    		UncancelOrderCommand uncancelOrderCommand = new UncancelOrderCommand (orderUUID);
    		OrderAggregate orderAggregate = new OrderAggregate (orderUUID,eventStream);
    		List<Event> events  = 	orderAggregate.handle (uncancelOrderCommand);
    		List<PersistedEvent> newPersistedEvents = EventSerializer.serialize(events);
    		persistedEventRepository.save(newPersistedEvents);
        return orderId.toString();
    }
    
    
    @RequestMapping("/orderSnapshot")
    public String orderSnapshot( @RequestParam(value="orderId") String orderId)  throws Exception{
		System.out.println("orderSnapshot>>>>");
    		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectId(orderId);
    		
    		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
    		UUID orderUUID = UUID.fromString(orderId);
    		OrderAggregate orderAggregate = new OrderAggregate (orderUUID,eventStream);
    		OrderSnapshot orderSnapshot = new OrderSnapshot (orderAggregate);
    		orderSnapshotRepository.save(orderSnapshot);
    		
        return orderId.toString();
    }

    @RequestMapping("/orderAggregate")
    public OrderAggregate orderAggregate( @RequestParam(value="orderId") String orderId)  throws Exception{
		System.out.println("orderAggregate>>>> orderId:"+orderId);
    		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectId(orderId);
    		
    		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
    		UUID orderUUID = UUID.fromString(orderId);
    		OrderAggregate orderAggregate = new OrderAggregate (orderUUID,eventStream);
        return orderAggregate;
    }
    
    @RequestMapping("/orderAggregateSnapshot")
    public OrderAggregate orderAggregateSnapshot( @RequestParam(value="orderId") String orderId)  throws Exception{
		System.out.println("orderAggregateSnapshot>>>> orderId:"+orderId );
		List<OrderSnapshot>  orderSnapshots= orderSnapshotRepository.findTop1ByObjectIdOrderByVersionDesc(orderId);
		System.out.println("orderSnapshots:"+orderSnapshots);
		OrderSnapshot orderSnapshot = orderSnapshots.get(0); 
		Date snapshotDate = orderSnapshot.getSnapshotDate();
		int snasphotVersion = orderSnapshot.getVersion();
		List<PersistedEvent> persistedEvents = persistedEventRepository.findByObjectIdAndVersionGreaterThan(orderId,snasphotVersion );
		List<Event> eventStream  = EventSerializer.deserialize(persistedEvents) ;
		System.out.println("orderAggregateSnapshot>>>> events:"+eventStream.size()+" snasphotVersion:"+snasphotVersion);
		
		
    		OrderAggregate orderAggregate = new OrderAggregate(orderSnapshot,eventStream);
        return orderAggregate;
    }
    
    
}
