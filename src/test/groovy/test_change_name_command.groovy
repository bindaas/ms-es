import groovy.json.*

// TEST Create Order

def ORDER_NAME = "qwerty"

def createOrderCommand = new URL("http://localhost:8080/createOrderCommand?name="+ORDER_NAME).openConnection();
def responseCode = createOrderCommand.getResponseCode();
println(responseCode);
def orderId = null 
if(responseCode.equals(200)) {
    orderId = createOrderCommand.getInputStream().getText() 
    println(orderId);
}

def NEW_ORDER_NAME = "asdfg"


def changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+NEW_ORDER_NAME+"&orderId="+orderId).openConnection();
responseCode = changeOrderCommand.getResponseCode();
println(responseCode);
if(responseCode.equals(200)) {
    orderId = changeOrderCommand.getInputStream().getText() 
    println(orderId);
}

	def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	responseCode = getOrderAggregate.getResponseCode();
	println(responseCode);
	def orderAggregate = null 
	if(responseCode.equals(200)) {
	    orderAggregate = getOrderAggregate.getInputStream().getText() 
	    println(orderAggregate);
	    def jsonSlurper = new JsonSlurper()
	    def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	    assert NEW_ORDER_NAME == orderAggregateJSON.name
	}

	
NEW_ORDER_NAME = "zxcvb"


changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+NEW_ORDER_NAME+"&orderId="+orderId).openConnection();
responseCode = changeOrderCommand.getResponseCode();
println(responseCode);
if(responseCode.equals(200)) {
    orderId = changeOrderCommand.getInputStream().getText() 
    println(orderId);
}

	getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	responseCode = getOrderAggregate.getResponseCode();
	println(responseCode);
	orderAggregate = null 
	if(responseCode.equals(200)) {
	    orderAggregate = getOrderAggregate.getInputStream().getText() 
	    println(orderAggregate);
	    def jsonSlurper = new JsonSlurper()
	    def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	    assert NEW_ORDER_NAME == orderAggregateJSON.name
	}
	
	
	