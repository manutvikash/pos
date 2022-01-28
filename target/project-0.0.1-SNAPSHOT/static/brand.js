function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}
//Button actions
function addBrand(event){
	//Set the values to update

	var $form = $("#brand-form");
	var json = toJson($form);
	var check = validateBrand(json);
	if(check) {
		var url = getBrandUrl();
		ajaxQuery(url,'POST',json,function(response) {
			getBrandList(response);
			$('#add-brand-modal').modal('toggle');
		},handleAjaxError);
	}
	return false;
}
function updateBrand(event){

	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;


	var $form = $("#brand-edit-form");
	var json = toJson($form);

	var check = validateBrand(json);
	if(check) {
		ajaxQuery(url,'PUT',json,function(response) {
			getBrandList(response);
			$('#edit-brand-modal').modal('toggle');
		},handleAjaxError);
	}

	return false;

}


function getBrandList(){
	var url = getBrandUrl();
	ajaxQuery(url,'GET','',displayBrandList,handleAjaxError);
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getBrandList,handleAjaxError);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	console.log('Printing Brand data');
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="deleteBrand(' + e.id + ')"><span class="material-icons" style="color:red">delete</span></button>'
		buttonHtml += ' <button style="padding: 0;border: none;background: none;" onclick="displayEditBrand(' + e.id + ')"><span class="material-icons" style="color:#CCCC00">edit</span></button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
    $tbody.append(row);
	}
}


function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	ajaxQuery(url,'GET','',displayBrand,handleAjaxError);
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

function displayUploadData(){
	resetUploadDialog();
	$("#download-errors").prop("disabled",true);
	$('#upload-brand-modal').modal('toggle');
}

function displayAddBrandModal() {
	$("#add-brand-modal").modal('toggle');
}

//FILE METHODS

var fileData = [];
var rowsProcessed = 0;

function processData(){
	var file = $('#brandFile')[0].files[0];
	checkHeader(file,["brand","category"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
	$("#download-errors").prop("disabled",false);
	//If everything processed then return
	if(rowsProcessed==fileData.length){
		getBrandList();
		return;
	}

	//Process next row
	var row = fileData[rowsProcessed];
	console.log(row);
	rowsProcessed++;

	var json = JSON.stringify(row);

	var url = getBrandUrl();

	//Make ajax call
	ajaxQueryRecur(url,'POST',json,uploadRows,uploadRows);

}

function downloadErrors(){
	writeFileData(errorData);
}


function resetUploadDialog(){

	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");

	rowsProcessed = 0;
	fileData = [];
	errorData = [];
	updateUploadDialog();
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function validateBrand(json) {
	json = JSON.parse(json);
	if(isBlank(json.brand)) {
		toastr.error("Brand field must not be empty");
		return false;
	}
	if(isBlank(json.category)) {
		toastr.error("Category field must not be empty");
		return false;
	}
	return true;
}

function updateUploadDialog(){
	var correct_rows = parseInt(fileData.length) - parseInt(errorData.length);
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + correct_rows);
	$('#errorCount').html("" + errorData.length);
}

function brandFilter() {
	$("#brand-filter").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#brand-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
}



//INITIALIZATION CODE
function init(){
	$("#open-add-brand").click(displayAddBrandModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#download-errors').click(downloadErrors);
	$('#process-data').click(processData);
	$('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(brandFilter);
