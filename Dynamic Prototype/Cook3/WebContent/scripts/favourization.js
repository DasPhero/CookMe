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

modifiyFavourites = (favouritesData, cookieValue, itemIsExistant) => {
	let selectedRecipe = $(".h1Wrapper h1").text();
	if(!selectedRecipe){ return; }
	$.get("rest/favourization/recipeId/" + selectedRecipe,
		  function(recipeId){ 
			let recipeIdInt = parseInt(recipeId);
			let itemIsExistant = favouritesData.indexOf(recipeIdInt) !== -1;
			if(itemIsExistant){
				removeRecipeFromFavouritesArray(recipeIdInt, favouritesData, cookieValue);
			}
			else {
				addRecipeIdToFavouritesArray(recipeIdInt, favouritesData, cookieValue) } 
			}
		)
}

addRecipeIdToFavouritesArray = (recipeId, favouritesData, cookieValue) => {
	favouritesData.push(recipeId);
	commitFavourites(favouritesData, cookieValue);
}

removeRecipeFromFavouritesArray = (recipeId, favouritesData, cookieValue) => {
	let recipePosition = favouritesData.indexOf(recipeId);
	favouritesData.splice(recipePosition, 1);
	commitFavourites(favouritesData, cookieValue);
}

commitFavourites = (favouritesData, cookieValue) => {
	let newString =  "[" + favouritesData.toString() + "]";
	let data = { "cookie": cookieValue, "favourites": newString };
	$.post("rest/favourization", data)
}