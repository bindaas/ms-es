import groovy.json.*

// TEST Create Order

def ORDER_NAME = "qwerty"
def responseCode = null 
def jsonSlurper = new JsonSlurper()

println "Testing create order command"
def orderId = createOrderCommand (ORDER_NAME)
println "Testing create order command complete:" +orderId

def NEW_ORDER_NAME1 = "asdfg"
println "Changing order name to :"+NEW_ORDER_NAME1
changeOrderName (NEW_ORDER_NAME1,orderId)


println "Verifying OrderAggregate"
def orderAggregate = verifyOrderAggregate (NEW_ORDER_NAME1,orderId,jsonSlurper)
println "OrderAggregate verified:" +orderAggregate

	
def NEW_ORDER_NAME2 = "zxcvb"
println "Changing order name to :"+NEW_ORDER_NAME2
changeOrderName (NEW_ORDER_NAME2,orderId)

println "Verifying OrderAggregate"
orderAggregate = verifyOrderAggregate (NEW_ORDER_NAME2,orderId,jsonSlurper)
println "OrderAggregate verified:" +orderAggregate

println "Creating Snapshot"
createOrderSnapshot (orderId)
println "Creating Snapshot over"

println ("testing snapshots")
orderAggregate = testSnapshot (orderId)
println ("testing snapshots:"+orderAggregate)



//*******************************************************************************//

def createOrderCommand (orderName){
	def createOrderCommand = new URL("http://localhost:8080/createOrderCommand?name="+orderName).openConnection();
	def responseCode = createOrderCommand.getResponseCode();
	assert (responseCode == 200)
	def orderId = createOrderCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}

def changeOrderName (newOrderName,orderId){
	def changeOrderCommand = new URL("http://localhost:8080/changeOrderNameCommand?newName="+newOrderName+"&orderId="+orderId).openConnection();
	def responseCode = changeOrderCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = changeOrderCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}

def verifyOrderAggregate (orderName,orderId,jsonSlurper){
	def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	def responseCode = getOrderAggregate.getResponseCode();
	assert (responseCode == 200)
	def orderAggregate = getOrderAggregate.getInputStream().getText() 
	assert (orderAggregate)
	def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	assert orderName == orderAggregateJSON.name
	orderAggregate
}

def createOrderSnapshot (orderId){
	def snapshotCommand = new URL("http://localhost:8080/orderSnapshot?orderId="+orderId).openConnection();
	def responseCode = snapshotCommand.getResponseCode();
	assert (responseCode == 200)
	orderId = snapshotCommand.getInputStream().getText() 
	assert(orderId)
	orderId
}

def testSnapshot (orderId){
	def getOrderAggregateSnapshot = new URL("http://localhost:8080/orderAggregateSnapshot?orderId="+orderId).openConnection();
	def responseCode = getOrderAggregateSnapshot.getResponseCode();
	assert (responseCode == 200)
	def orderAggregate = getOrderAggregateSnapshot.getInputStream().getText() 
	assert (orderAggregate)
	orderAggregate
}
	