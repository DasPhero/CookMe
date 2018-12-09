addToFavourites = () => {
	let cookie = getAndValidateCookie();

	if(!cookie){
		goToLoginPage();
	}
	updateFavourites(cookie);
}

goToLoginPage = () => {
	window.location.assign('login.html');
}

updateFavourites = (cookieValue) => {
	$.get("rest/favourization/favourizedItems/" + cookieValue,
	function(favouritesArray){ modifiyFavourites(JSON.parse(favouritesArray), cookieValue); }
	);
}

modifiyFavourites = (favouritesData, cookieValue) => {
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

	$.post("rest/favourization", data);
	toggleFavourizationButtonState();
}

checkIfItemAlreadyIsFavourized = (recipeId) => {
	let cookie = getAndValidateCookie();

	if(!cookie){
		return;
	}
	$.get("rest/favourization/favourizedItems/" + cookie,
	function(favouritesArray){ 
		if(favouritesArray.indexOf(recipeId) !== -1){
			changeButtonToRemoveButton();
		}
		else{
			changeButtonToAddButton();
		}
	 }
	);
}

changeButtonToRemoveButton = () => {
	$("button[name='addToFavourites']").attr("name", "removeFromFavourites").text("Aus Favoriten entfernen");
}

changeButtonToAddButton = () => {
	$("button[name='removeFromFavourites']").attr("name", "addToFavourites").text("Zu Favoriten hinzufügen");
}

toggleFavourizationButtonState = () => {
	if($("button[name='addToFavourites']").length === 0){
		changeButtonToAddButton();
	}
	else{
		changeButtonToRemoveButton();
	}
}