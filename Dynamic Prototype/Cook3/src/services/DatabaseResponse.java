package services;

import java.util.ArrayList;
import java.util.List;

public class DatabaseResponse {

	private int type;

	// recipe == 0
	private List<String> title;
	private List<Integer> author;
	private List<String> description;
	private List<String> ingrements;

	// both
	private List<Integer> id;

	// person == 1
	private List<String> userName;
	private List<String> password;
	private List<Integer> sQuestion;
	private List<String> sAnswer;

	public DatabaseResponse() {
		this.type = 0;

		this.title = new ArrayList<String>();
		this.author = new ArrayList<Integer>();
		this.description = new ArrayList<String>();
		this.ingrements = new ArrayList<String>();

		this.userName = new ArrayList<String>();
		this.password = new ArrayList<String>();
		this.sQuestion = new ArrayList<Integer>();
		this.sAnswer = new ArrayList<String>();
		this.id = new ArrayList<Integer>();
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
			r.setIngrements(this.ingrements.get(i));
			list.add(r);
		}
		return list;
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

	public List<String> getIngrements() {
		return ingrements;
	}

	public void setIngrements(List<String> ingrements) {
		this.ingrements = ingrements;
	}

	public void addIngrements(String ingrements) {
		this.ingrements.add(ingrements);
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
}