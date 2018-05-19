import groovy.json.*
import groovyx.net.http.*
import groovyx.net.http.ContentType.*
import groovyx.net.http.Method.*

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
	def baseUrl = new URL('http://localhost:8080/createOrderCommand')
	def queryString = "name="+orderName
	def connection = baseUrl.openConnection()
	connection.with {
	  doOutput = true
	  requestMethod = 'POST'
	  outputStream.withWriter { writer ->
	    writer << queryString
	  }
	  orderId =  content.text
	}
	assert(orderId)
	println "Testing create order command complete:" +orderId
	orderId
	
}

def changeOrderName (newOrderName,orderId){
	println "Changing order name to :"+newOrderName
	
	def baseUrl = new URL('http://localhost:8080/changeOrderNameCommand')
	def queryString = "newName="+newOrderName+"&orderId="+orderId
	println "queryString:"+queryString
	def connection = baseUrl.openConnection()
	connection.with {
	  doOutput = true
	  requestMethod = 'POST'
	  outputStream.withWriter { writer ->
	    writer << queryString
	  }
	  orderId =  content.text
	}
	assert(orderId)
	println "Testing change order command complete:" +orderId
	orderId
}

def cancelOrder (orderId){
	println "Canceling order :"+orderId
	
	def baseUrl = new URL('http://localhost:8080/cancelOrderCommand')
	def queryString = "orderId="+orderId
	println "queryString:"+queryString
	def connection = baseUrl.openConnection()
	connection.with {
	  doOutput = true
	  requestMethod = 'POST'
	  outputStream.withWriter { writer ->
	    writer << queryString
	  }
	  orderId =  content.text
	}
	assert(orderId)
	orderId

}

def uncancelOrder (orderId){
	println "Uncanceling order :"+orderId
	
	def baseUrl = new URL('http://localhost:8080/uncancelOrderCommand')
	def queryString = "orderId="+orderId
	println "queryString:"+queryString
	def connection = baseUrl.openConnection()
	connection.with {
	  doOutput = true
	  requestMethod = 'POST'
	  outputStream.withWriter { writer ->
	    writer << queryString
	  }
	  orderId =  content.text
	}
	assert(orderId)
	orderId

}


def verifyOrderAggregate (orderName,orderId,jsonSlurper, orderStatus){
	println "Verifying OrderAggregate orderName->"+orderName+" orderId->"+orderId
	def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	def responseCode = getOrderAggregate.getResponseCode();
	assert (responseCode == 200)
	def orderAggregate = getOrderAggregate.getInputStream().getText() 
	println "Verifying OrderAggregate orderAggregate->"+orderAggregate
	
	assert (orderAggregate)
	def orderAggregateJSON = jsonSlurper.parseText(orderAggregate)
	assert orderName == orderAggregateJSON.name
	assert orderStatus == orderAggregateJSON.status
	println "OrderAggregate verified:" +orderAggregate
	orderAggregate
}

def createOrderSnapshot (orderId){
	println "Creating Snapshot"
	
	def baseUrl = new URL('http://localhost:8080/orderSnapshot')
	def queryString = "orderId="+orderId
	println "queryString:"+queryString
	def connection = baseUrl.openConnection()
	connection.with {
	  doOutput = true
	  requestMethod = 'POST'
	  outputStream.withWriter { writer ->
	    writer << queryString
	  }
	  orderId =  content.text
	}
	assert(orderId)
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
	