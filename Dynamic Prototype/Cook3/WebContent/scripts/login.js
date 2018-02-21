$(document).ready(function() {
	$("#loginButton").click(function() {
		$.post("rest/login", {
			username : $("#username").val(),
			password : $("#password").val()
		}, function(data, status) {
			if(data.id != -1){
				$.get("rest/login/"+data.id+"/0", function(data2, status) {

					$.ajax({ 
						type: "PUT",
					    contentType: "application/x-www-form-urlencoded",
					    url: "rest/login",
					    data: {id: data.id, cookie:data2},
					    success:function(response){
					        document.cookie = "uuid="+ data2;
					        window.location.href = "http://localhost:8080/Cook2/profile.html";            
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

function ckeckLogIn() {
	var cookies = document.cookie;
	if( cookies != ""){
		var cookie = cookies.split("=")[1];
		if (cookie != "" && cookie != '0' && cookie != 0 && cookie != null && cookie != "invalid"){
			
			$.get("rest/login/"+RESOLVE_USERNAME+"/"+cookie, function(userName, status) {
				if (userName != ""){
					window.location.assign('profile.html');
				}else{
					window.location.assign('login.html');
				}
			});
		}else{
			window.location.assign('login.html');
		}
		
	}else{
		window.location.assign('login.html');
	}
}	