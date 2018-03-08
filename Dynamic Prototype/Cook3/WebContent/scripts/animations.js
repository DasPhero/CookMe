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