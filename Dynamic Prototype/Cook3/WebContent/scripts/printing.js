let parsedIngredientArray = JSON.parse(localStorage.getItem("shoppingList"));

$(".groceryList").html(createHtmlList(parsedIngredientArray));

window.onafterprint = function(){
	   let dummyWindow = window.open("", "_self");
	   dummyWindow.close();
}
setTimeout(function () { window.print(); }, 0);