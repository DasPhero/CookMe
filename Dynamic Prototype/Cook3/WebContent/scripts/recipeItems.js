const GET_ALL_INGREDIENTS = "-3";

function getIngredientsList(){
$.get("rest/ingredientList/"+GET_ALL_INGREDIENTS, function(data, status) { //get items
	obj = JSON.parse(data);
	let itemSource = "";
	obj.forEach(function(element) {
		itemSource += "<div class=\"fridgeItem\" id=\"fridgeItem"+ element["id"] + "\">" +
							"<input type=\"checkbox\" class=\"fridgeCheckbox\" id=\"item" + element["id"] + "\" disabled>"+
							"<span class=\"fridgeDescription\">" + element["name"] + "</span>" +
						"</div>";
	});

	$(".fridgeList ").html(itemSource);//override static code
	$.holdReady(false);
});
}

$(document).ready(function() {

listsRecipesForSelectedIngredients = () => {
	console.log("clicked");
	let selectSQL = "";
	$(".fridgeItem").each( function( index ) {
		if ($(this).find(".fridgeCheckbox").is(':checked')){
			let id=$(this).find(".fridgeCheckbox").attr("id");
			selectSQL += "recipeitems.fk_item = " + id.slice(4) +" ||";
		}
	});
	if (selectSQL.length > 2){
		selectSQL = selectSQL.slice(0,selectSQL.length -2);
	}
	selectSQL+="";
	console.log(selectSQL);
	$.ajax({ 
		type: "PUT",
		contentType: "application/x-www-form-urlencoded",
		url: "rest/ingredientList",
		data: {select: selectSQL},
		success:function(response){
			console.log("ok");
		},
		error: function(){
			console.log("Error");
		}
	});	
};
	
	
});	