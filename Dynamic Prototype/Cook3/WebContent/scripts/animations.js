$(document).ready(function() {
	$(".listWrapper li").click(function() {
		$(this).next('ul').slideToggle();
	});
});
		