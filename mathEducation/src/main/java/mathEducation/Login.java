package mathEducation;

public class Login {
	private String userid;
	private String password;
	public Login() {
		super();
	}
	public Login(String userID,String pwd) {
		super();
		this.userid=userID;
		this.password=pwd;
	}
	public String getUserid() {
		return this.userid;
	}
	public void setUserid(String userID) {
		this.userid = userID;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String pwd) {
		this.password =pwd;
	}
}
