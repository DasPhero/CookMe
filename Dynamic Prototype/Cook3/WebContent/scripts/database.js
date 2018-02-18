$.holdReady(true); //disable document == ready



$.get("rest/recipe/-3", function(data, status) { //get categories
	obj = JSON.parse(data);
	var quellcode = "";
	
	obj.forEach(function(element) {
		quellcode = quellcode + "<li class=\"rCategory\" id=\"r"
				+ element["id"] + "\">" + element["name"]
				+ "</li><ul class=\"c" + element["id"] + "\"></ul>"; //set class to "c1", "c2" ..

	});
	$(".listWrapper ").html(quellcode);//override static code
	getTitles();//get titles of each recipe
	
});

function getTitles() {
	$.get("rest/recipe/-1",// -1 = get all recipes
			function(data2, status) {
				recipes = JSON.parse(data2); //parse to JSON
				$(".rCategory").each(function(i, category) {
					var quellcode = "";
					recipes.forEach(function(recipe) {
						if ($(category).attr('id') == "r"+ recipe["category"]) {
							quellcode = quellcode
							+ "<li><input type=\"checkbox\" id=\"checkbox"
							+ recipe["id"]
							+ "\"/> <span class=\"listEntry\" id=\"recipe"
							+ recipe["id"]
							+ "\">"
							+ recipe["title"]
							+ "</span></li>";
						}
					});
					$(category).next(" ul").html(quellcode);
				});
				$.holdReady(false); //set document ready
			});
}

function getRecipe(object){
	var id = object.substr(6);	
	//alert("a"+id);
	$.get("rest/recipe/" + id,// -1 = get all recipes
	function(data3, status) {
		//alert(data3);
		if (data3.length > 1){
			recipe = JSON.parse(data3); //parse to JSON
			//alert(recipe[0]["description"]  + "2");
			$(".recipeSteps h2 ").next('ul').text(recipe[0]["description"]);
		}
	});
}


