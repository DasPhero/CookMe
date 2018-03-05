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
        tickSelectedCheckBoxes(shoppingListRecipeArray);
    }
);
}

tickSelectedCheckBoxes = (shoppingListRecipes) => {
    shoppingListRecipes.forEach(listItem => {
        let listEntry = $("input." + listItem)[0];
        if(listEntry){
            listEntry.checked = true;
        }
    });
    createInitialShoppingList(shoppingListRecipes);
}

async function createInitialShoppingList(shoppingListRecipes){
    $(".toBuyList ul").html("<li>Einkaufsliste wird erstellt...</li>");
    localStorage.setItem("shoppingList", "[]");

    let ingredientsList = [];
    for (const recipeId of shoppingListRecipes) {
        let recipeIngredients = await fetchRecipeIngredients(recipeId);
        ingredientsList = ingredientsList.concat(recipeIngredients);
    }
    let compressedList = compressList(ingredientsList);
    localStorage.setItem("shoppingList", JSON.stringify(compressedList));

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

        listCode += `<li>${amount} ${unit} ${name}</li>`;
    });
    $(".toBuyList ul").html(listCode);
    return listCode;
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
    let checkBoxToggledOn = $(checkBox)[0].checked == true;
	if(cookie){
        $.get("rest/shoppingList/selectedItems/" + cookie,
        function(shoppingList){
            let shoppingListArray = JSON.parse(shoppingList);
            prepareShoppingList(cookie, shoppingListArray, selectedId, checkBoxToggledOn);
        }
        );
    }
}

prepareShoppingList = (cookie, shoppingList, selectedId, checkBoxToggledOn) => {
    let recipeIndex = indexOfSelectedId(shoppingList, selectedId);
    let idIsAlreadySelected = (recipeIndex != -1);
    if(idIsAlreadySelected && !checkBoxToggledOn){
        shoppingList.splice(recipeIndex, 1);
    }
    else if(checkBoxToggledOn){
        shoppingList.push(selectedId);
    }
    let addItem = !idIsAlreadySelected && checkBoxToggledOn;
    updateShoppingListIncrementally(selectedId, addItem);
    commitNewList(cookie, shoppingList);
}

indexOfSelectedId = (array, item) => {
    let index = 0;
    for(let i = 0; i < array.length; i++){
        if(array[i] == item){
            return i;
        }
    };
    return -1;
} 

updateShoppingListIncrementally = (recipeId, addItem) => {
    $.get("rest/recipe/" + recipeId,
    (recipe) => {
        let parsedRecipe = JSON.parse(recipe)[0];
        let recipeIngredients = JSON.parse(parsedRecipe.ingredients);
        let currentList = JSON.parse(localStorage.getItem("shoppingList"));

        if(addItem){
            currentList = currentList.concat(recipeIngredients);
            let compressedList = compressList(currentList);
            localStorage.setItem("shoppingList", JSON.stringify(compressedList));
            createHtmlList(compressedList);
        }
        else{
            let reducedList = removeItemsFromList(currentList, recipeIngredients);
            localStorage.setItem("shoppingList", JSON.stringify(reducedList));
            createHtmlList(reducedList);
        }
    });
}

removeItemsFromList = (sourceArray, itemsToRemove) => {
    itemsToRemove.forEach(ingredient => {
        let itemIndex = getIndexOfObject(sourceArray, ingredient);
        if(itemIndex != -1){
            sourceArray[itemIndex].value -= ingredient.value;
            if(sourceArray[itemIndex].value == 0){
                sourceArray.splice(itemIndex, 1);
            }
        }
    });
    return sourceArray;
}



commitNewList = (cookie, updatedList) => {
    let newList =  "[" + updatedList.toString() + "]";
    let data = { "cookie": cookie, "selectedrecipes": newList };
    $.post("rest/shoppingList/", data);
}