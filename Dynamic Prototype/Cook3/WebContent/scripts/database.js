$.holdReady(true); //disable document == ready

const GET_NAV_TITLES = "-1";
const GET_CATEGORIES = "-2";

$.get("rest/recipe/"+GET_CATEGORIES, function(data, status) { //get categories
	obj = JSON.parse(data);
	let categorySource = "";
	
	obj.forEach(function(element) {
		categorySource += "<li class=\"rCategory\" id=\"r"
				+ element["id"] + "\">"+ "▿" + element["name"]
				+ "</li><ul class=\"c" + element["id"] + "\"></ul>"; //set class to "c1", "c2" ..
	});
	categorySource += "<li id='searchNotification' hidden>Keine Rezepte gefunden</li>"
	$(".listWrapper ").html(categorySource);//override static code
		
	let cookie = getAndValidateCookie();
	if(!cookie){
		getTitles();
	}else{
		$.get("rest/favourization/favourizedItems/" + cookie,
				(favourites) => { 
					let parsedArray = JSON.parse(favourites);
					getTitles(parsedArray);
				}
		);		
	}
});

function getTitles(favouriteArray = []) {
	$.get("rest/recipe/"+GET_NAV_TITLES,// -1 = get all recipes
			function(data2, status) {
				recipes = JSON.parse(data2); //parse to JSON
				$(".rCategory").each(function(i, category) {
					let recipeListSource = "";
					let url = window.location.href;
					recipes.forEach(function(recipe) {
						if(url.includes("profile")){
							if ($(category).attr('id') == "r"+ recipe["category"] && (favouriteArray.indexOf(recipe.id) != -1)){
								recipeListSource = createListItemForRecipe(recipe, recipeListSource);
							}							
						}
						else{
							if ($(category).attr('id') == "r"+ recipe["category"]){
								recipeListSource = createListItemForRecipe(recipe, recipeListSource);
							}
						}
					});
					$(category).next(" ul").html(recipeListSource);
					let noFavouritesSelected = favouriteArray.length == 0 && url.includes("profile"); 
					if(noFavouritesSelected){
						$(".listWrapper").html("<li style='font-family: inherit; font-size: 16px;'>Keine Rezepte favorisiert</li>");
					}
				});
				tickAlreadySelectedItems();
				hideEmptyCategories();
				setCookie();			//set cookie and get ingredients item list
			});
}

createListItemForRecipe = (recipe, recipeSource) => {
	recipeSource +=
		"<li><input type=\"checkbox\" onchange=\"updateSelectedItems(this)\" class=\"checkbox "
		+ recipe["id"]
	+ "\"/> <span class=\"listEntry\" id=\"recipe"
	+ recipe["id"]
	+ "\" onclick='markAsActive(this)'>"
	+ recipe["title"]
	+ "</span></li>";
	
	return recipeSource;
}

hideEmptyCategories = () => {	
	$(".rCategory").each((i, category) => {
		let categoryHeader = $("#" + category.id);
		let categoryWrapper = categoryHeader.next();
		if(categoryWrapper[0].innerHTML == ""){
			categoryHeader.css("display", "none");
		}
	});
}

function setCookie(){ //set cookie and get ingredients item list
	let pathName = window.location.pathname;
	var cookies = document.cookie;
	if( cookies != ""){
		var cookie = cookies.split("=")[1];
		if (cookie != "" && cookie != '0' && cookie != 0 && cookie != null && cookie != "invalid"){
			$.get("rest/login/username/"+cookie, function(userName, status) {
				userName = userName.replace(/\b\w/g, l => l.toUpperCase());
				$(".userNameHeader").html(userName);
				styleOnLogin();
				if (pathName.includes("profile")){
					getIngredientsList();
				}else{
					$.holdReady(false); //set document ready
				}
			});
		}else{
			if (pathName.includes("profile")){
				getIngredientsList();
			}else{
				$.holdReady(false); //set document ready
			}
		}
	}else{
		if (pathName.includes("profile")){
			getIngredientsList();
		}else{
			$.holdReady(false); //set document ready
		}
	}
}

styleOnLogin = () => {
	$("[name='sendCommentButton']").prop("disabled", false);
	$("[name='addToFavourites']").css("visibility", "visible").prop("disabled", false);
	$(".commentInput").attr("placeholder", "Kommentar verfassen...");
	$(".symbolWrapperIndex").text("Profil");
	$(".commentInput").prop("disabled", false);
}

function getRecipe(titleM2,object){
	$(".commentInput").unbind("keyup");
	var id = object.substr(6);
	let url1 =window.location.href;
	if (url1.includes("profile")){
		window.location.assign("index.html?id="+ id);
	}
	
	localStorage.setItem("currentRecipeId", id);
	
	$(".h1Wrapper h1").html(titleM2);
	$(".recipeSteps h2").next("ul").html("Rezept wird geladen");
	$(".recipeSteps h2").next("ul").html("<div>Zutatenliste wird geladen</div>");
	styleOnSelectingRecipe();

	$.get("rest/recipe/" + id,// -1 = get all recipes
	function(responseData) {
		if (responseData.length > 1){			
			recipe = JSON.parse(responseData); //parse to JSON
			var stepSource="";
			var ingredientsSource="";
			var foodFactSource="";
			
			
			var description =  JSON.parse(recipe[0]["description"]);
			var ingredients =  JSON.parse(recipe[0]["ingredients"]);
			var nutritionFacts =  JSON.parse(recipe[0]["nutritionfacts"]);

			description.forEach((step) => {
				stepSource += "<li>" + step + "</li>";
			});
			ingredients.forEach(function(ingredient) {
				ingredientsSource += "<div class=\"ingredientItem\">";
				if(0 != ingredient["value"]){
					ingredientsSource+= "<span class=\"recipeValue\">" + ingredient["value"]+ "</span>";
				}
				
				ingredientsSource+= "</span class=\"recipeUnit\"> " +ingredient["unit"] + "</span>"
				+ "</span class=\"recipeItem\"> " +ingredient["item"] + "</span>"
				+ "</div>";
			});

			
			foodFactSource = " <div class=\"nutrionInformation\">Kalorien: </div> \
             <div class=\"calInformation nutrionInformation\">"+ nutritionFacts.cal +"</div>  \
             <div class=\"nutrionInformation\">Fett: </div>  \
             <div class=\"fatInformation nutrionInformation\">"+ nutritionFacts.fat +" </div> \
             <div class=\"nutrionInformation\">Kohlenhydrate: </div> \
             <div class=\"carbInformation nutrionInformation\">"+ nutritionFacts.carbs +"</div> \
             <div class=\"nutrionInformation\">Eiweiß: </div> \
             <div class=\"proteinInformation nutrionInformation\">"+ nutritionFacts.protein +"</div> \
             <div class=\"nutrionInformation\">Salz: </div> \
             <div class=\"saltInformation nutrionInformation\">"+ nutritionFacts.salt +"</div> ";
			
			$(".recipeSteps h2").next("ul").html(stepSource);
			$(".ingredientList").html(ingredientsSource);
			$(".nutritionalFacts").html(foodFactSource);
			

			let recipeId = JSON.parse(recipe[0].id);
			checkIfItemAlreadyIsFavourized(recipeId);
			loadExistingComments();
			
			$(".commentInput").unbind("keyup");
			$(".commentInput").keyup(function(event) {
				event.preventDefault();
				if (event.keyCode === 13) { $("[name='sendCommentButton']").click() }
				}
			);
		}
	});
}

styleOnSelectingRecipe = () => {
	$(".h1Wrapper").css("flex-basis", "80%");
	$(".h1Wrapper h1").css("font-size", "2em");
	$(".recipeNameHeader button").css("flex-basis", "20%");
	$(".subheader").css("display", "none");
	$(".recipeDetails").css("display", "initial");
	$(".commentSection").css("visibility", "visible");
	$("[name='addToFavourites']").css("display", "initial");
}
