//const GET_ALL_INGREDIENTS = "-3";
const GET_ALL_UNITS = "-4";
//const GET_NAV_TITLES = -1;

function getIngredientsComboBox(){
	getUnitComboBox();
	$.get("rest/ingredientList/-3", function(data, status) { //get items
		obj = JSON.parse(data);
		let itemSource = "<select id=\"itemId\">";
		obj.forEach(function(element) {
			itemSource += "<option value=\"item" + element["id"] + "\">" + element["name"] + " </option>";
		});
		itemSource += "</select>";
		$(".recipeItem ").html(itemSource);//override static code
		$.holdReady(false);
	});
}

function getUnitComboBox(){
	getRecipeTitles()
	$.get("rest/unit/"+GET_ALL_UNITS, function(data, status) { //get items
		obj = JSON.parse(data);
		let itemSource = "<select id=\"unitId\">";
		obj.forEach(function(element) {
			itemSource += "<option value=\"item" + element["id"] + "\">" + element["name"] + "</option>";
		});
		itemSource += "</select>";
		$(".recipeUnit ").html(itemSource);//override static code
		//$.holdReady(false);
	});
}

function getRecipeTitles() {
	getRecipeCategories();
	$.get("rest/recipe/-1",// -1 = get all recipes
	function(data2, status) {
		recipes = JSON.parse(data2); //parse to JSON
		let itemSource = "<select id=\"recipeId\">";
		recipes.forEach(function(element) {
			itemSource += "<option value=\"item" + element["id"] + "\">" + element["title"] + "</option>";
		});
		itemSource += "</select>";
		$(".recipeId ").html(itemSource);//override static code
		//$.holdReady(false);
	});
}

function getRecipeCategories() {
	$.get("rest/recipe/-2",// -2 = get all categories
	function(data2, status) {
		recipes = JSON.parse(data2); //parse to JSON
		let itemSource = "<select id=\"categoryId\">";
		recipes.forEach(function(element) {
			itemSource += "<option value=\"item" + element["id"] + "\">" + element["name"] + "</option>";
		});
		itemSource += "</select>";
		$(".recipeCategory ").html(itemSource);//override static code
		//$.holdReady(false);
	});
}

function addToRecipe(){
	$.post("rest/recipeitem", {
		recipeId : $("#recipeId").val().substring(4),
		unitId : $("#unitId").val().substring(4),
		itemId : $("#itemId").val().substring(4),
		value : $("#itemValue").val()
		}, function(data, status) {
			if(data.id != false){
				alert("Zutat wurde hinzugefügt");
			}
			else{
				alert("Zutat konnte nicht hinzugefügt werden!");
			}
	});
}

function addNewRecipe(){

	
	$.post("rest/recipe", {
		recipeTitle : $("#recipeTitle").val(),
		category : $("#categoryId").val().substring(4),
		recipeText : $("#recipeText").val()
		}, function(data, status) {
			if(data.id != false){
				alert("Neues Rezept wurde hinzugefügt");
			}
			else{
				alert("Neues Rezept konnte nicht hinzugefügt werden!");
			}
		});
}