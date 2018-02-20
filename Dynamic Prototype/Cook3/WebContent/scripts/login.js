$(document).ready(function() {
	$("#loginButton").click(function() {
		$.post("rest/login", {
			username : $("#username").val(),
			password : $("#password").val()
		}, function(data, status) {
			if(data == "Success"){
				 document.cookie = "uuid="
				// TODO: SESSION COOKIE
				window.location.href = "http://localhost:8080/Cook2/index.html";
			}
			else{
				alert("Wrong username or password!");
			}
		});
	});
});