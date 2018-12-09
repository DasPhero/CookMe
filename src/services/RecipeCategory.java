package services;

public class RecipeCategory {
	private String categoryName;
	private int id;
	
	public RecipeCategory() {
		this.id=0;
		this.categoryName="";
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
