$(document).ready(function() {
	$(".loginDataInput").keyup(function(event) { //click at enter
		event.preventDefault();
		if (event.keyCode === 13) {document.getElementById("loginButton").click();}});
	$("#loginButton").click(function() {
		$.post("rest/login", {
			username : $("#username").val(),
			password : $("#password").val()
		}, function(data, status) {
			if(data.id != -1){
				$.get("rest/login/cookie/"+data.id, function(data2, status) {
					$.ajax({ 
						type: "PUT",
					    contentType: "application/x-www-form-urlencoded",
					    url: "rest/login",
					    data: {id: data.id, cookie:data2},
					    success:function(response){
					        document.cookie = "uuid="+ data2;
					        window.location.assign('profile.html');            
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
			
			$.get("rest/login/username/"+cookie, function(userName, status) {
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

getAndValidateCookie = () => {
	let activeCookies = document.cookie;
	let relevantCookieIndex = activeCookies.indexOf("uuid=");

	if(relevantCookieIndex == -1){
		return false;
	}

	const cookieLength = 20;
	const keyNameLength = 5;
	let cookieValue = activeCookies.substr(relevantCookieIndex + keyNameLength, cookieLength);

	if(cookieValue === "invalid"){
		return false;
	}

	return cookieValue;
}

var deleteCookie = function(cookieName) {
    document.cookie = cookieName + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};