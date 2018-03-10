$(document).ready(function() {
	$(".searchListTextBox").keyup(function(event) { //click at enter
		event.preventDefault();
		if (event.keyCode === 13) {searchRecipe();
	}});
});
function searchRecipe(){
	let searchString = $(".searchListTextBox").val();
	
	searchString = searchString.replace(" ","");
	if (searchString == ""){
		showAllRecipesInNav();
		return;
	}
	
	let cArray =[];
	$(".rCategory").each(function(i, category) {
		cArray.push($(category).attr("id"));
	});
	$(".listEntry").each(function(i, title) {
		$(title).parent().hide();
		if ($(title).text().toLowerCase().includes(searchString.toLowerCase())){
			let catClass ="r" + $(title).parent().parent().attr("class").substring(1);
			$(title).parent().show();
			let index = cArray.indexOf(catClass);
			if (index >= 0){
				cArray.splice(index,1);
			}
		}
	});	
	cArray.forEach(function(element) {
		$("#" + element).hide();
	});
}

function showAllRecipesInNav(){
	$(".rCategory").each(function(i, category) {
		$(category).show();
	});
	$(".listEntry").each(function(i, title) {
		$(title).parent().show();
	});
}