$(document).ready(function() {
	//slider in nav bar (categories)
	$(".listWrapper > li").click(function() {
		
		let headerText = $(this).text();
		let firstCharacter = headerText.slice(0,1);
		
		$(this).next('ul').slideToggle();		
		headerText = firstCharacter == "▿"? headerText.replace("▿","▹") : headerText.replace("▹","▿");
		$(this).text(headerText);
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
});	

markAsActive = (listEntry) => {
	$(".listEntry").each((i, entry) => {entry.className = "listEntry"});
	listEntry.className = "listEntry active";
}

function getImpressum(){
	let pathname = window.location.pathname;
	let path2=pathname.substring(0,pathname.length-10)+"impressum";
	alert(path2);
	window.location.assign(path2);
}