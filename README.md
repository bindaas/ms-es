# ms-es

## Pre-requisites:
* Java
* Gradle


## Commands:
* _To build the distribution_ gradle assemble
* _To start the web app_ java -jar build/libs/gs-rest-service-0.1.0.jar
* _To run all tests_ groovy src/test/groovy/test_all.groovy

# About
This program exhibits some of the key principles behind EventSourcing

* `Command` -> A Command sent to an Aggregate to **take some action**
* `Events` -> These are the **business events** that are emitted by the Aggregate. These events are persisted in the Database in a chronological order.
* `Aggregate` -> The **current state** of the domain object. This is put together from the event stream.
* `Snapshot` -> Since the numbers of events will grow infinitely, it sometimes make sense to create a **Snapshot of the Aggregate periodically** and then use the Snapshot to hyderate the Aggregate instead of reading a huge sum or events

# Implementation Details
This is a Java application written using `SpringBoot`.  
It has been tested to work against a `MySQL` database , but can be adopted for any RDBMS system.  
Moving to another NoSQL solution would need some code changes.  
It doesn't have a Web UI. The only way to interact with it is via APIs (listed below).  

## Domain
This program is modeled around a very simplistic domain - Order.  
You can Create an order. Change the name of the order. Cancel it. Uncancel it.

## APIs
All APIs with _Command_ in the URI and `orderSnapshot` are POST request that create a Command Object.  
Other APIs are GET Requests that return the OrderAggregate

* /createOrderCommand
* /changeOrderNameCommand
* /cancelOrderCommand
* /uncancelOrderCommand
* /orderSnapshot
* /orderAggregate
* /orderAggregateSnapshot


## Testing
`test_all.groovy` runs a series of tests on all the APIs confirming Aggregate, Snapshot behavior. It also verifies different commands

## References
https://vaughnvernon.co/ (Some of the code in this program has been taken from a workshop by Vaughn Vernon)  
https://martinfowler.com/eaaDev/EventSourcing.html
