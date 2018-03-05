$.holdReady(true); //disable document == ready

const GET_ALL_INGREDIENTS = "-3";

$.get("rest/ingredientList/"+GET_ALL_INGREDIENTS, function(data, status) { //get items
	obj = JSON.parse(data);
	let itemSource = "";
	obj.forEach(function(element) {
		itemSource += "<div class=\"entryContainer\" id=\"item" + element["id"] + "\">"+
							"<span class=\"textBlock\">" + element["name"] + "</span>" +
							"<button name=\"deleteListItem\">" +
									"<span class=\"deleteSymbol\">X</span>" +
							"</button>" +
						"</div>";
	});

	$(".fridgeListView ").html(itemSource);//override static code
	$.holdReady(false);		
});