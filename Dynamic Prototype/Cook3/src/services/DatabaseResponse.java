package services;

import java.util.ArrayList;
import java.util.List;

public class DatabaseResponse {

	private int type;
	
	// recipe == 0
	private List<String> title;
	private List<Integer> author;
	private List<String> description;
	private List<String> nutritionFacts;
	private List<String> ingredientsItem;
	private List<Integer> ingredientsValue;
	private List<String> ingredientsUnit;

	private List<List<String>> ingredientsItemList;
	private List<List<Integer>> ingredientsValueList;
	private List<List<String>> ingredientsUnitList;
	
	private List<Integer> categoryId;
	private Integer recipeId;
	
	// all
	private List<Integer> id;

	// person == 1
	private List<String> userName;
	private List<String> password;
	private List<Integer> sQuestion;
	private List<String> sAnswer;
	private List<String> cookie;
	private String favourites;
	private String selection;
	private Integer userId;
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer i) {
		this.userId = i;
	}

	//item
	private List<Integer> itemCount;
	// + title
	
	
	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	// category == 3
	private List<String> categoryName;

	public DatabaseResponse() {
		this.type = 0;
		this.id = new ArrayList<Integer>();

		this.title = new ArrayList<String>();
		this.author = new ArrayList<Integer>();
		this.description = new ArrayList<String>();
		this.nutritionFacts = new ArrayList<String>();
		this.ingredientsItem = new ArrayList<String>();
		this.ingredientsUnit = new ArrayList<String>();
		this.ingredientsValue = new ArrayList<Integer>();
		
		this.ingredientsItemList = new ArrayList<List<String>>();
		this.ingredientsUnitList = new ArrayList<List<String>>();
		this.ingredientsValueList = new ArrayList<List<Integer>>();
		
		this.categoryId = new ArrayList<Integer>();

		this.userName = new ArrayList<String>();
		this.password = new ArrayList<String>();
		this.sQuestion = new ArrayList<Integer>();
		this.sAnswer = new ArrayList<String>();
		this.cookie = new ArrayList<String>();
		this.favourites = "";
		this.selection = "";
		this.userId = 0;
		this.username = "";
		
		this.itemCount = new ArrayList<Integer>();

		this.categoryName = new ArrayList<String>();
	}

	public List<Person> toPersonList() {
		List<Person> list = new ArrayList<Person>();
		Person p = new Person();
		for (int i = 0; i < this.id.size(); i++) {
			p.setId(this.id.get(i));
			p.setUserName(this.userName.get(i));
			p.setSQuestion(this.sQuestion.get(i));
			p.setSAnwser(this.sAnswer.get(i));
			p.setPassword(this.password.get(i));
			p.setCookie(this.cookie.get(i));
			list.add(p);
		}
		return list;
	}

	public List<Recipe> toRecipeList() {
		List<Recipe> list = new ArrayList<Recipe>();
		for (int i = 0; i < this.id.size(); i++) {
			Recipe r = new Recipe();
			r.setTitle(this.title.get(i));
			r.setId(this.id.get(i));
			r.setAuthor(this.author.get(i));
			r.setDescription(this.description.get(i));
			if(0 != this.ingredientsItemList.size()) {
				r.setIngredientsItem(this.ingredientsItemList.get(i));
				r.setIngredientsUnit(this.ingredientsUnitList.get(i));
				r.setIngredientsValue(this.ingredientsValueList.get(i));
			}
			r.setNutritionFacts(this.nutritionFacts.get(i));
			r.setCategoryId(this.categoryId.get(i));
			list.add(r);
		}
		return list;
	}
	
	public List<RecipeCategory> toRecipeCategoryList() {
		List<RecipeCategory> list = new ArrayList<RecipeCategory>();
		for (int i = 0; i < this.id.size(); i++) {
			RecipeCategory r = new RecipeCategory();
			r.setId(this.id.get(i));
			r.setCategoryName(this.categoryName.get(i));
			list.add(r);
		}
		return list;
	}
	
	public String toFavouritesString() {
		return this.favourites;
	}
	
	public String toSelectionString() {
		return this.selection;
	}

	public Integer toRecipeId() {
		return this.id.get(0);
	}

	public void addItemCount(int item) {
		this.itemCount.add(item);
	}
	
	public List<Integer> getItemCount() {
		return itemCount;
	}

	public void setItemCount(List<Integer> itemCount) {
		this.itemCount = itemCount;
	}

	public void addIngredientsItemList(List<String> itemList) {
		this.ingredientsItemList.add(itemList);
	}
	
	public List<List<String>> getIngredientsItemList() {
		return ingredientsItemList;
	}

	public void setIngredientsItemList(List<List<String>> ingredientsItemList) {
		this.ingredientsItemList = ingredientsItemList;
	}

	public void addIngredientsValueList(List<Integer> valueList) {
		this.ingredientsValueList.add(valueList);
	}
	
	public List<List<Integer>> getIngredientsValueList() {
		return ingredientsValueList;
	}

	public void setIngredientsValueList(List<List<Integer>> ingredientsValueList) {
		this.ingredientsValueList = ingredientsValueList;
	}

	public void addIngredientsUnitList(List<String> unitList) {
		this.ingredientsUnitList.add(unitList);
	}
	
	public List<List<String>> getIngredientsUnitList() {
		return ingredientsUnitList;
	}

	public void setIngredientsUnitList(List<List<String>> ingredientsUnitList) {
		this.ingredientsUnitList = ingredientsUnitList;
	}

	public void addIngredientsItem(String item) {
		this.ingredientsItem.add(item);
	}
	
	public List<String> getIngredientsItem() {
		return ingredientsItem;
	}

	public void setIngredientsItem(List<String> ingredientsItem) {
		this.ingredientsItem = ingredientsItem;
	}

	public void addIngredientsValue(int value) {
		this.ingredientsValue.add(value);
	}
	
	public List<Integer> getIngredientsValue() {
		return ingredientsValue;
	}

	public void setIngredientsValue(List<Integer> ingredientsValue) {
		this.ingredientsValue = ingredientsValue;
	}

	public void addIngredientsUnit(String unit) {
		this.ingredientsUnit.add(unit);
	}
	
	public List<String> getIngredientsUnit() {
		return ingredientsUnit;
	}

	public void setIngredientsUnit(List<String> ingredientsUnit) {
		this.ingredientsUnit = ingredientsUnit;
	}

	public void addNutritionFacts(String nutritionFacts) {
		this.nutritionFacts.add(nutritionFacts);
	}
	
	public List<String> getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(List<String> nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
	}

	public void addCookie(String cookie) {
		this.cookie.add(cookie);
	}
	
	public List<String> getCookie() {
		return cookie;
	}

	public void setCookie(List<String> cookie) {
		this.cookie = cookie;
	}

	public List<Integer> getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(List<Integer> categoryId) {
		this.categoryId = categoryId;
	}

	public List<String> getCategoryName() {
		return categoryName;
	}
	
	public void addCategoryId(int categoryId) {
		this.categoryId.add(categoryId);
	}

	public void setCategoryName(List<String> categoryName) {
		this.categoryName = categoryName;
	}

	public void addCategoryName(String categoryName) {
		this.categoryName.add(categoryName);
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public DatabaseResponse(int type) {
		this();
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

	public void addTitle(String title) {
		this.title.add(title);
	}

	public List<Integer> getAuthor() {
		return author;
	}

	public void setAuthor(List<Integer> author) {
		this.author = author;
	}

	public void addAuthor(int author) {
		this.author.add(author);
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public void addDescription(String description) {
		this.description.add(description);
	}

	public List<String> getUserName() {
		return userName;
	}

	public void setUserName(List<String> userName) {
		this.userName = userName;
	}

	public void addUserName(String userName) {
		this.userName.add(userName);
	}

	public List<String> getPassword() {
		return password;
	}

	public void setPassword(List<String> password) {
		this.password = password;
	}

	public void addPassword(String password) {
		this.password.add(password);
	}

	public List<Integer> getsQuestion() {
		return sQuestion;
	}

	public void setsQuestion(List<Integer> sQuestion) {
		this.sQuestion = sQuestion;
	}

	public void addSQuestion(int sQuestion) {
		this.sQuestion.add(sQuestion);
	}

	public List<String> getsAnswer() {
		return sAnswer;
	}

	public void setsAnswer(List<String> sAnswer) {
		this.sAnswer = sAnswer;
	}

	public void addSAnswer(String sAnswer) {
		this.sAnswer.add(sAnswer);
	}

	public List<Integer> getId() {
		return id;
	}

	public void setId(List<Integer> id) {
		this.id = id;
	}

	public void addId(int id) {
		this.id.add(id);
	}

	public String getFavourites() {
		return favourites;
	}

	public void setFavourites(String favourites) {
		this.favourites = favourites;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}
}
