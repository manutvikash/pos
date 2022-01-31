function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}
var page=false;
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	if(validateBrand(json)){
	var url=getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList(response);
            $('#add-brand-modal').modal('toggle');   
	   },
	   error: handleAjaxError
	});
}
	return false;
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

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}
function updateBrand(event){
	
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

    if(validateBrand(json)){

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList(response);
          $('#edit-brand-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});
}
	return false;
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
              if(!page){
	          pagination();
               }  
	   },
	   error: handleAjaxError
	});
}

//UI
function displayBrandList(data){
	
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-primary" style="border: none;" onclick="displayEditBrand(' + e.id + ')"><i class="icon-edit editicon"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	
}


function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);   
	   },
	   error: handleAjaxError
	});	
}

function displayBrand(data){
	$("#brand-edit-form input[name=id]").val(data.id);
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);		
	$('#edit-brand-modal').modal('toggle');
	
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	/*readFileData(file, readFileDataCallback);*/
	checkHeader(file,["brand","category"],readFileDataCallback);
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
	$("#download-errors").prop("disabled",false);
	
	if(processCount==fileData.length){
		getBrandList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
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


function downloadErrors(){
	writeFileData(errorData);
}



function displayUploadData(){
 	resetUploadDialog(); 
    $("#download-errors").prop("disabled",true);	
	$('#upload-brand-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var $file = $('#brandFile');
	var path=$file.val();
	var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#brandFileName').html(fileName);
}


function brandFilter() {
	$("#brand-filter").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#brand-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > 0);
    });
  });
}

var numOfVisibleRows = $('tr:visible').length;
console.log(numOfVisibleRows);


function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	/*$('#refresh-data').click(getBrandList);*/
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)

}
$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(brandFilter);

function pagination() {  
    $('#brand-table').after ('<div id="nav" style="text-align: center; padding-bottom: 3%; font-size: 16px;"></div>');  
    page=false;   
 var rowsShown = 8;  
    var rowsTotal = $('#brand-table tbody tr').length;  
    var numPages = rowsTotal/rowsShown;  
    for (i = 0;i < numPages;i++) {  
        var pageNum = i + 1;  
        $('#nav').append ('<a href="#" rel="'+i+'">'+pageNum+'</a> ');  
    }  
    $('#brand-table tbody tr').hide();  
    $('#brand-table tbody tr').slice (0, rowsShown).show();  
    $('#nav a:first').addClass('active');  
    $('#nav a').bind('click', function() {  
    $('#nav a').removeClass('active');  
   $(this).addClass('active');  
        var currPage = $(this).attr('rel');  
        var startItem = currPage * rowsShown;  
        var endItem = startItem + rowsShown;  
        $('#brand-table tbody tr').css('opacity','0.0').hide().slice(startItem, endItem).  
        css('display','table-row').animate({opacity:1}, 300);  
    });  
   page=true;
} 


$('.navl li').click(function(){
    $('.navl li').removeClass('active');
    $(this).addClass('active');
})