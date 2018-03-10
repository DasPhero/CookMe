const GET_ALL_INGREDIENTS = "-3";

loadSelectedItems = () => {
	var fridgeContent = window.localStorage.getItem("fridgeContent").split(",");
	 if(fridgeContent[0] == ""){
		 return;
	 }
	fridgeContent.forEach(item => {
		let a = $("#" + item).parent().click();
	});
}

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

listsRecipesForSelectedIngredients = () => {
	let whereSQL = "";
	let activeItems = [];
	$(".fridgeItem").each( function( index ) {
		if ($(this).find(".fridgeCheckbox").is(':checked')){
			let id=$(this).find(".fridgeCheckbox").attr("id");
			activeItems.push(id);
//			whereSQL += "recipeitems.fk_item = " + id.slice(4) +" ||";
			whereSQL += " || " +id.slice(4);
		}
	});
	window.localStorage.setItem("fridgeContent", activeItems);
	
	//if (whereSQL.length > 2){
	//	whereSQL = whereSQL.slice(2,whereSQL.length);
	//}
	
	whereSQL+="";
	$.ajax({ 
		type: "PUT",
		contentType: "application/x-www-form-urlencoded",
		url: "rest/ingredientList",
		data: {	where: whereSQL},
		success:function(response){
			if (response != ""){
				setRecipeListItem(response);
			}else{
				$(".recEntryWrapper").html("<div class=\"recEntry\">Keine Rezepte gefunden. Wähle weitere Zutaten aus, damit Rezepte angezeigt werden.</div>");
			}
		},
		error: function(){
			console.log("Error");
		}
	});	
};

function setRecipeListItem(response){
	obj = JSON.parse(response);
	let recipeSource = "";
	obj.forEach(function(recipe) {
		recipeSource+="<div class=\"recEntry\" id=\"searchRecipe"+ recipe["id"]+"\" onclick=\"window.location.assign('index.html?id="+ recipe["id"] + "');\">"+recipe["title"] +":  "+ recipe["count"]+ " Zutaten enthalten </div>";
	});
	$(".recEntryWrapper").html(recipeSource);
}

registerClickHandler = () => {
	$(".fridgeItem").click(function (e) {    
		  if (e.target.tagName != 'INPUT') {
			  $(this).find("input").toggleCheckbox();
			  let checkboxIsChecked = $(this).find("input").attr('checked');
			  let color = checkboxIsChecked? "#3C7216" : "#5f9539";
			  $(this).css('background-color',color);
			  listsRecipesForSelectedIngredients();
			  return false; 
			}
		});
}

$(document).ready(function() {
	registerClickHandler();
	loadSelectedItems();
});	