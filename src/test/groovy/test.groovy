// GET
def get = new URL("http://localhost:8080/createOrderCommand").openConnection();
def getRC = get.getResponseCode();
println(getRC);
def orderId = null 
if(getRC.equals(200)) {
    orderId = get.getInputStream().getText() 
    println(orderId);
}
if (orderId){

	def getOrderAggregate = new URL("http://localhost:8080/orderAggregate?orderId="+orderId).openConnection();
	def geOrderAggregatetRC = getOrderAggregate.getResponseCode();
	println(geOrderAggregatetRC);
	def orderAggregate = null 
	if(geOrderAggregatetRC.equals(200)) {
	    orderAggregate = getOrderAggregate.getInputStream().getText() 
	    println(orderAggregate);
	}

}