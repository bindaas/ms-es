import groovy.json.*

// TEST Create Order

def responseCode = null 
def jsonSlurper = new JsonSlurper()

def orderId = createOrderCommand ("qwerty")
verifyOrderAggregate ("qwerty",orderId,jsonSlurper,"CREATED")

changeOrderName ("asdfg",orderId)
verifyOrderAggregate ("asdfg",orderId,jsonSlurper,"CREATED")

changeOrderName ("zxcvb",orderId)
verifyOrderAggregate ("zxcvb",orderId,jsonSlurper,"CREATED")

createOrderSnapshot (orderId)
verifyOrderAggregateSnapshot ("zxcvb",orderId,jsonSlurper,"CREATED")

changeOrderName ("qaz",orderId)
verifyOrderAggregate ("qaz",orderId,jsonSlurper,"CREATED")
verifyOrderAggregateSnapshot ("qaz",orderId,jsonSlurper,"CREATED")

cancelOrder (orderId)
verifyOrderAggregate ("qaz",orderId,jsonSlurper,"CANCELED")

uncancelOrder (orderId)
verifyOrderAggregate ("qaz",orderId,jsonSlurper,"CREATED")

cancelOrder (orderId)
verifyOrderAggregate ("qaz",orderId,jsonSlurper,"CANCELED")



//*******************************************************************************//

def createOrderCommand (orderName){
	println "Testing create order command"
	def createOrderCommand = new URL("http://localhost:8080/createOrderCommand?name="+orderName).openConnection();
	def responseCode = createOrderCommand.getResponseCode();
	assert (responseCode == 200)
	def orderId = createOrderCommand.getInputStream().getText() 
	assert(orderId)
	println "Testing create order command complete:" +orderId
	orderId
}

def changeOrderName (newOrderName,orderId){
	println "Changing order name to :"+newOrderName
	def changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+newOrderName+"&orderId="+orderId).openConnection();
	def responseCode = changeOrderCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = changeOrderCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}

def cancelOrder (orderId){
	println "Canceling order :"+orderId
	def cancelOrderCommand = new URL("http://localhost:8080/cancelOrderCommand?orderId="+orderId).openConnection();
	def responseCode = cancelOrderCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = cancelOrderCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}

def uncancelOrder (orderId){
	println "Uncanceling order :"+orderId
	def uncancelOrderCommand = new URL("http://localhost:8080/uncancelOrderCommand?orderId="+orderId).openConnection();
	def responseCode = uncancelOrderCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = uncancelOrderCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}


def verifyOrderAggregate (orderName,orderId,jsonSlurper, orderStatus){
	println "Verifying OrderAggregate"
	def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	def responseCode = getOrderAggregate.getResponseCode();
	assert (responseCode == 200)
	def orderAggregate = getOrderAggregate.getInputStream().getText() 
	assert (orderAggregate)
	def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	assert orderName == orderAggregateJSON.name
	assert orderStatus == orderAggregateJSON.status
	println "OrderAggregate verified:" +orderAggregate
	orderAggregate
}

def createOrderSnapshot (orderId){
	println "Creating Snapshot"
	def snapshotCommand = new URL("http://localhost:8080/orderSnapshot?orderId="+orderId).openConnection();
	def responseCode = snapshotCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = snapshotCommand.getInputStream().getText() 
	assert(orderId)
	println "Creating Snapshot over"
	orderId
}

def verifyOrderAggregateSnapshot (orderName,orderId,jsonSlurper, orderStatus){
	println ("testing snapshots")
	def getOrderAggregateSnapshot = new URL("http://localhost:8080/orderAggregateSnapshot?orderId="+orderId).openConnection();
	def responseCode = getOrderAggregateSnapshot.getResponseCode();
	assert (responseCode == 200)
	def orderAggregate = getOrderAggregateSnapshot.getInputStream().getText() 
	assert (orderAggregate)
	def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	assert orderName == orderAggregateJSON.name
	assert orderStatus == orderAggregateJSON.status
	println ("testing snapshots:"+orderAggregate)
	orderAggregate
}
	