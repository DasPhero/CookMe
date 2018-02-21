$.holdReady(true); //disable document == ready

const GET_NAV_TITLES = "-1";
const GET_CATEGORIES = "-2";
const RESOLVE_USERNAME = "-3";
$.get("rest/recipe/"+GET_CATEGORIES, function(data, status) { //get categories
	obj = JSON.parse(data);
	let sourceCode1 = "";
	
	obj.forEach(function(element) {
		sourceCode1 = sourceCode1 + "<li class=\"rCategory\" id=\"r"
				+ element["id"] + "\">" + element["name"]
				+ "</li><ul class=\"c" + element["id"] + "\"></ul>"; //set class to "c1", "c2" ..
	});
	$(".listWrapper ").html(sourceCode1);//override static code
		
	getTitles();//get titles of each recipe
	
});

function getTitles() {
	$.get("rest/recipe/"+GET_NAV_TITLES,// -1 = get all recipes
			function(data2, status) {
				recipes = JSON.parse(data2); //parse to JSON
				$(".rCategory").each(function(i, category) {
					let sourceCode2 = "";
					recipes.forEach(function(recipe) {
						if ($(category).attr('id') == "r"+ recipe["category"]) {
							sourceCode2 = sourceCode2
							+ "<li><input type=\"checkbox\" id=\"checkbox"
							+ recipe["id"]
							+ "\"/> <span class=\"listEntry\" id=\"recipe"
							+ recipe["id"]
							+ "\">"
							+ recipe["title"]
							+ "</span></li>";
						}
					});
					$(category).next(" ul").html(sourceCode2);
				});
				var cookies = document.cookie;
				if( cookies != ""){
					var cookie = cookies.split("=")[1];
					if (cookie != "" && cookie != '0' && cookie != 0 && cookie != null && cookie != "invalid"){
						
						$.get("rest/login/"+RESOLVE_USERNAME+"/"+cookie, function(userName, status) {
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
	function(data3, status) {
		if (data3.length > 1){
			recipe = JSON.parse(data3); //parse to JSON
			var sourceCode3="";
			var sourceCode4="";
			var sourceCode5="";
			var description =  JSON.parse(recipe[0]["description"]);
			var ingredients =  JSON.parse(recipe[0]["ingredients"]);
			var nutritionFacts =  JSON.parse(recipe[0]["nutritionfacts"]);
			description.forEach(function(step) {
				sourceCode3 = sourceCode3 + "<li>"
				+ step 
				+ "</li>";
			});
			
			ingredients.forEach(function(item) {
				sourceCode4 = sourceCode4 + "<div class=\"ingredientItem\"><span class=\"itemAmount\">"
				+ item[0]+ "</span></span class=\"recipeItem\">" +item[1]
				+ "</span></div>";
			});
			
			nutritionFacts.forEach(function(fact) {
				sourceCode5 = sourceCode5 + "<div>"
				+ fact
				+ "</div>";
			});
			
			$(".recipeSteps h2").next("ul").html(sourceCode3);
			$(".ingredientList").html(sourceCode4);
			$(".nutritionalFacts").html(sourceCode5);
		}
	});
}


