$(document).ready(function() {
	$("#loginButton").click(function() {
		$.post("rest/login", {
			username : $("#username").val(),
			password : $("#password").val()
		}, function(data, status) {
			alert(data);
		});
	});
});