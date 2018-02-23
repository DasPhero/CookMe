addToFavourites = () => {
	let activeCookies = document.cookie;
	let relevantCookieIndex = activeCookies.indexOf("uuid=");

	if(relevantCookieIndex == -1){
		goToLoginPage();
	}

	const cookieLength = 20;
	const keyNameLength = 5;
	let cookieValue = activeCookies.substr(relevantCookieIndex + keyNameLength, cookieLength);

	if(cookieValue === "invalid"){
		goToLoginPage();
	}
	updateFavourites(cookieValue);
}

goToLoginPage = () => {
	window.location.assign('login.html');
}

updateFavourites = (cookieValue) => {
	$.get("rest/favourization/favourizedItems/" + cookieValue,
	function(favouritesArray, status){ modifiyFavourites(JSON.parse(favouritesArray), cookieValue); }
	);
}

modifiyFavourites = (favouritesData, cookieValue) => {
	let selectedRecipe = $(".h1Wrapper h1").text();
	if(!selectedRecipe){ return; }
	$.get("rest/favourization/recipeId/" + selectedRecipe,
		  function(recipeId){ addRecipeIdToFavouritesArray(recipeId, favouritesData, cookieValue) } 
		)
}

addRecipeIdToFavouritesArray = (recipeId, favouritesData, cookieValue) => {
	let recipeIdInt = parseInt(recipeId);
	if(favouritesData.indexOf(recipeIdInt) == -1){
		favouritesData.push(recipeIdInt);
		commitFavourites(favouritesData, cookieValue);
	}
}

commitFavourites = (favouritesData, cookieValue) => {
	let newString =  "[" + favouritesData.toString() + "]";
	let data = { "cookie": cookieValue, "favourites": newString };
	$.post("rest/favourization", data)
}