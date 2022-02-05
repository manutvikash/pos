function getOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function addOrderItem(event) {
	console.log("addorderitem");
	var barcodeLength = $.trim($('#orderitem-form input[name=barcode]').val()).length;
	var quantityLength = $.trim($('#orderitem-form input[name=quantity]').val()).length;

	if (barcodeLength == 0 || quantityLength == 0) {
		toastr.error('Please enter the values correctly');
		return false;
	}

	var quantity = Number($('#orderitem-form input[name=quantity]').val());
	if (quantity < 0 || isNaN(quantity) || !Number.isInteger(quantity)) {
		toastr.error('Enter valid quantity');
		return;
	}

	getOrderItemDetails();
}


function getOrderItemDetails() {
	// barcode pass as param
	var barcode = $('#orderitem-form input[name=barcode]').val();
	var url = getOrderUrl() + '/' + barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(orderData) {
			checkForQuantity(orderData);
		},
		error: handleAjaxError
	});
}

function checkForQuantity(orderData) {
	var requiredQuantity = checkPreviouslyBarcode(orderData);
	if (requiredQuantity > orderData.quantity) {
		toastr.error('Entered Quantity : ' + requiredQuantity + ' but existing quantity: ' + orderData.quantity);
		return false;
	}
	orderData.quantity = requiredQuantity;

	addToDisplayTable(orderData);
}

var orderItemsTable = {};

function checkPreviouslyBarcode(orderData) {
	var requiredQuantity = Number($('#orderitem-form input[name=quantity]').val());
	if (requiredQuantity < 0 || isNaN(requiredQuantity) || !Number.isInteger(requiredQuantity)) {
		toastr.error('Enter valid quantity');
		return;
	}
	if (orderItemsTable.hasOwnProperty(orderData.barcode)) {
		toastr.error('Barcode already exists,update the quantity');

		//if(orderItemsTable[orderData.barcode].quantity+requiredQuantity<orderData.quantity){
		requiredQuantity += orderItemsTable[orderData.barcode].quantity;
		//}
	}
	return requiredQuantity;
}
var total=[];

function showTable() {

	var $tbody = $('#orderitem-table').find('tbody');
	$tbody.empty();
	let serialNo = 1;
	var totalAmount = 0;
	for (let key in orderItemsTable) {
		orderData = orderItemsTable[key];
		console.log(orderData.barcode);
		//orderData.barcode.trim();
		var buttonHtml = '<button class="btn btn-info" onclick="editOrderItem(\'' + orderData.barcode + '\')">Edit</button>';
		buttonHtml += '<button class="btn btn-danger" style="margin-left:8%;" onclick="deleteOrder(\'' + orderData.barcode + '\') ">Delete</button>';
		var tablerow = '<tr>'
			+ '<td>' + serialNo+ '</td>'
			+ '<td>' + orderData.barcode + '</td>'
			+ '<td>'  + orderData.productName + '</td>'
			+ '<td>' + orderData.quantity + '</td>'
			+ '<td  class="price-value"> ' + orderData.mrp + '</td>'
		    + '<td  class="price-value">' + orderData.mrp*orderData.quantity + '</td>'
			+ '<td>' + buttonHtml + '<td>'
			+ '</tr>';
		$tbody.append(tablerow);
		totalAmount += orderData.mrp * orderData.quantity;
		serialNo++;
	}
	 $('#total').val(totalAmount);
}

function addToDisplayTable(orderData) {
	//console.log(orderData);// quantity should change
	orderItemsTable[orderData["barcode"]] = orderData;
	showTable();
	$('#orderinput-form input[name=barcode]').val("");
	$('#orderinput-form input[name=quantity]').val("");
}

function deleteOrder(barcode) {
	//    console.log(barcode);
	delete orderItemsTable[barcode];

	showTable();
}
function editOrderItem(barcode){
    
    $('#orderitem-edit-form input[name=barcode]').val(barcode);
    $('#orderitem-edit-form input[name=productName]').val(orderItemsTable[barcode].productName);
    $('#orderitem-edit-form input[name=mrp]').val(orderItemsTable[barcode].mrp);
    $('#orderitem-edit-form input[name=quantity]').val(orderItemsTable[barcode].quantity)
    $('#edit-orderitem-modal').modal('toggle');
}

function updateOrderItem(){
	 var barcode=$('#orderitem-edit-form input[name=barcode]').val();
    var url=getOrderUrl()+'/'+barcode;
    $.ajax({
        url:url,
        type:'GET',
        success:function(orderData){
            editQuantity(orderData);

        },
        error:handleAjaxError
    });
}
function editQuantity(orderData){
	var enteredQuantity=Number($('#orderitem-edit-form input[name=quantity]').val());
	var availableQuantity=orderData.quantity;
	if(enteredQuantity<=0||isNaN(enteredQuantity)||!Number.isInteger(enteredQuantity)){
		toastr.error("Enter valid quantity");
		return;
	}
	if(enteredQuantity>availableQuantity){
		toastr.error('Available Quantity: '+availableQuantity+'Please Enter less then this value');
	}
	orderData.quantity=enteredQuantity;
console.log(orderData.quantity);

        var updatedQuantity=$('#orderitem-edit-form input[name=quantity]').val();
        var barcode=$('#orderitem-edit-form input[name=barcode]').val();
        orderItemsTable[barcode].quantity=updatedQuantity;

        //quantity.html(updatedQuantity);
        showTable();
        $('#edit-orderitem-modal').modal('toggle');
}

function resetEverything(){
    orderItemsTable={};
    $("#orderitem-form").trigger("reset");
    var $tbody=$('#orderitem-table').find('tbody');
    $tbody.empty();
    $('#total').val("");
    total=0;
   // allowAllfields();
}
function createOrder(){
    if(Object.keys(orderItemsTable).length ==0 ){
        toastr.error("Please enter the order items");
        return false;
    }
    var form=[];

    for(let barcode in orderItemsTable){
        var orderItem=orderItemsTable[barcode];
        var item={};
        item["quantity"]=orderItem.quantity;
        item["barcode"]=orderItem.barcode;
        form.push(item);
    }
    var json=JSON.stringify(form);
    var url=getOrderUrl();
    $.ajax({
        url:url,
        type:'POST',
        data:json,
        headers:{
            'Content-Type':'application/json'
        },
        success:function(response){
            toastr.success("Order Placed successfully");
            orderTable();
            resetEverything();
        },
        error:handleAjaxError
    })
}

function orderTable(){
	var url=getOrderUrl();
	$.ajax({
		url:url,
		type:'GET',
		success:function(data){
			console.log("ordertable")
			displayMainTable(data);
		},
		error:handleAjaxError
	});
}

function displayMainTable(data){
	
	var $tbody=$('#order-table').find('tbody');
	console.log("main table");
	$tbody.empty();
	var number=1;
    var orderNo=0;
	for(var i in data){
		order=data[i];
		if(orderNo!=order.orderId){
			orderNo=order.orderId
        var buttonHtml = '<button class="btn btn-info" onclick="Invoice(\'' + data.barcode + '\')">Invoice</button>';
		var tablerow = '<tr>'
			+ '<td>' + order.orderId+ '</td>'
			+ '<td>' + order.date+ '</td>'
			+ '<td>' + buttonHtml + '<td>'
			+ '</tr>';
		$tbody.append(tablerow);
		number++;
		}
		
		//orderData.barcode.trim();
	}
}
function init() {
	 $('#add-order-item').click(addOrderItem);
	 $('#update-orderitem').click(updateOrderItem);
	 $('#create-new-order').click(resetEverything);
     $('#create-order').click(createOrder);
}

$(document).ready(init);
$(document).ready(orderTable);