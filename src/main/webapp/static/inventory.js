function inventoryUrl(){
	var baseUrl=$("meta[name=baseUrl]").attr("content");
	return baseUrl+"/api/inventory";
}


function addInventory(event){
	var $form=$("#inventory-form");
	var json=toJson($form);
	if(validateInventory(json)){
			var url=inventoryUrl();
			$.ajax({
				url: url,
			type: 'POST',
			data: json,
			headers: {
				'Content-Type': 'application/json'
			},
			success: function(response) {
				getInventoryList(response);
				$('#add-inventory-modal').modal('toggle');
			},
			error: handleAjaxError
			});
	}

	
}
function validateInventory(json){
	json=JSON.parse(json);
	if(isBlank(json.barcode)){
		toastr.error("Barcode cannot be empty");
		return false;
	}
	if(isBlank(json.quantity)||isNaN(parseInt(json.quantity))|| !isInt(json.quantity)){
		toastr.error("Quantity must be a positive integer value.");
		return false;
	}
	return true;
}

function isInt(n){
	return n % 1 === 0;
}

function isBlank(str){
	return (!str || /^\s*$/.test(str));
}


function getInventoryList(){
	var url=inventoryUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError

		
	})
}


//Display table
function displayInventoryList(data){
	console.log(data);
	data=data.sort(function(a,b){
		if(a.name===b.name){
			return 0;
		}
		if(a.name<b.name){
			return -1;
		}else{
			return 1;
		}
	});
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-info" style="border: none;" onclick="displayEditInventory(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
     }
}
function displayEditInventory(id){
	var url = inventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: handleAjaxError
	});	
}
function displayInventory(data){
	$("#inventory-edit-form input[name=id]").val(data.id);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);	
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$('#edit-inventory-modal').modal('toggle');
	
}

//Edit
function editInventory(){
	var $form=$("#inventory-edit-form");
	var json=toJson($form);
	var id=$("#inventory-edit-form input[name=id]").val();
	var url=inventoryUrl()+"/"+id;
	if(validateInventory(json)){
		$.ajax({
			   url: url,
	           type: 'PUT',
	           data: json,
	           headers: {
                	'Content-Type': 'application/json'
               },	   
	           success: function(response) {
		toastr.success("Inventory updated successfully");
	   		         getInventoryList(response);
                     $('#edit-inventory-modal').modal('toggle');
	           },
	           error: handleAjaxError
		});
	}
	return false;
}

//Upload
var fileData = [];
var errorData = [];
var processCount = 0;

function displayUploadInventory(){
	resetUploadDialog();
	 $("#download-errors-inventory").prop("disabled",true);	
	$('#upload-inventory-modal').modal('toggle');
}
function resetUploadDialog(){
	var $file=$('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	var correct=parseInt(fileData.length)-parseInt(errorData.length);
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + correct);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var path=$file.val();
	var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#inventoryFileName').html(fileName);
}

//Processing file data
function processData(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
	console.log("processdata");
	/*checkHeader(file,["barcode","quantity"],readFileDataCallback);*/
}



function readFileDataCallback(results){
	fileData = results.data;
	console.log("readfileDataCallBack");
	uploadRows();
}

function uploadRows(){
	console.log("uploadRows");
	updateUploadDialog();
	$("#download-errors-inventory").prop("disabled",false);
	if(processCount==fileData.length){
		//toastr.success("Uploaded Inventory Successfully");
		//getInventoryList();
		return;
	}
	
	var row=fileData[processCount];
	processCount++;
	/*var id=$("#inventory-edit-form input[name=id]").val();*/
	var json=JSON.stringify(row);
	console.log(json);

    if(!isInt(JSON.parse(json).quantity)){
	       row.error="Quantity must be integer";
	   		errorData.push(row);
	   		uploadRows();
        return;
	
     }

	var url=inventoryUrl()+"/barcode";
//	var url=inventoryUrl()+'/'+id;
	console.log(url);
		$.ajax({
	   url: url,
	   type: 'POST',//1
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		console.log("success");
		console.log(response);
		//return;
		response=response[0];
		response.quantity=JSON.parse(json).quantity;
		response.barcode=JSON.parse(json).barcode;
		//console.log(response.barcode);
		
		console.log(response.id);
		updateInventory(response.id,JSON.stringify(response),row);
	   		//updateInventory()
            uploadRows(); 
        
	   },
	  error: function(response){
	   		row.error=JSON.parse(response.responseText).message;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function updateInventory(id,json,row){
	console.log("updateinventory");
	console.log(id);
	//console.log("updating inventory");
	var url=inventoryUrl()+"/"+id;
	console.log(url);
	$.ajax({
		url:url,
		type:'PUT',
		data:json,
		headers:{
			'Content-Type':'application/json'
		},
		success:function(response){
			//console.log("success");
			getInventoryList();
			//$('#upload-inventory-modal').modal('toggle');
		},
		error:function(response){
			row.error=JSON.parse(response.responseText).message
	   		errorData.push(row);
	   		updateUploadDialog();
		}
	});
return false; 
}
//Errors in file
function downloadErrors(){
	writeFileData(errorData);
}
function inventorySearch(event){

	//Set the values to update
	var $form = $("#inventory-form");
	console.log($form);
	var json = toJson($form);
	console.log(json);
	var url = inventoryUrl() + '/barcode';

	var json2 = {barcode: JSON.parse(json).barcode, name: "", quantity: 0};
   console.log(json2);
    if(json2.barcode === "")
    {
        getInventoryList();

        return;
    }

	json = JSON.stringify(json2);
console.log(json);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(data) {
console.log(data);
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});

	return false;
}

/*function inventoryFilter() {

    var value = document.getElementById("inventory-filter").value;
    value = value.trim();
    value = value.toLowerCase();

    console.log(value);

    if(value === '')
    {
        getInventoryList();

        return;
    }

    $("#inventory-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
    });
}*/
/*function getReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/report";
}
function getInventoryReport(){
	var url = getReportUrl() + "/inventory";
	console.log(url);
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
      	link.download="InventoryReport_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}*/

function downloadInventoryReport(){

	var url = inventoryUrl();
	console.log(url);
	// call api
	$.ajax({
		url: url,
		type: 'GET',
	
		success: function(response) {

			var config = {
            		quoteChar: '',
            		escapeChar: '',
            		delimiter: "\t"
            	};

            	var data = Papa.unparse(response, config);
                var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
                var fileUrl =  null;
                var currentdate = new Date();
                var inventoryreportname = "inventory-report_"+ currentdate.getDate() + "/"
                                 	+ (currentdate.getMonth()+1)  + "/"
                                 	+ currentdate.getFullYear() + "@"
                                 	+ currentdate.getHours() + "h_"
                                 	+ currentdate.getMinutes() + "m_"
                                 	+ currentdate.getSeconds()+"s.tsv";
                if (navigator.msSaveBlob) {
                    fileUrl = navigator.msSaveBlob(blob, inventoryreportname);
                } else {
                    fileUrl = window.URL.createObjectURL(blob);
                }
                var tempLink = document.createElement('a');
                tempLink.href = fileUrl;
                tempLink.setAttribute('download', inventoryreportname);
                tempLink.click();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}
function init(){
	$('#add-inventory').click(addInventory);
	$('#edit-inventory').click(editInventory);
	$('#upload-inventory').click(displayUploadInventory);
	$('#inventoryFile').on('change', updateFileName);
	$('#process-inventory').click(processData);
		$('#download-errors-inventory').click(downloadErrors);
	$('#search-inventory').click(inventorySearch);
	$('#inventory-report').click(downloadInventoryReport);
}

$(document).ready(init);
$(document).ready(getInventoryList);