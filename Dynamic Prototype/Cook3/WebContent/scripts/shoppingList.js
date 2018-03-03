tickAlreadySelectedItems = () => {
    let cookie = getAndValidateCookie();

	if(cookie){
        let shoppingList = tickSelectedCheckBoxes(cookie);
	}
}

tickSelectedCheckBoxes = (cookie) => {
    $.get("rest/shoppingList/selectedItems/" + cookie,
    function(shoppingList){ 
            let shoppingListArray = JSON.parse(shoppingList);
            getSelectedItems(shoppingListArray);
        }
	);
}

getSelectedItems = (shoppingListItems) => {
    shoppingListItems.forEach(listItem => {
        let listEntry = $("input." + listItem)[0];
        if(listEntry){
            listEntry.checked = true;
        }
    });
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