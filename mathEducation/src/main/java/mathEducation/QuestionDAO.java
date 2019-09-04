package mathEducation;

import java.util.List;

public interface QuestionDAO {
	List<Question> getQuestionsByID(String[] questionIDs);//invokes when load training page
	void updateAnsweredRight(int time,String questionCategory,String questionID,String averageTime,String errorRate);//invoke when finish answering a question correctly
	void updateAnsweredWrong(int time,String wrongAnswer,String questionCategory,String questionID,int type,String averageTime,
			String errorRate,String errorRandomness);//invoke when finish answering a question incorrectly
	void updateFeedback(String[] questionIDs,String[] new_feedbackRates,String[] old_feedbackRates);//invoke when finishing rating questions in summary
	String getAnalysis(String questionCategory,String questionID);//invoke when displaying summary
	List<Question> getRecommendedQuestionsByID(String answered_questions,String user_id);
	List<Question> getWrongQuestionsByID(String question_category_and_IDs, String wrong_answers,String user_id);
}
