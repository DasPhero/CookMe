$(document).ready(function() {
	//slider in nav bar (categories)
	$(".listWrapper li").click(function() {
		$(this).next('ul').slideToggle();
	});
	
	//select single recipe
	$(".listEntry").click(function() {
		getRecipe($(this).text(),$(this).attr('id'));
	});	

	$.fn.toggleCheckbox = function() {
		this.attr('checked', !this.attr('checked'));
		}

	function checkURLRecipeID (){
		let url1 = new URL(window.location.href);
		let urlID = "123456" + url1.searchParams.get("id");
		if (urlID == "123456null"){
			return;
		}
		let recipeName= $("#recipe"+url1.searchParams.get("id")).text();
		getRecipe(recipeName,urlID);
	}

	checkURLRecipeID();

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
});	