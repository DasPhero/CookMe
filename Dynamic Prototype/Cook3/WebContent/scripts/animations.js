$(document).ready(function() {
	$(".recipeList ul li").click(function() {
		$(this).next('ul').slideToggle();
	});
});
