

function addBrand(){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = './api';
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		alert("Done"); 
	   },
	   error: function(){
		alert("An error has occured");
	}
	});

	return false;
}
function init(){
	$('#add-brand').click(addBrand);

}
$(document).ready(init);