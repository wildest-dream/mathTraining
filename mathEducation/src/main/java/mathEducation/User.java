package mathEducation;


public class User {
	private String user_id;
	private String password;
	private String error_rates;
	private String wrong_answers;
	private String recommended_questions;
	private String answered_questions;
	private String average_time_consumption;
	private String time_spent;
	public User() {
		super();
	}
	public User(String userid,String pwd) {
		super();
		this.user_id=userid;
		this.password=pwd;
		this.error_rates=this.wrong_answers=this.recommended_questions=this.answered_questions=this.average_time_consumption=this.time_spent=null;
	}
	public String getID() {
		return this.user_id;
	}
	public String getPWD() {
		return this.password;
	}
	public String getError_rates() {
		return this.error_rates;
	}
	public void setError_rates(String ErrorRates) {
		this.error_rates=ErrorRates;
	}
	public String getWrong_answers() {
		return this.wrong_answers;
	}
	public void setWrong_answers(String WrongAnswers) {
		this.wrong_answers=WrongAnswers;
	}
	public String getRecommended_questions() {
		return this.recommended_questions;
	}
	public void setRecommended_questions(String RecommendedQuestions) {
		this.recommended_questions=RecommendedQuestions;
	}
	public String getAnswered_questions() {
		return this.answered_questions;
	}
	public void setAnswered_questions(String AnsweredQuestions) {
		this.answered_questions=AnsweredQuestions;
	}
	public String getAverage_time_consumption() {
		return this.average_time_consumption;
	}
	public void setAverage_time_consumption(String AverageTimeConsumption) {
		this.average_time_consumption=AverageTimeConsumption;
	}
	public String getTime_spent() {
		return this.time_spent;
	}
	public void setTime_spent(String TimeSpent) {
		this.time_spent=TimeSpent;
	}
}
