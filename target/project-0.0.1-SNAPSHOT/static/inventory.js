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
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-primary" style="border: none;" onclick="displayEditInventory(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
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
	/*readFileData(file, readFileDataCallback);*/
	checkHeader(file,["barcode","quantity"],readFileDataCallback);
}

function checkHeader(file,header_list,callback) {
	var allHeadersPresent = true;
	Papa.parse(file,{
		delimiter: "\t",
		step: function(results, parser) {

        for(var i=0; i<header_list.length; i++){
					if(!results.data.includes(header_list[i])){
						allHeadersPresent = false;
						break;
					}
				}

        parser.abort();
        results=null;
        delete results;

    }, complete: function(results){

        results=null;
        delete results;
		if(allHeadersPresent) {
			readFileData(file,callback);
		}
		else{
			alert("Improper or absent headers in file");
     	}

    }
	});
}


function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
	$("#download-errors-inventory").prop("disabled",false);
	if(processCount==fileData.length){
		getInventoryList();
		return;
	}
	
	var row=fileData[processCount];
	processCount++;
	
	var json=JSON.stringify(row);
	var url=inventoryUrl();
	
		$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows(); 
        /*$('#upload-brand-modal').modal('toggle');*/
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

//Errors in file
function downloadErrors(){
	writeFileData(errorData);
}


function inventoryFilter() {

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
}
function init(){
	$('#add-inventory').click(addInventory);
	$('#edit-inventory').click(editInventory);
	$('#upload-inventory').click(displayUploadInventory);
	$('#inventoryFile').on('change', updateFileName);
	$('#process-inventory').click(processData);
		$('#download-errors-inventory').click(downloadErrors);
	$('#search-inventory').click(inventoryFilter);
}

$(document).ready(init);
$(document).ready(getInventoryList);