$(document).ready(function() {
	var $postButton, $multipartPostButton;
	
	OData.defaultError = function(err) {
		alert("Error: " + err.message + " - " + err.response.body);
	};
	
	$postButton = $("#post").button();
	$postButton.click(function() {
		$.post("servlet/customers");
	});
	
	$multipartPostButton = $("#multiparrt_post").button();
	$multipartPostButton.click(function() {
		OData.request({
			requestUri : "servlet/customers",
			method : "POST",
			data : {
				__batchRequests : [ {
					requestUri : "Evaluations(customerId=1,evalID=3)",
					method : "GET"
				}, {
					requestUri : "Evaluations(customerId=1,evalID=4)",
					method : "GET"
				} ]
			}
		}, function(data, response) {
			alert(response);
		}, undefined, OData.batchHandler);
	});
	
});