var barcodeList = [];
var brandList = [];
var categoryList = [];
var inventoryMap = {};

/*toastr.options = {
    timeOut: 0,
    extendedTimeOut: 0,
"preventDuplicates": true,
"preventOpenDuplicates": true,
};*/


function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product";
}

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/inventory";
}

function createBarcodeList() {
	var url = getProductUrl();
		$.ajax({
	   url: url,
	   type: 'GET',
	   
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		formBarcodeList(response);
	   },
	   error: handleAjaxError
	});
}

function createBrandCategoryList() {
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		formBrandCategoryList(response);
	   },
	   error: handleAjaxError
	});
}

function createInventoryMap() {
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		formInventoryMap(response);
	   },
	   error: handleAjaxError
	});
}

function formBarcodeList(data) {
	for(var i in data){
		var e = data[i];
		barcodeList.push(e.barcode);
	}
}

function formBrandCategoryList(data) {
	for(var i in data){
		var e = data[i];
		brandList.push(e.brand);
		categoryList.push(e.category);
	}
}

function formInventoryMap(data) {
	for(var i in data){
		var e = data[i];
		inventoryMap[e.barcode] = e.quantity;
	}
}


//HELPER METHOD
function toJson($form) {
	var serialized = $form.serializeArray();
	console.log(serialized);
	var s = '';
	var data = {};
	for (s in serialized) {
		data[serialized[s]['name']] = serialized[s]['value']
	}
	var json = JSON.stringify(data);
	return json;
}


function handleAjaxError(response) {
	var response = JSON.parse(response.responseText);
	toastr.error(response.message);
}

function readFileData(file, callback) {
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
		}
	}
	Papa.parse(file, config);
}


function writeFileData(arr) {
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
	var blob = new Blob([data], { type: 'text/tsv;charset=utf-8;' });
	var fileUrl = null;

	if (navigator.msSaveBlob) {
		fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
	} else {
		fileUrl = window.URL.createObjectURL(blob);
	}
	var tempLink = document.createElement('a');
	tempLink.href = fileUrl;
	tempLink.setAttribute('download', 'download.tsv');
	tempLink.click();
}

function init(){
	createBarcodeList();
	createBrandCategoryList();
	createInventoryMap();
	$(".barcode").autocomplete({
		minLength:0,
		source: barcodeList
	});
	$(".brand").autocomplete({
		minLength:0,
		source: brandList
	});
	$(".category").autocomplete({
		minLength:0,
		source: categoryList
	});
}
$(document).ready(init);
