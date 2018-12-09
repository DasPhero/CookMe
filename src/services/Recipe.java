package services;

import java.util.ArrayList;
import java.util.List;

public class Recipe{

	private String title;
	private int author;
	private String description;
	private List<String> ingredientsItem;
	private List<Integer> ingredientsValue;
	private List<String> ingredientsUnit;
	private String nutritionFacts;
	private int id;
	private int categoryId;

	public Recipe() {
		this.title = "";
		this.author = 0;
		this.description = "";
		this.ingredientsItem = new ArrayList<String>();
		this.ingredientsValue = new ArrayList<Integer>();
		this.ingredientsUnit = new ArrayList<String>();
		this.id = 0;
		this.categoryId=0;
		this.nutritionFacts = "";
	}
	
	public String toString() {
		String text= "Titel: "+ title;
		text = text + "\nAuthor: " + author;
		text = text + "\nBeschreibung: " + description;
		text = text + "\nID: " + id;
		text = text + "\nKategorie ID: " + categoryId;
		text = text + "\nNährwertangaben: " + nutritionFacts;
		return text;
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

	public String getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(String nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
