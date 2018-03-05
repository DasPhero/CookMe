$.holdReady(true); //disable document == ready

const GET_ALL_INGREDIENTS = "-3";

$.get("rest/ingredientList/"+GET_ALL_INGREDIENTS, function(data, status) { //get items
	obj = JSON.parse(data);
	let itemSource = "";
	obj.forEach(function(element) {
		itemSource += "<div class=\"fridgeItem\">" +
							"<input type=\"checkbox\" class=\"fridgeCheckbox\" id=\"item" + element["id"] + "\">"+
							"<span class=\"fridgeDescription\">" + element["name"] + "</span>" +
						"</div>";
	});

	$(".fridgeList ").html(itemSource);//override static code
	$.holdReady(false);		
});