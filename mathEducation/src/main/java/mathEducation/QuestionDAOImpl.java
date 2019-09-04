package mathEducation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class QuestionDAOImpl implements QuestionDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public List<Question> getQuestionsByID(String[] questionIDs) {//questionID.length()==10,IDs answered in each category
		// TODO Auto-generated method stub
		List<Question> questions=new ArrayList<Question>();
		for(int i=0;i<10;i++) {
			String sql="select * from Question where category like ? ";
			if(questionIDs[i].equals("")) {
				sql+="limit 1";
			}else {
				sql+="and id not in (";
				for(int j=0;j<questionIDs[i].length()/3;j++) {
					sql+="\""+questionIDs[i].substring(j*3, j*3+3)+"\",";
				}
				sql=sql.substring(0, sql.length()-1);
				sql+=") limit 1";
			}
			Question question;
			try {
				question=jdbcTemplate.queryForObject(sql, new Object[] {Integer.toString(i)+"%"}, new RowMapper<Question>() {

					@Override
					public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						Question newQuestion=new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),
								rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(13));
						newQuestion.setAverage_time(rs.getString(10));
						newQuestion.setError_rate(rs.getString(11));
						newQuestion.setError_randomness(rs.getString(12));
						newQuestion.setFeedback_rate(rs.getString(14));
						return newQuestion;
					}
					
				});
			}catch(EmptyResultDataAccessException e) {
				question=null;
			}
			if(question!=null) {
				questions.add(question);
			}
		}
		return questions.isEmpty()?null:questions;
	}

	@Override
	public void updateAnsweredRight(int time,String questionCategory, String questionID, String averageTime, String errorRate) {
		// TODO Auto-generated method stub
		String newAvgTime;
		String newErrRate;
		if(averageTime==null) {
			newAvgTime=Integer.toString(time)+"/1";
		}else {
			String[] avg_time=averageTime.split("/");
			int avgT=Integer.parseInt(avg_time[0]);
			int times=Integer.parseInt(avg_time[1]);
			String newT=times<9999?Integer.toString((avgT*times+time)/(times+1)):avg_time[0];
			String newTimes=times<9999?Integer.toString(times+1):avg_time[1];
			newAvgTime=newT+"/"+newTimes;
		}
		if(errorRate==null) {
			newErrRate="0/1";
		}else {
			System.out.println(errorRate.length());
			if(errorRate==null) {System.out.println(errorRate);}
			String[] err_rate=errorRate.split("/");
			int Wtimes=Integer.parseInt(err_rate[0]);
			int Rtimes=Integer.parseInt(err_rate[1]);
			String newWtimes=Rtimes<9999?err_rate[0]:Integer.toString((int)(Wtimes*0.9+0.5));//times hold 4 places at most
			String newRtimes=Rtimes<9999?Integer.toString(Rtimes+1):"9000";
			newErrRate=newWtimes+"/"+newRtimes;
		}
		String sql="update Question set average_time=?,error_rate=? where category=? and id=?";
		jdbcTemplate.update(sql, new Object[] {newAvgTime,newErrRate,questionCategory,questionID});
	}

	@Override
	public void updateAnsweredWrong(int time, String wrongAnswer, String questionCategory, String questionID,
			int type,String averageTime, String errorRate, String errorRandomness) {
		// TODO Auto-generated method stub
		String newAvgTime;
		String newErrRate;
		String newErrRand;
		if(averageTime==null) {
			newAvgTime=Integer.toString(time)+"/1";
		}else {
			String[] avg_time=averageTime.split("/");
			int avgT=Integer.parseInt(avg_time[0]);
			int times=Integer.parseInt(avg_time[1]);
			String newT=times<9999?Integer.toString((avgT*times+time)/(times+1)):avg_time[0];
			String newTimes=times<9999?Integer.toString(times+1):avg_time[1];
			newAvgTime=newT+"/"+newTimes;
		}
		if(errorRate==null) {
			newErrRate="1/0";
		}else {
			String[] err_rate=errorRate.split("/");
			int Wtimes=Integer.parseInt(err_rate[0]);
			int Rtimes=Integer.parseInt(err_rate[1]);
			String newRtimes=Wtimes<9999?err_rate[1]:Integer.toString((int)(Rtimes*0.9+0.5));
			String newWtimes=Wtimes<9999?Integer.toString(Wtimes+1):"9000";
			newErrRate=newWtimes+"/"+newRtimes;
		}
		switch(type) {//set new error_randomness according to question type, error_randomness is null if the type is T or F question
		case 0:
			newErrRand=null;
			break;
		case 1://if type is 1(multiple choices),the format of _error_randomness is like A:times/B/C:times/D:times if the correct answer is B
			if(errorRandomness==null) {
				switch(wrongAnswer) {
				case "A":
					newErrRand="A:1/B/C/D";
					break;
				case "B":
					newErrRand="A/B:1/C/D";
					break;
				case "C":
					newErrRand="A/B/C:1/D";
					break;
				default:
					newErrRand="A/B/C/D:1";
				}
			}else {
				String[] answers=errorRandomness.split("/");
				switch(wrongAnswer) {
				case "A":
					if(answers[0].equals("A")) {
						newErrRand="A:1/"+answers[1]+"/"+answers[2]+"/"+answers[3];
					}else {
						newErrRand="A:"+Integer.toString(Integer.parseInt(answers[0].split(":")[1])+1)+"/"+answers[1]+"/"+answers[2]+"/"+answers[3];
					}
					break;
				case "B":
					if(answers[1].equals("B")) {
						newErrRand=answers[0]+"/B:1/"+answers[2]+"/"+answers[3];
					}else {
						newErrRand=answers[0]+"/B:"+Integer.toString(Integer.parseInt(answers[1].split(":")[1])+1)+"/"+answers[2]+"/"+answers[3];
					}
					break;
				case "C":
					System.out.println(answers[2]);
					if(answers[2].equals("C")) {
						newErrRand=answers[0]+"/"+answers[1]+"/C:1/"+answers[3];
					}else {
						newErrRand=answers[0]+"/"+answers[1]+"/C:"+Integer.toString(Integer.parseInt(answers[2].split(":")[1])+1)+"/"+answers[3];
					}
					break;
				default:
					if(answers[3].equals("D")) {
						newErrRand=answers[0]+"/"+answers[1]+"/"+answers[2]+"/D:1";
					}else {
						newErrRand=answers[0]+"/"+answers[1]+"/"+answers[2]+"/D:"+Integer.toString(Integer.parseInt(answers[3].split(":")[1])+1);
					}
				}
			}
			break;
		default://type is 2(fill in blank question),the format of error_randomness is like:answer1/times1/answer2/times2/answer3/times3...,order by times
			if(errorRandomness==null) {
				newErrRand=wrongAnswer+"/1";
			}else {
				String[] answers_and_times=errorRandomness.split("/");
				boolean isNew=true;//if the wrong answer is new
				for(int i=answers_and_times.length/2;i>0;i--) {//find an answer that matches the input wrong answer
					if(answers_and_times[2*i-2].equals(wrongAnswer)) {
						if(answers_and_times[2*i-1].equals("9999")) {
							for(int j=1;j<answers_and_times.length/2+1;j++) {
								if(i==j) {
									answers_and_times[2*j-1]="9000";
								}else {
									answers_and_times[2*j-1]=Integer.toString((int)(Integer.parseInt(answers_and_times[2*j-1])*0.9+0.5));
								}
							}
						}else {
							answers_and_times[2*i-1]=Integer.toString(Integer.parseInt(answers_and_times[2*i-1])+1);
							for(int k=i;k>1;k--) {//find if its times are greater than its front wrong answer's times 
								if(Integer.parseInt(answers_and_times[2*k-1])>Integer.parseInt(answers_and_times[2*k-3])) {//if greater,then swap them
									String tempAns=answers_and_times[2*k-2];
								    String tempTimes=answers_and_times[2*k-1];
								    answers_and_times[2*k-2]=answers_and_times[2*k-4];
								    answers_and_times[2*k-1]=answers_and_times[2*k-3];
								    answers_and_times[2*k-4]=tempAns;
								    answers_and_times[2*k-3]=tempTimes;
								}else {
									break;
								} 
							}
						}
						isNew=false;
						break;
					}
				}
				if(isNew) {
					if(wrongAnswer.length()<48-errorRandomness.length()) {//if the space is enough, then add the new wrong answer and its times
						newErrRand=answers_and_times[0]+"/"+answers_and_times[1];
						for(int i=2;i<answers_and_times.length/2+1;i++) {
							newErrRand+="/"+answers_and_times[2*i-2]+"/"+answers_and_times[2*i-1];
						}
						newErrRand+="/"+wrongAnswer+"/1";
					}else if(wrongAnswer.length()<50-errorRandomness.length()+answers_and_times[answers_and_times.length-1].length()
							+answers_and_times[answers_and_times.length-2].length()&&Integer.parseInt(answers_and_times[answers_and_times.length-1])==1) {
						newErrRand=answers_and_times[0]+"/"+answers_and_times[1];//if the space isn't enough and the last answer only occurs once,then replace it
						for(int i=2;i<answers_and_times.length/2;i++) {
							newErrRand+="/"+answers_and_times[2*i-2]+"/"+answers_and_times[2*i-1];
						}
						newErrRand+="/"+wrongAnswer+"/1";
					}else {//if the space isn't enough or the last wrong answer occurs more than once,just ignore the new wrong answer
						newErrRand=answers_and_times[0]+"/"+answers_and_times[1];
						for(int i=2;i<answers_and_times.length/2+1;i++) {
							newErrRand+="/"+answers_and_times[2*i-2]+"/"+answers_and_times[2*i-1];
						}
					}
				}else {//if the input wrong answer isn't new,just recollect the wrong answers and times(already modified)
					newErrRand=answers_and_times[0]+"/"+answers_and_times[1];
					for(int i=2;i<answers_and_times.length/2+1;i++) {
						newErrRand+="/"+answers_and_times[2*i-2]+"/"+answers_and_times[2*i-1];
					}
				}
			}
		}
		String sql="update Question set average_time=?,error_rate=?,error_randomness=? where category=? and id=?";
		jdbcTemplate.update(sql, new Object[] {newAvgTime,newErrRate,newErrRand,questionCategory,questionID});
	}

	@Override
	public void updateFeedback(String[] questionIDs,String[] new_feedbackRates,String[] old_feedbackRates) {//questionsIDs.length==2*new_feedbackRates.length==2*old_feedbackRates
		// TODO Auto-generated method stub
		String sql="update Question set feedback_rate=? where category=? and id=?";
		for(int i=0;i<new_feedbackRates.length;i++) {
			int d=new_feedbackRates[i].charAt(0)-'0';
			int f=new_feedbackRates[i].charAt(1)-'0';
			int t=new_feedbackRates[i].charAt(2)-'0';
			if(old_feedbackRates[i].equals(" ")) {
				old_feedbackRates[i]=Integer.toString(d)+(d==0?"/0/":"/1/")+Integer.toString(f)+(f==0?"/0/":"/1/")+Integer.toString(t)+(t==0?"/0":"/1");
			}else {
				String[] dft_rates=old_feedbackRates[i].split("/");
				if(d!=0) {
					old_feedbackRates[i]=Integer.toString((Integer.parseInt(dft_rates[0])*Integer.parseInt(dft_rates[1])+d)/(Integer.parseInt(dft_rates[1])+1))
							+"/"+Integer.toString(Integer.parseInt(dft_rates[1])+1)+"/";
				}else {
					old_feedbackRates[i]=dft_rates[0]+"/"+dft_rates[1]+"/";
				}
				if(f!=0) {
					old_feedbackRates[i]+=Integer.toString((Integer.parseInt(dft_rates[2])*Integer.parseInt(dft_rates[3])+f)/(Integer.parseInt(dft_rates[3])+1))
							+"/"+Integer.toString(Integer.parseInt(dft_rates[3])+1)+"/";
				}else {
					old_feedbackRates[i]+=dft_rates[2]+"/"+dft_rates[3]+"/";
				}
				if(t!=0) {
					old_feedbackRates[i]+=Integer.toString((Integer.parseInt(dft_rates[4])*Integer.parseInt(dft_rates[5])+t)/(Integer.parseInt(dft_rates[5])+1))
							+"/"+Integer.toString(Integer.parseInt(dft_rates[5])+1);
				}else {
					old_feedbackRates[i]+=dft_rates[4]+"/"+dft_rates[5];
				}
			}
			this.jdbcTemplate.update(sql, new Object[] {old_feedbackRates[i],questionIDs[2*i],questionIDs[2*i+1]});
		}
	}

	@Override
	public String getAnalysis(String questionCategory, String questionID) {
		// TODO Auto-generated method stub
		String sql="select * from Analysis where question_category=? and question_id=?";
		List<String> result=jdbcTemplate.query(sql, new Object[] {questionCategory,questionID}, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getString(3);
			}
			
		});
		return result.get(0);
	}

	@Override
	public List<Question> getRecommendedQuestionsByID(String answered_questions,String user_id) {
		// TODO Auto-generated method stub
		if(answered_questions==null) {
			return null;
		}
		String[] IDs_answered={"","","","","","","","","",""};
		String target_category_and_IDs="";
		for(int i=0;i<answered_questions.length()/17;i++) {//get base questions to recommend and answered question IDs in each area 
			if(answered_questions.charAt(i*17+7)=='F'||(answered_questions.charAt(i*17+7)=='T'&&Integer.parseInt(answered_questions.substring(i*17+8, i*17+11))>600)) {
				target_category_and_IDs+=answered_questions.substring(i*17, i*17+4).stripTrailing()+"/"+answered_questions.subSequence(17*i+4, 17*i+7)+"/";
			}
			IDs_answered[answered_questions.charAt(i*17)-'0']+=answered_questions.subSequence(17*i+4, 17*i+7);
		}
		if(target_category_and_IDs.length()==0) {
			return null;
		}
		List<Question> questions=new ArrayList<Question>();
		String[] targetCategoryAndIDs=target_category_and_IDs.split("/");
		for(int i=0;i<targetCategoryAndIDs.length/2;i++) {
			if(questions.size()==5) {
				break;
			}
			if((!questions.isEmpty())&&questions.get(questions.size()-1).getCategory().charAt(0)-'0'>=targetCategoryAndIDs[2*i].charAt(0)-'0') {
				continue;
			}
			Question question;
			try {//get the base question
				question=jdbcTemplate.queryForObject("select * from Question where category="+targetCategoryAndIDs[2*i]+" and id="+targetCategoryAndIDs[2*i+1],
						 new RowMapper<Question>() {

					@Override
					public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						Question newQuestion=new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),
								rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(13));
						newQuestion.setAverage_time(rs.getString(10));
						newQuestion.setError_rate(rs.getString(11));
						newQuestion.setError_randomness(rs.getString(12));
						newQuestion.setFeedback_rate(rs.getString(14));
						return newQuestion;
					}
					
				});
			}catch(EmptyResultDataAccessException e) {
				question=null;
			}
			if(question!=null) {//get new questions to recommend
				String sql="select * from Question where category="+targetCategoryAndIDs[2*i]+" and id";
				String not_in_or_equal;
				if(IDs_answered[targetCategoryAndIDs[2*i].charAt(0)-'0'].equals("")) {
					not_in_or_equal="<>"+targetCategoryAndIDs[2*i+1];
				}else {
					not_in_or_equal=" not in(";
					for(int j=0;j<IDs_answered[targetCategoryAndIDs[2*i].charAt(0)-'0'].length()/3;j++) {
						not_in_or_equal+="\""+IDs_answered[targetCategoryAndIDs[2*i].charAt(0)-'0'].substring(j*3,j*3+3)+"\",";
					}
					not_in_or_equal=not_in_or_equal.substring(0, not_in_or_equal.length()-1)+")";
				}
				sql+=not_in_or_equal+" and (knowledge_points=? or id=(select id from Question where category=?"+
						" and id"+not_in_or_equal+" order by power(difficulty_of_understanding-?,2)+power(difficulty_of_solving-?,2)+power(practicality-?,2) asc limit 1))";
				List<Question> questions2recommend;//get qualified questions to recommend
				questions2recommend=jdbcTemplate.query(sql, new Object[] {question.getKnowledge_points(),
						targetCategoryAndIDs[2*i],question.getDifficulty_of_understanding(),question.getDifficulty_of_solving(),question.getPracticality()}, 
						new RowMapper<Question>() {

					@Override
					public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						Question newQuestion=new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),
								rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(13));
						newQuestion.setAverage_time(rs.getString(10));
						newQuestion.setError_rate(rs.getString(11));
						newQuestion.setError_randomness(rs.getString(12));
						newQuestion.setFeedback_rate(rs.getString(14));
						return newQuestion;
					}
					
				});
				if(!questions2recommend.isEmpty()) {
					Question question2recommend=null;
					for(int j=0;j<questions2recommend.size();j++) {//choose one question to recommend
						if(Math.pow(questions2recommend.get(j).getDifficulty_of_understanding()-question.getDifficulty_of_understanding(), 2)+
								Math.pow(questions2recommend.get(j).getDifficulty_of_solving()-question.getDifficulty_of_solving(), 2)+
								Math.pow(questions2recommend.get(j).getPracticality()-question.getPracticality(), 2)<12.0) {
							question2recommend=questions2recommend.get(j);
							break;
						}
					}
					if(question2recommend!=null) {
						questions.add(question2recommend);
						for(int j=0;j<answered_questions.length()/17;j++) {//mark answered_questions
							if(answered_questions.substring(j*17, j*17+4).stripTrailing().equals(targetCategoryAndIDs[2*i])
									&&answered_questions.substring(j*17+4, j*17+7).equals(targetCategoryAndIDs[2*i+1])) {
								if(answered_questions.charAt(17*j+7)=='T') {
									System.out.println(answered_questions.substring(j*17,j*17+8));
									answered_questions=answered_questions.substring(0, 17*j+7)+"1"+answered_questions.substring(17*j+8);
								}else {
									System.out.println(answered_questions.substring(j*17,j*17+8));
									answered_questions=answered_questions.substring(0, 17*j+7)+"0"+answered_questions.substring(17*j+8);
								}
								break;
							}
						}
					}
				}
			}
		}
		if(questions.isEmpty()) {
			return null;
		}else {//update answered_questions
			String sql="update User set answered_questions=? where user_id=?";
			jdbcTemplate.update(sql, new Object[] {answered_questions,user_id});
			String head=questions.get(0).getError_rate();//attach updated answered_questions
			questions.get(0).setError_rate(head+":"+answered_questions);
			return questions;
		}
	}

	@Override
	public List<Question> getWrongQuestionsByID(String question_category_and_IDs, String wrong_answers,String user_id) {
		// TODO Auto-generated method stub
		if(question_category_and_IDs.equals("")) {
			return null;
		}
		String[] category_and_IDs=question_category_and_IDs.split("/");
		String sql="select * from Question where";
		for(int i=0;i<category_and_IDs.length/2;i++) {
			sql+=" category="+category_and_IDs[i*2]+" and id="+category_and_IDs[i*2+1]+" or";
		}
		sql=sql.substring(0, sql.length()-3);
		List<Question> Questions=this.jdbcTemplate.query(sql, new RowMapper<Question>() {

			@Override
			public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Question newQuestion=new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),
						rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(13));
				newQuestion.setAverage_time(rs.getString(10));
				newQuestion.setError_rate(rs.getString(11));
				newQuestion.setError_randomness(rs.getString(12));
				newQuestion.setFeedback_rate(rs.getString(14));
				return newQuestion;
			}
			
		});
		if(Questions.isEmpty()) {
			return null;
		}else {
			String sqlUpdate="update User set wrong_answers=? where user_id=?";
			jdbcTemplate.update(sqlUpdate, new Object[] {wrong_answers,user_id});
			return Questions;
		}
	}
	
	
}
