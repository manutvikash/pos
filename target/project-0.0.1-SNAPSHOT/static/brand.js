function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}
var page = false;
function displayAddBrandModal() {
	$("#add-brand-modal").modal('toggle');
}
function addBrand(event) {
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	if (validateBrand(json)) {
		var url = getBrandUrl();
		$.ajax({
			url: url,
			type: 'POST',
			data: json,
			headers: {
				'Content-Type': 'application/json'
			},
			success: function(response) {
				$("#add-brand-modal").modal('toggle');	
				toastr.success("Brand created successfully");
				getAllBrand(response);
				getBrandList(response);
				//$("#brand-form").trigger("reset");
			},
			error: handleAjaxError
		});
	}
	return false;
}
function validateBrand(json) {
	json = JSON.parse(json);
	if (isBlank(json.brand)) {
		toastr.error("Brand field must not be empty");
		return false;
	}
	if (isBlank(json.category)) {
		toastr.error("Category field must not be empty");
		return false;
	}
	return true;
}

function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}
function updateBrand(event) {

	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	if (validateBrand(json)) {

		$.ajax({
			url: url,
			type: 'PUT',
			data: json,
			headers: {
				'Content-Type': 'application/json'
			},
			success: function(response) {
				toastr.success("Brand updated successfully");
				getAllBrand(response);
				getBrandList(response);
				$('#edit-brand-modal').modal('toggle');
			},
			error: handleAjaxError
		});
	}
	return false;
}

function getBrandList() {
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			displayBrandList(data);
		},
		error: handleAjaxError
	});
}

//UI
function displayBrandList(data) {

	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
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

function getAllBrand() {
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			displaySearchBrandCategory(data);
			//displaysearchBrand(data);
		},
		error: handleAjaxError
	});
}
function displaySearchBrandCategory(data) {
	var $brandBody = $('#searchForm').find('#enterInputBrand');
	var $categoryBody = $('#searchForm').find('#enterInputCategory');
	$brandBody.empty();
	$categoryBody.empty();
	var brandSet = new Set();
	var categorySet = new Set();

	for (var i in data) {
		var e = data[i];
		brandSet.add(e.brand);
		categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	categorySet = Array.from(categorySet);

	row1 = '<option value="select">Brand</option>';
	row2 = '<option value="select">Category</option>';
	$brandBody.append(row1);
	$categoryBody.append(row2);
	for (var i in brandSet) {
		row1 = '<option value=' + brandSet[i] + '>' + brandSet[i] + '</option>';
		$brandBody.append(row1);
	}
	for (var i in categorySet) {

		row2 = '<option value=' + categorySet[i] + '>' + categorySet[i] + '</option>';
		$categoryBody.append(row2);
	}
}

function displaysearchBrand(data) {
	//$("#enterInputCategory").prop("disabled",ture);
	document.getElementById("enterInputCategory").disabled = true;
	/*var brand=$("#enterInputBrand :selected").text();
	var json=JSON.stringify(brand);*/
	var $brandBody = $('#searchForm').find('#enterInputBrand');
	$brandBody.empty();
	var brandSet = new Set();
	for (var i in data) {
		var e = data[i];
		brandSet.add(e.brand);
		//categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	//categorySet = Array.from(categorySet);

	//row = '<option value="select">select</option>';
	//$brandBody.append(row);
	//$categoryBody.append(row);
	for (var i in brandSet) {
		row = '<option value=' + brandSet[i] + '>' + brandSet[i] + '</option>';
		$brandBody.append(row);
	}

}

function searchBrand(s) {
	//var brand=$("#enterInputBrand :selected").text();
	var brand = s;
	var json = JSON.stringify(brand);
	console.log(json);

}
function searchBrandCategory() {
	var brand = $("#enterInputBrand :selected").text();
	var category = $("#enterInputCategory :selected").text();
	var obj = { brand, category };

	if (obj.brand === "Brand") {
		obj.brand = "";
	}
	if (obj.category === "Category") {
		obj.category = "";
	}
	var json = JSON.stringify(obj);
	console.log(json);
	var url = getBrandUrl() + "/search";
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(data) {
			displayBrandList(data);
		},
		error: handleAjaxError
	});
}
function displayEditBrand(id) {
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

function displayBrand(data) {
	$("#brand-edit-form input[name=id]").val(data.id);
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$('#edit-brand-modal').modal('toggle');

}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
	var file = $('#brandFile')[0].files[0];
	/*readFileData(file, readFileDataCallback);*/
	checkHeader(file, ["brand", "category"], readFileDataCallback);
}


function checkHeader(file, header_list, callback) {
	var allHeadersPresent = true;
	Papa.parse(file, {
		delimiter: "\t",
		step: function(results, parser) {

			for (var i = 0; i < header_list.length; i++) {
				if (!results.data.includes(header_list[i])) {
					allHeadersPresent = false;
					break;
				}
			}

			parser.abort();
			results = null;
			delete results;

		}, complete: function(results) {

			results = null;
			delete results;
			if (allHeadersPresent) {
				readFileData(file, callback);
			}
			else {
				toastr.error("Wrong file. Please check the head of it");
			}

		}
	});
}


function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	$("#download-errors").prop("disabled", false);

	if (processCount == fileData.length) {
		getBrandList();
		getAllBrand();
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
		error: function(response) {
			row.error = JSON.parse(response.responseText).message
			errorData.push(row);
			uploadRows();
		}
	});

}


function downloadErrors() {
	writeFileData(errorData);
}



function displayUploadData() {
	resetUploadDialog();
	$("#download-errors").prop("disabled", true);
	$('#upload-brand-modal').modal('toggle');
}

function resetUploadDialog() {
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

function updateUploadDialog() {
	var correct = parseInt(fileData.length) - parseInt(errorData.length);
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + correct);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName() {
	var $file = $('#brandFile');
	var path = $file.val();
	var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#brandFileName').html(fileName);
}



/*function getReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/report";
}*/

function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/brand-report";
}




function downloadBrandReport(){
	var url = getBrandReportUrl();
	console.log(url);
	console.log("hello");
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
                var brandreportname = "brand-report_"+ currentdate.getDate() + "/"
                                 	+ (currentdate.getMonth()+1)  + "/"
                                 	+ currentdate.getFullYear() + "@"
                                 	+ currentdate.getHours() + "h_"
                                 	+ currentdate.getMinutes() + "m_"
                                 	+ currentdate.getSeconds()+"s.tsv";
                if (navigator.msSaveBlob) {
                    fileUrl = navigator.msSaveBlob(blob, brandreportname);
                } else {
                    fileUrl = window.URL.createObjectURL(blob);
                }
                var tempLink = document.createElement('a');
                tempLink.href = fileUrl;
                tempLink.setAttribute('download', brandreportname);
                tempLink.click();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function init() {
	$("#open-add-brand").click(displayAddBrandModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#brandFile').on('change', updateFileName)
	$('#search-brand').click(searchBrandCategory);
	$('#brand-report').click(downloadBrandReport);

	//$('#enterInputBrand').on('change',searchBrand);
}

function pagination() {
	$('#brand-table').after('<div id="nav" style="text-align: center; padding-bottom: 3%; font-size: 16px;"></div>');
	var rowsShown = 8;
	var rowsTotal = $('#brand-table tbody tr').length;
	var numPages = rowsTotal / rowsShown;
	for (i = 0; i < numPages; i++) {
		var pageNum = i + 1;
		$('#nav').append('<a href="#" rel="' + i + '">' + pageNum + '</a> ');
	}
	$('#brand-table tbody tr').hide();
	$('#brand-table tbody tr').slice(0, rowsShown).show();
	$('#nav a:first').addClass('active');
	$('#nav a').bind('click', function() {
		$('#nav a').removeClass('active');
		$(this).addClass('active');
		var currPage = $(this).attr('rel');
		var startItem = currPage * rowsShown;
		var endItem = startItem + rowsShown;
		$('#brand-table tbody tr').css('opacity', '0.0').hide().slice(startItem, endItem).
			css('display', 'table-row').animate({ opacity: 1 }, 300);
	});

}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(pagination);
$(document).ready(getAllBrand);
