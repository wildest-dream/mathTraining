package mathEducation;

public interface UserDAO {
	User getUserById(String userID);//invoke when load home page
	int[] getLevels(String error_rates,String time_consumption);//invoke when load home page
	void updateAnsweredQuestions(String questions_to_add,String answered_questions,String wrong_answers,
			String error_rates,String time_consumption,String time_spent,String userID);
	void updateRecommendedQuestions(String questions_to_add,String if_needed,String answered_questions,String recommended_questions,
			String wrong_answers,String error_rates,String time_consumption,String time_spent,String userID);
	void updateErrorReview(String questions_to_set,String wrong_answers,String answered_questions,String time_spent,String userID);
}
