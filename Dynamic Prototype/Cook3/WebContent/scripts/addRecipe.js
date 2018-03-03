$(document).ready(function() {
	$("#addRecipe").click(function() {
		alert("außer Funktion");
		/*$.post("rest/recipe", {
			title : $("#title").val(),
			ingredients : $("#ingredients").val(),
			description : $("#description").val(),
			category : $("#category").val(),
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
		*/
	});
});