tickAlreadySelectedItems = () => {
    let cookie = getAndValidateCookie();

	if(cookie){
        getSelectedItems(cookie);
	}
}

getSelectedItems = (cookie) => {
    $.get("rest/shoppingList/selectedItems/" + cookie,
	    function(shoppingListArray){ 
            updateCheckBoxes(JSON.parse(shoppingListArray)); 
        }
	);
}

updateCheckBoxes = (shoppingListArray) => {
    shoppingListArray.forEach(listItem => {
        let listEntry = $("input." + listItem)[0];
        if(listEntry){
            listEntry.checked = true;
        }
    });
}