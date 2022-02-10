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


function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl+"/api/report/sales";
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
	if(parseInt(json.quantity)<=0) {
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
		console.log(e);
		var buttonHtml = '<button class="btn btn-info" id="insideEdit" onclick="initializeDropdown('+e.id+')" >View & Edit</button>';
		 buttonHtml += '<button class="btn btn-primary" onclick="downloadPDF('+e.id+')" style="margin-left:3%;"><i class="fa fa-download" aria-hidden="true"></i> Generate Invoice</button>';
		var row = '<tr class="order-header view">'
		+ '<td style="text-align:center;">' + e.id + '</td>'
		+ '<td style="text-align:center;">'  + e.datetime + '</td>'
			//+ '<td style="text-align:center;">'  + total + '</td>'
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
	$("#insideEdit").prop("disabled", true);
	var url = getInvoiceUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
		 xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {
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
	thHtml += '<th scope="col">Unit Price</th>';
	thHtml += '<th scope="col">Cost</th>';
	thHtml += '<th scope="col">Action</th>';
	thHtml += '</tr>';
	table.append(thHtml);
	var total=0;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-info" onclick="displayEditOrderItem(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>';
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + e.mrp*e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
	
	total+=e.mrp*e.quantity;
		table.append(row);
	}
	console.log(id);
	table.append('<tr><td colspan="3" style="font-weight:700;padding-left:3%; font-size:22px"> Total: '+total+'</td><td colspan="3"><button class="btn btn-info insideEdit" style="margin-left:52%;" onclick="displayAddOrderItemModal(' + id + ')"><i class="fa fa-plus" aria-hidden="true"></i> Add Item</button></td></tr>');
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

function checkOrderItem1(json){
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

function updateOrder(event){
	$('#edit-orderitem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderitem-edit-form input[name=id]").val();
	var orderId = $("#orderitem-edit-form input[name=order-id]").val();
	console.log(orderId);
	var url = getOrderItemUrl() + "/" + id;
	var $form = $("#orderitem-edit-form");
	var json = toJson($form);
	if(checkOrderItem1(json)){
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


function getSalesReport(){
	var $form=$("#sales-report-form");
	var json=toJson($form);
	var url=getSalesReportUrl();
	console.log(url);
	if(checkSalesForm(json)){
		$.ajax({
			url:url,
			type:'POST',
			data:json,
			headers:{
				'Content-Type':'application/json'
			},
			xhrFields:{
				responseType:'blob'
			},
			success:function(blob) {
					$("#sales-report-form").trigger("reset");
					console.log(blob.size);
	      	var link=document.createElement('a');
	      	link.href=window.URL.createObjectURL(blob);
	      	link.download="SalesReport_" + new Date() + ".pdf";
	      	link.click();

		   },
		   error: function(response){
		   		toastr.error("No sales data was found within given date range and brand category pair");
		   }
		});
	}
}

function defaultDates(){
	document.getElementById("inputStartDate").defaultValue="2022-02-01";
	document.getElementById("inputEndDate").defaultValue="2022-02-20";
	
}

function endDateCheck() {
	var startDate = document.getElementById("inputStartDate").value;
	var endDate = document.getElementById("inputEndDate").value;

	if ((Date.parse(startDate) > Date.parse(endDate))) {
			toastr.error("End date should be greater than Start date");
			document.getElementById("inputEndDate").value = "2022-02-20";
	}
}
function startDateCheck() {
	var startDate = document.getElementById("inputStartDate").value;
	var endDate = document.getElementById("inputEndDate").value;

	if ((Date.parse(startDate) > Date.parse(endDate))) {
			toastr.error("Start date should be lesser than End date");
			document.getElementById("inputStartDate").value = "2022-02-01";
	}
}


function checkSalesForm(data){
	json=JSON.parse(data);
	if(!brandList.includes(json.brand)&&!isBlank(json.brand)){
		toastr.error("Please enter valid brand");
		return false;
	}
	if(!categoryList.includes(json.category)&&!isBlank(json.category)){
		toastr.error("Please enter valid category");
	}
	return true;
}
function init(){
$("#open-add-modal").click(displayAddModal);	
$('#add-orderitem').click(addOrderItemtoList);
$('#add-order').click(addOrder);
$("#add-orderitem-previousorders").click(addOrderItemToOldOrder);
$('#update-orderitem').click(updateOrder);
$("#sales-report").click(getSalesReport);
$("#inputEndDate").change(endDateCheck);
$("#inputStartDate").change(startDateCheck);
}


$(document).ready(init);
$(document).ready(getOrderItemList);
$(document).ready(getOrderList);
$(document).ready(defaultDates);