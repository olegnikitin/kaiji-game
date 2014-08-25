$(document).ready(function() {
	$(function() {
		$("#gameName").autocomplete({
			source : function(request, response) {
				$.ajax({
					url : "/createdgames",
					type : "POST",
					data : {
						joinGameName : request.term
					},
					dataType : "json",
					success : function(data) {
						response(data);
					}
				});
			}
		});
	});
});
