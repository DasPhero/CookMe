$.holdReady(true); //disable document == ready

const GET_CATEGORIES = "-2";
const GET_NAV_TITLES = "-1";
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
				$.holdReady(false); //set document ready
			});
}

function getRecipe(titleM2,object){
	var id = object.substr(6);	
	$(".h1Wrapper h1").html(titleM2);
	$(".recipeSteps h2").next("ul").html("Rezept wird geladen");
	$.get("rest/recipe/" + id,// -1 = get all recipes
	function(data3, status) {
		if (data3.length > 1){
			recipe = JSON.parse(data3); //parse to JSON
			var sourceCode3="";
			var description =  JSON.parse(recipe[0]["description"]);
			description.forEach(function(step) {
				sourceCode3 = sourceCode3 + "<li>"
				+ step 
				+ "</li>";
			});
			
			$(".recipeSteps h2").next("ul").html(sourceCode3);
		}
	});
}


