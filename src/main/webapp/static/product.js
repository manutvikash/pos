function productUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/product";
}

function addProduct(event) {

	var $form = $("#product-form");
	var json = toJson($form);
	if (validateProduct(json)) {
		var url = productUrl();
		$.ajax({
			url: url,
			type: 'POST',
			data: json,
			headers: {
				'Content-Type': 'application/json'
			},
			success: function(response) {
				toastr.success("Product Created successfully");
				getProductList(response);
				$('#add-product-modal').modal('toggle');
			},
			error: handleAjaxError
		});
	}
return false;
}

function validateProduct(json) {
	json = JSON.parse(json);
	if (isBlank(json.barcode)) {
		toastr.error("Barcode must not be empty");
		return false;
	}
	if (isBlank(json.brand)) {
		toastr.error("Brand must not be empty");
		return false;
	}
	if (isBlank(json.category)) {
		toastr.error("Category must not be empty");
		return false;
	}
	if (isBlank(json.brand)) {
		toastr.error("Brand must not be empty");
		return false;
	}
	if (isBlank(json.name)) {
		toastr.error("Name must not be empty");
		return false;
	}
	if (isBlank(json.mrp) || isNaN(parseFloat(json.mrp))) {
		toastr.error("MRP must not be empty and must be a float value");
		return false;
	}
	return true;
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function getProductList(){
	var url = productUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
              /*if(!page){
	          pagination();
               } */ 
	   },
	   error: handleAjaxError
	});
}

//UI
function displayProductList(data){
	data=data.sort(function(a,b){
		if(a.brand===b.brand){
			if(a.category<b.category){
				return -1;
			}else{
				return 1;
			}
		}else{
		if(a.brand<b.brand){
			return -1;
		}else{
			return 1;
		}
		}
	});
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-primary" style="border: none;" onclick="displayEditProduct(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + parseFloat(e.mrp).toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	
}
function displayEditProduct(id){
	var url = productUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);   
	   },
	   error: handleAjaxError
	});	
}


function displayProduct(data){
	$("#product-edit-form input[name=id]").val(data.id);
	$("#product-edit-form input[name=brand]").val(data.brand);	
	$("#product-edit-form input[name=category]").val(data.category);	
	$("#product-edit-form input[name=name]").val(data.name);
    $("#product-edit-form input[name=mrp]").val(data.mrp);	
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$('#edit-product-modal').modal('toggle');
	
}


function editProduct(event){
	var id=$("#product-edit-form input[name=id]").val();
	var url = productUrl() + "/" + id;
	var $form = $("#product-edit-form");
	var json = toJson($form);

    if(validateProduct(json)){

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		toastr.success("Product Updated successfully");
	   		getProductList(response);
          $('#edit-product-modal').modal('toggle');
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


function processData(){
	var file = $('#productFile')[0].files[0];
	/*readFileData(file, readFileDataCallback);*/
	checkHeader(file,["brand","category","barcode","name","mrp"],readFileDataCallback);
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
					toastr.error("Wrong file. Please check the head of it");
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
	$("#download-errors-product").prop("disabled",false);
	if(processCount==fileData.length){
		getProductList();
		return;
	}
	
	var row=fileData[processCount];
	processCount++;
	
	var json=JSON.stringify(row);
	var url=productUrl();
	
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
	   		row.error=JSON.parse(response.responseText).message
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

//Errors in file
function downloadErrors(){
	writeFileData(errorData);
}

function displayUploadData(){
 	resetUploadDialog(); 
    $("#download-errors-product").prop("disabled",true);	
	$('#upload-product-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
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
	var $file = $('#productFile');
	var path=$file.val();
	var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#productFileName').html(fileName);
}


function productFilter() {

    var value = document.getElementById("product-filter").value;
    value = value.trim();
    value = value.toLowerCase();

    console.log(value);

    if(value === '')
    {
        getProductList();

        return;
    }

    $("#product-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
    });
}


function init(){
	$('#add-product').click(addProduct);
	$('#edit-product').click(editProduct);
	$('#search-product').click(productFilter);
	$('#upload-data-product').click(displayUploadData);
	$('#download-errors-product').click(downloadErrors);
	$('#process-data-product').click(processData);
	$('#productFile').on('change', updateFileName);
}


$(document).ready(init);
$(document).ready(getProductList);

