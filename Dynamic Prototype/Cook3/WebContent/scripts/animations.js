$(document).ready(function() {
	//slider in nav bar (categories)
	$(".listWrapper li").click(function() {
		$(this).next('ul').slideToggle();
	});
	
	//select single recipe
	$(".listEntry").click(function() {
		getRecipe($(this).text(),$(this).attr('id'));
	});	
});	