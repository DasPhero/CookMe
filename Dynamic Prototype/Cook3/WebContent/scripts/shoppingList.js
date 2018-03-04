tickAlreadySelectedItems = () => {
    let cookie = getAndValidateCookie();

	if(cookie){
        getSelectedItems(cookie);
	}
}

getSelectedItems = (cookie) => {
    $.get("rest/shoppingList/selectedItems/" + cookie,
    function(shoppingList){ 
        let shoppingListRecipeArray = JSON.parse(shoppingList);
        tickSelectedCheckBoxes(shoppingListRecipeArray, cookie);
    }
);
}

tickSelectedCheckBoxes = (shoppingListRecipes, cookie) => {
    shoppingListRecipes.forEach(listItem => {
        let listEntry = $("input." + listItem)[0];
        if(listEntry){
            listEntry.checked = true;
        }
    });

    let cachedList = JSON.parse(localStorage.getItem("cachedShoppingList"));
    if(!cachedList){
        cachedList = {"cookie": "undefined"};
    }
    if(cookie === cachedList.cookie){
        createHtmlList(cachedList.shoppingList);
    }else{
        createInitialShoppingList(shoppingListRecipes, cookie);
    }
}

async function createInitialShoppingList(shoppingListRecipes, cookie){
    $(".toBuyList ul").html("<li>Einkaufsliste wird erstellt...</li>");

    let ingredientsList = [];
    for (const recipeId of shoppingListRecipes) {
        let recipeIngredients = await fetchRecipeIngredients(recipeId);
        ingredientsList = ingredientsList.concat(recipeIngredients);
    }
    let compressedList = compressList(ingredientsList);
    
    localStorage.setItem("cachedShoppingList", JSON.stringify({ "cookie": cookie, "shoppingList": compressedList }));
    
    createHtmlList(compressedList);
}

fetchRecipeIngredients = (recipeId) => {
    return new Promise(function(resolve, reject){
        $.get("rest/recipe/" + recipeId,
        (recipe) => {
            let parsedRecipe = JSON.parse(recipe)[0];
            let recipeIngredients = JSON.parse(parsedRecipe.ingredients);
            resolve(recipeIngredients);
        });    
    })
}

compressList = (ingredientsList) => {
    let compressedList = [];
    ingredientsList.forEach(ingredient => {
        let itemIndex = getIndexOfObject(compressedList, ingredient);
        if(itemIndex != -1){
            compressedList[itemIndex].value += ingredient.value;
        }
        else{
            compressedList.push(ingredient);
        }
    });
    return compressedList;
}

createHtmlList = (ingredientList) => {
    let listCode = "";
    ingredientList.forEach(listElement => {
        let amount = listElement.value;
        amount = amount? amount : "";
        let unit = listElement.unit;
        let name = listElement.item;

        listCode += `<li>${amount} ${unit} ${name}</li>`
    });
    $(".toBuyList ul").html(listCode);
}

getIndexOfObject = (arrayOfObjects, objectToFind) => {
    let index = 0;
    for (const object of arrayOfObjects) {
        if(itemsAreEqual(object, objectToFind)){
            return index;
        }
        index++;
    }
    return -1;
}

itemsAreEqual = (itemA, itemB) => {
    let namesAreEqual = (itemA.item === itemB.item);
    let unitsAreEqual = (itemA.unit === itemB.unit);
    return namesAreEqual && unitsAreEqual;
}
    
updateSelectedItems = (checkBox) => {
    let cookie = getAndValidateCookie();
    let selectedId = $(checkBox)[0].classList[1];
    console.log(selectedId);
	if(cookie){
        $.get("rest/shoppingList/selectedItems/" + cookie,
        function(shoppingList){
            console.log(shoppingList);
            let shoppingListArray = JSON.parse(shoppingList);
            console.log(shoppingListArray);
            prepareShoppingList(cookie, shoppingListArray, selectedId);
        }
    );
}
}

prepareShoppingList = (cookie, shoppingList, selectedId) => {
    let indexOfSelectedId = shoppingList.indexOf(selectedId)
    let idIsAlreadySelected = indexOfSelectedId !== -1;
    
    if(idIsAlreadySelected){
        shoppingList.splice(indexOfSelectedId, 1);
    }
    else{
        shoppingList.push(selectedId);
    }
    commitNewList(cookie, shoppingList);
}

commitNewList = (cookie, updatedList) => {
    let newList =  "[" + updatedList.toString() + "]";
    let data = { "cookie": cookie, "selectedrecipes": newList };
    $.post("rest/shoppingList/", data);
}