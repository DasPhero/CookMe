package services;

public class Person {

	private String userName;
	private String password;
	private int sQuestion;
	private String sAnswer;
	private int id;
	private String cookie;
	private String favourites;

	public Person() {
		super();
		this.userName = "";
		this.password = "";
		this.sQuestion = 0;
		this.sAnswer = "";
		this.id = 0;
	}

	public Person(int id, String userName) {
		super();
		this.userName = userName;
		this.password = "";
		this.id = id;
		this.sQuestion = 0;
		this.sAnswer = "";
	}

	public Person(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
		this.sQuestion = 0;
		this.sAnswer = "";
	}

	public Person(int id, String userName, String password, int sQuestion, String sAnswer) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.sQuestion = sQuestion;
		this.sAnswer = sAnswer;
	}

	
	
	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getPassowrd() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSAnswer() {
		return sAnswer;
	}

	public void setSAnwser(String sAnswer) {
		this.sAnswer = sAnswer;
	}

	public int getSQuestion() {
		return sQuestion;
	}

	public void setSQuestion(int sQuestion) {
		this.sQuestion = sQuestion;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFavourites() {
		return favourites;
	}

	public void setFavourites(String favourites) {
		this.favourites = favourites;
	}
}