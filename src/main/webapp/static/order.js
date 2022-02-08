var orderitemList = [];

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/order";
}

function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/order_item";
}

function getAllOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/all_orders";
}

function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/invoice";
}

function getInvoiceDisableUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/invoicedisable";
}
function displayAddModal() {
	$("#add-order-modal").modal('toggle');	
}
function addOrderItemtoList(){
	var $form=$("#orderitem-form");
	var json=toJson($form);
	if(checkOrderItem(json)){
		var check = checkIfPresent(JSON.parse(json).barcode);
		if(check==-1){
			if(parseInt(inventoryMap[JSON.parse(json).barcode])>=parseInt(JSON.parse(json).quantity)) {
				orderitemList.push(JSON.parse(json));
			}
			else{
				toastr.error("Quantity ordered is exceeding inventory. Inventory present is " + inventoryMap[JSON.parse(json).barcode]);
			}
		}
		else{
			var quantity=parseInt(orderitemList[check].quantity)+parseInt(JSON.parse(json).quantity);
			if(parseInt(inventoryMap[JSON.parse(json).barcode])>=quantity){
				orderitemList[check].quantity=quantity;
			}
			else{
				toastr.error("Quantity ordered is exceeding inventory. Inventory present is " + inventoryMap[JSON.parse(json).barcode]);
			}
		}
		
	}
	getOrderItemList();
	$("#orderitem-form").trigger("reset");
}

function getOrderItemList() {

console.log("getorderitemlist");
	displayOrderItem(orderitemList);
}

function displayOrderItem(data){
	var $tbody=$('#orderitem-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		console.log(e);
		var buttonHtml = '<button class="btn btn-danger" onclick="deleteOrderItem(' + i + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + (i+1) + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


function deleteOrderItem(id) {
	orderitemList.splice(id,1);
	getOrderItemList();
}




function checkOrderItem(json) {
	json = JSON.parse(json);
	if(isBlank(json.barcode)) {
		toastr.error("Barcode field must not be empty");
		return false;
	}
	if(isBlank(json.quantity) || isNaN(parseInt(json.quantity)) || !isInt(json.quantity)) {
		toastr.error("Quantity field must not be empty and must be an integer value");
		return false;
	}
	if(barcodeList.indexOf(json.barcode) == -1) {
		toastr.error("Please enter a valid barcode");
		return false;
	}
	if(parseInt(json.quantity)<0) {
		toastr.error("Quantity must be positive");
		return false;
	}
	return true;
}
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}
function isInt(n) {
   return n % 1 === 0;
}
function checkIfPresent(barcode) {
	for(var i in orderitemList) {
		var e = orderitemList[i];
		if(e.barcode.localeCompare(barcode) == 0){
			return i;
		}
	}
	return -1;
}

function addOrder(event){
	if(orderitemList.length == 0) {
		toastr.error("No order items added.");
		return;
	}
	var url=getOrderUrl();
	var json=JSON.stringify(orderitemList);
	
		$.ajax({
			url: url,
			type: 'POST',
			data: json,
			headers: {
				
				'Content-Type': 'application/json'
			},
			success: function(response) {
				$("#add-order-modal").modal('toggle');
				getOrderList(response);
			},
			error: handleAjaxError
		});
		
		return false;

}

function getOrderList(){
	var url=getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		mainOrdersListTable(response);
	   },
	   error: handleAjaxError
	});
}

function mainOrdersListTable(data){
	var $tbody=$('#order-main-table').find('tbody');
	$tbody.empty();
	data.reverse();
	for(var i in data){
		var e = data[i];
		console.log(e.id);
		var buttonHtml1 = '<button style="padding: 0;border: none;background: none;" ><span class="material-icons">keyboard_arrow_down</span></button>';
		var buttonHtml = '<button class="btn btn-primary" onclick="downloadPDF('+e.id+')"><i class="fa fa-download" aria-hidden="true"></i> Download Invoice</button>';
		var row = '<tr class="order-header view" onclick="initializeDropdown(' + e.id + ')" >'
		+ '<td style="text-align:center;">' + buttonHtml1+" "+ e.id + '</td>'
		+ '<td style="text-align:center;">'  + e.datetime + '</td>'
		+ '<td style="text-align:center;">' + buttonHtml + '</td>'
		+ '</tr>';
		orderitemsHtml = '<tr><td colspan="3"><table style ="display:none;" class="table table-striped orderitemrows' + e.id +'"><tbody></tbody></table></tr>';
    $tbody.append(row);
		$tbody.append(orderitemsHtml);
		getOrderItemsHtml(e.id);
	}
}

/*function disablebuttons(id){
	var url= getInvoiceDisableUrl+"/"+id;
	$.ajax({
		
	});
}*/
function downloadPDF(id){
	$(".insideEdit").prop("disabled", true);
	var url = getInvoiceUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
		 xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {
				console.log(blob.size);
      	var link=document.createElement('a');
      	link.href=window.URL.createObjectURL(blob);
      	link.download="Invoice_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});

}
function deleteOrder(id){
	var url=getOrderUrl()+"/"+id;
	$.ajax({
		url:url,
		type:'DELETE',
		 headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				getOrderList();
		 },
		 error: function(response){
				handleAjaxError(response);
		 }
	});
}
function getOrderItemsHtml(id) {
	var url = getOrderUrl() + "/" + id;
	console.log(id);
	$.ajax({
		 url: url,
		 type: 'GET',
		 headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				createOrderItemsHtml(response,id);
		 },
		 error: function(response){
				handleAjaxError(response);
		 }
	});
}
function createOrderItemsHtml(data,id) {

	var table = $('.orderitemrows' + id).find('tbody');
	var thHtml = '<tr>';
	thHtml += '<th scope="col">Barcode</th>';
	thHtml += '<th scope="col">Name</th>';
	thHtml += '<th scope="col">Quantity</th>';
	thHtml += '<th scope="col">MRP</th>';
	thHtml += '<th scope="col">Action</th>';
	thHtml += '</tr>';
	table.append(thHtml);
	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button class="btn btn-danger" onclick="deleteOrderItemFromOrderList(' + e.id + ')">Delete</button>';
		var buttonHtml = '<button class="btn btn-info insideEdit" onclick="displayEditOrderItem(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>';
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
	
		table.append(row);
	}
	console.log(id);
	table.append('<tr><td colspan="3"><button class="btn btn-primary insideEdit" onclick="displayAddOrderItemModal(' + id + ')"><i class="fa fa-plus" aria-hidden="true"></i> Add Item</button></td><td colspan="2"></td></tr>');
}
function deleteOrderItemFromOrderList(id){
	var url=getOrderItemUrl()+"/"+id;
	$.ajax({
		url:url,
		type:'DELETE',
		 headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				getOrderList();
		 },
		 error: function(response){
				handleAjaxError(response);
		 }
	});
}


function initializeDropdown(id) {
	console.log("Orderitems toggle");
	var orderitem_row = '.orderitemrows' + id;
  $(orderitem_row).toggle();
}

function addOrderItemToOldOrder(event){
	var $form=$("#orderitem-add-form");
	var json=toJson($form);
	var orderId=$("#orderitem-add-form input[name=order_id]").val();
	console.log(orderId);
	var url=getOrderItemUrl()+"/"+orderId;
	console.log(url);
	if(checkOrderItem(json)){
		$.ajax({
		url: url,
			type: 'POST',
			data: json,
			headers: {
				
				'Content-Type': 'application/json'
			},
			success: function(response) {
				$("#add-orderitem-modal").modal('toggle');
				getOrderList(response);
			},
			error: handleAjaxError

		});
	}
	return false;
}

function updateOrder(event){
	$('#edit-orderitem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderitem-edit-form input[name=id]").val();
	var orderId = $("#orderitem-edit-form input[name=order-id]").val();
	console.log(orderId);
	var url = getOrderItemUrl() + "/" + id;
	var $form = $("#orderitem-edit-form");
	var json = toJson($form);
	if(checkOrderItem(json)){
		$.ajax({
			url:url,
			type:'PUT',
			data:json,
			headers:{
				
				'Content-Type': 'application/json'
			},
			success: function(response){
				getOrderList(response);
				var orderitem_row='.orderitemros'+orderId;
				$(orderitem_row).show();
				
			},
			error: handleAjaxError
		});
	}
	return false;
}
function displayAddOrderItemModal(order_id) {
	$("#orderitem-add-form input[name=order_id]").val(order_id);
	$('#add-orderitem-modal').modal('toggle');
}
function displayEditOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;
	
	$.ajax({
		url:url,
		type:'GET',
		headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				displayEditOrderItemModal(response);
		 },
		 error: function(response){
				handleAjaxError(response);
		 }

	});
	
}

function displayEditOrderItemModal(data){
	$("#orderitem-edit-form input[name=barcode]").val(data.barcode);
	$("#orderitem-edit-form input[name=quantity]").val(data.quantity);
	$("#orderitem-edit-form input[name=id]").val(data.id);
	$("#orderitem-edit-form input[name=order-id]").val(data.orderId);
	$('#edit-orderitem-modal').modal('toggle');
}
function init(){
$("#open-add-modal").click(displayAddModal);	
$('#add-orderitem').click(addOrderItemtoList);
$('#add-order').click(addOrder);
$("#add-orderitem-previousorders").click(addOrderItemToOldOrder);
$('#update-orderitem').click(updateOrder);
}


$(document).ready(init);
$(document).ready(getOrderItemList);
$(document).ready(getOrderList);