package hu.eisys.david.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DbChange implements Step {

	private String dbName;
	private String userName;
	private String password;
	private String comment;

	public DbChange() {
	}

	public DbChange(String dbName, String userName, String password) {
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
			return comment;
	}

	@JsonIgnore
	public StepNames getType() {
		return StepNames.DBCHANGE;
	}

	public void setDataBase(String dbName, String userName, String password) {
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
	}

	@JsonIgnore
	public String getShowElement() {
		return userName + "@" + dbName;
	}
}
