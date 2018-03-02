$.holdReady(true); //disable document == ready

const GET_NAV_TITLES = "-1";
const GET_CATEGORIES = "-2";

$.get("rest/recipe/"+GET_CATEGORIES, function(data, status) { //get categories
	obj = JSON.parse(data);
	let categorySource = "";
	
	obj.forEach(function(element) {
		categorySource += "<li class=\"rCategory\" id=\"r"
				+ element["id"] + "\">" + element["name"]
				+ "</li><ul class=\"c" + element["id"] + "\"></ul>"; //set class to "c1", "c2" ..
	});
	$(".listWrapper ").html(categorySource);//override static code
		
	getTitles();//get titles of each recipe
	
});

function getTitles() {
	$.get("rest/recipe/"+GET_NAV_TITLES,// -1 = get all recipes
			function(data2, status) {
				recipes = JSON.parse(data2); //parse to JSON
				$(".rCategory").each(function(i, category) {
					let recipeListSource = "";
					recipes.forEach(function(recipe) {
						if ($(category).attr('id') == "r"+ recipe["category"]) {
							recipeListSource +=
							"<li><input type=\"checkbox\" id=\"checkbox"
							+ recipe["id"]
							+ "\"/> <span class=\"listEntry\" id=\"recipe"
							+ recipe["id"]
							+ "\">"
							+ recipe["title"]
							+ "</span></li>";
						}
					});
					$(category).next(" ul").html(recipeListSource);
				});
				var cookies = document.cookie;
				if( cookies != ""){
					var cookie = cookies.split("=")[1];
					if (cookie != "" && cookie != '0' && cookie != 0 && cookie != null && cookie != "invalid"){
						
						$.get("rest/login/username/"+cookie, function(userName, status) {
							userName = userName.replace(/\b\w/g, l => l.toUpperCase());
							$(".userNameHeader").html(userName);
							$.holdReady(false); //set document ready
						});
					}else{
						$.holdReady(false); //set document ready
					}
					
				}
				$.holdReady(false); //set document ready
			});
}

function getRecipe(titleM2,object){
	var id = object.substr(6);	

	$(".h1Wrapper h1").html(titleM2);
	$(".recipeSteps h2").next("ul").html("Rezept wird geladen");
	$(".recipeSteps h2").next("ul").html("<div>Zutatenliste wird geladen</div>");

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

			$(".recipeImage").attr("src","http://192.168.3.3/pictures/"+id+".jpg");
			
			let recipeId = JSON.parse(recipe[0].id);
			checkIfItemAlreadyIsFavourized(recipeId);
		}
	});
}


