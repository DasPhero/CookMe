$(document).ready(function() {
	$("#loginButton").click(function() {
		$.post("rest/login", {
			username : $("#username").val(),
			password : $("#password").val()
		}, function(data, status) {
			if(data.id != -1){
				
				$.get("rest/login/1", function(data2, status) {

					$.ajax({ 
						type: "PUT",
					    contentType: "application/x-www-form-urlencoded",
					    url: "rest/login",
					    data: {id: data.id, cookie:data2},
					    success:function(response){
					        document.cookie = "uuid="+ data2;
					        window.location.href = "http://localhost:8080/Cook2/index.html";            
					    },
					    error: function(){
					    	alert("Cookie konnte nicht in der Datenbank gespeichert werden");
					    }
					});			
					
				});
			}
			else{
				document.cookie = "uuid=invalid";
				alert("Wrong username or password!");
			}
		});
	});
});