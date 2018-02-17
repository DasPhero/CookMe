$(document).ready(function() {
	$(".listEntry").click(function() {
		$.get("rest/recipe/-1", function(data, status) {
			obj = JSON.parse(data);
			var quellcode="<li>Aufläufe</li><ul>";
			obj.forEach(function(element) {
				 // alert(element["id"]);
				quellcode= quellcode  +  "<li><input type=\"checkbox\" id=\""  + element["id"] + "\"/> <span class=\"listEntry\">" + element["titel"]+ "</span></li>";
			});
			quellcode = quellcode + "</ul>";
			$(".listWrapper ").html(quellcode);
		});
	});
});