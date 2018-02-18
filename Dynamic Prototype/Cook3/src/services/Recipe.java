package services;

public class Recipe{

	private String title;
	private int author;
	private String description;
	private String ingrements;
	private int id;
	private int categoryId;

	public Recipe() {
		this.title = "";
		this.author = 0;
		this.description = "";
		this.ingrements = "";
		this.id = 0;
		this.categoryId=0;
	}
	
	public String toString() {
		String text= "Titel: "+ title;
		text = text + "\nAuthor: " + author;
		text = text + "\nBeschreibung: " + description;
		text = text + "\nZutaten: " + ingrements;
		text = text + "\nID: " + id;
		text = text + "\nKategorie ID: " + categoryId;
		return text;
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

	public String getIngrements() {
		return ingrements;
	}

	public void setIngrements(String ingrements) {
		this.ingrements = ingrements;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
