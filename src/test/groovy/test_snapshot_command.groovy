import groovy.json.*

// TEST Create Order

def ORDER_NAME = "qwerty"

println "Testing create order command"

def createOrderCommand = new URL("http://localhost:8080/createOrderCommand?name="+ORDER_NAME).openConnection();
def responseCode = createOrderCommand.getResponseCode();
assert (responseCode == 200)
def orderId = null 
orderId = createOrderCommand.getInputStream().getText() 
assert(orderId)

println "Testing create order command complete:" +orderId


def NEW_ORDER_NAME1 = "asdfg"

println "Changing order name to :"+NEW_ORDER_NAME1


def changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+NEW_ORDER_NAME1+"&orderId="+orderId).openConnection();
responseCode = changeOrderCommand.getResponseCode();
assert (responseCode == 200)
orderId = changeOrderCommand.getInputStream().getText() 
assert(orderId)

println "Verifying OrderAggregate"

def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
responseCode = getOrderAggregate.getResponseCode();
assert (responseCode == 200)
def orderAggregate = getOrderAggregate.getInputStream().getText() 
assert(orderAggregate);
def jsonSlurper = new JsonSlurper()
def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
assert NEW_ORDER_NAME1 == orderAggregateJSON.name

println "OrderAggregate verified:" +orderAggregate

	
def NEW_ORDER_NAME2 = "zxcvb"

println "Changing order name to :"+NEW_ORDER_NAME2


changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+NEW_ORDER_NAME2+"&orderId="+orderId).openConnection();
responseCode = changeOrderCommand.getResponseCode();
assert (responseCode == 200)
orderId = changeOrderCommand.getInputStream().getText() 
assert(orderId)

println "Verifying OrderAggregate"

getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
responseCode = getOrderAggregate.getResponseCode();
assert (responseCode == 200)
orderAggregate = getOrderAggregate.getInputStream().getText() 
assert (orderAggregate)
orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
assert NEW_ORDER_NAME2 == orderAggregateJSON.name
	
println "OrderAggregate verified:" +orderAggregate

println "Creating Snapshot"
	
def snapshotCommand = new URL("http://localhost:8080/orderSnapshot?orderId="+orderId).openConnection();
responseCode = snapshotCommand.getResponseCode();
assert (responseCode == 200)
orderId = snapshotCommand.getInputStream().getText() 
assert(orderId)

println "Creating Snapshot over"

println ("testing snapshots")
def getOrderAggregateSnapshot = new URL("http://localhost:8080/orderAggregateSnapshot?orderId="+orderId).openConnection();
responseCode = getOrderAggregateSnapshot.getResponseCode();
assert (responseCode == 200)
orderAggregate = getOrderAggregateSnapshot.getInputStream().getText() 
assert (orderAggregate)
assert NEW_ORDER_NAME2 == orderAggregateJSON.name
println ("testing snapshots:"+orderAggregate)
	