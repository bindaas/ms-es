package com.rajivnarula.presto.order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rajivnarula.presto.Event;
import com.rajivnarula.presto.PersistedEvent;
import com.rajivnarula.presto.PersistedEventRepository;
import com.rajivnarula.presto.order.command.ChangeOrderNameCommand;
import com.rajivnarula.presto.order.command.CreateOrderCommand;
import com.rajivnarula.presto.order.event.EventSerializer;
import com.rajivnarula.presto.order.event.OrderCreatedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OrderCommandController {

    @Autowired
    private PersistedEventRepository persistedEventRepository ;
	
    @RequestMapping("/createOrderCommand")
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
    public String updateOrderCommand(@RequestParam(value="newName", defaultValue="any newName") String newName , @RequestParam(value="orderId", defaultValue="any orderId") String orderId)  throws Exception{
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
    
    
}
