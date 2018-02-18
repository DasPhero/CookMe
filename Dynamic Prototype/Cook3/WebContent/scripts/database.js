$.holdReady( true );
$.get("rest/recipe/-1/3", function(data, status) {
	obj = JSON.parse(data);
	var quellcode = "<li id=\"rCategory\">Aufläufe</li><ul>";
	obj.forEach(function(element) {
		// alert(element["id"]);
		quellcode = quellcode + "<li><input type=\"checkbox\" id=\""
				+ element["id"] + "\"/> <span class=\"listEntry\">"
				+ element["titel"] + "</span></li>";
	});
	quellcode = quellcode + "</ul>";
	$(".listWrapper ").html(quellcode);
	$.holdReady( false );
});
