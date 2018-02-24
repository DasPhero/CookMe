addToFavourites = () => {
	let cookie = getAndValidateCookie();

	if(!cookie){
		goToLoginPage();
	}
	updateFavourites(cookieValue);
}

getAndValidateCookie = () => {
	let activeCookies = document.cookie;
	let relevantCookieIndex = activeCookies.indexOf("uuid=");

	if(relevantCookieIndex == -1){
		return false;
	}

	const cookieLength = 20;
	const keyNameLength = 5;
	let cookieValue = activeCookies.substr(relevantCookieIndex + keyNameLength, cookieLength);

	if(cookieValue === "invalid"){
		return false;
	}

	return cookieValue;
}

goToLoginPage = () => {
	window.location.assign('login.html');
}

updateFavourites = (cookieValue) => {
	$.get("rest/favourization/favourizedItems/" + cookieValue,
	function(favouritesArray){ modifiyFavourites(JSON.parse(favouritesArray), cookieValue); }
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

checkIfItemAlreadyIsFavourized = (recipeId) => {
	let cookie = getAndValidateCookie();

	if(!cookie){
		return;
	}
	$.get("rest/favourization/favourizedItems/" + cookie,
	function(favouritesArray){ 
		console.log("asd" + recipeId, favouritesArray);
		if(favouritesArray.indexOf(recipeId) !== -1){
			console.log("in");
			changeButtonToRemoveButton();
		}
		else{
			console.log("out");
			changeButtonToAddButton();
		}
	 }
	)
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