package mathEducation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDAOImpl implements UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
	@Override
	public User getUserById(String userID) {
		// TODO Auto-generated method stub
		String sql="select * from User where user_id=?";
		List<User> user=jdbcTemplate.query(sql, new Object[] {userID}, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				User newUser=new User(rs.getString(1),rs.getString(2));
				newUser.setError_rates(rs.getString(3));
				newUser.setWrong_answers(rs.getString(4));
				newUser.setRecommended_questions(rs.getString(5));
				newUser.setAnswered_questions(rs.getString(6));
				newUser.setAverage_time_consumption(rs.getString(7));
				newUser.setTime_spent(rs.getString(8));
				return newUser;
			}
			
		});
		return user.size()==1?user.get(0):null;
	}

	@Override
	public int[] getLevels(String error_rates,String time_consumption) {
		//get levels in every area according to answered question number,
		//error rates and average time consumption
		// TODO Auto-generated method stub
		int[] levels=new int[10];
		if(error_rates==null||time_consumption==null) {
			for(int i=0;i<10;i++) {
				levels[i]=0;
			}
		}else {
			for(int i=0;i<10;i++) {
				if(error_rates.substring(2*i,2*i+2).equals("-2")) {
					levels[i]=0;
				}else {
					double E,T;
					if(error_rates.substring(2*i, 2*i+2).equals("-1")) {
						E=1.0;
					}else {
						E=(double)(error_rates.charAt(2*i)-'0')*0.1+(double)(error_rates.charAt(2*i+1)-'0')*0.01;
					}
			        T=(double)((time_consumption.charAt(3*i)-'0')*100+(time_consumption.charAt(3*i+1)-'0')*10+(time_consumption.charAt(3*i+2)-'0'));
			        double row_level=1.0/(0.7*E+T/3000.0)-1.0;
			        if(row_level>=9) {
			        	levels[i]=9;
			        }else {
			        	levels[i]=(int)(row_level+0.5);
			        }
				}
			}
		}
		return levels;
	}
	@Override
	public void updateAnsweredQuestions(String questions_to_add, String answered_questions, String wrong_answers,
			String error_rates, String time_consumption, String time_spent, String userID) {
		// TODO Auto-generated method stub
		if(questions_to_add==null) {
			return;
		}
		if(answered_questions==null) {
			answered_questions="";
		}
		System.out.println(questions_to_add);
		for(int i=0;i<questions_to_add.length()/17;i++) {
			int addCate=Integer.parseInt(questions_to_add.substring(i*17, i*17+4).stripTrailing());
			boolean biggest=true;
			for(int j=0;j<answered_questions.length()/17;j++) {
				int cate=Integer.parseInt(answered_questions.substring(j*17, j*17+4).stripTrailing());
				if(cate>addCate) {
					answered_questions=answered_questions.substring(0, j*17)+questions_to_add.substring(i*17, i*17+17)+answered_questions.substring(j*17);
					biggest=false;
					break;
				}else if(cate==addCate) {
					int addID=Integer.parseInt(questions_to_add.substring(i*17+4, i*17+7));
					int id=Integer.parseInt(answered_questions.substring(j*17+4, j*17+7));
					if(id>addID) {
						answered_questions=answered_questions.substring(0, j*17)+questions_to_add.substring(i*17, i*17+17)+answered_questions.substring(j*17);
						biggest=false;
						//System.out.println(id+" "+addID);
						break;
					}
				}
				//System.out.println(cate+" "+addCate);
			}
			if(biggest) {
				answered_questions=answered_questions.concat(questions_to_add.substring(i*17, i*17+17));
			}
			biggest=true;
			if(questions_to_add.charAt(i*17+7)=='F') {
				if(wrong_answers==null) {
					wrong_answers=questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11);
				}else {
					for(int j=0;j<wrong_answers.length()/13;j++) {
						int cate=Integer.parseInt(wrong_answers.substring(j*13, j*13+4).stripTrailing());
						if(cate>addCate) {
							wrong_answers=wrong_answers.substring(0, j*13)+questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11)+wrong_answers.substring(j*13);
							biggest=false;
							break;
						}else if(cate==addCate) {
							int addID=Integer.parseInt(questions_to_add.substring(i*17+4, i*17+7));
							int id=Integer.parseInt(wrong_answers.substring(j*13+4, j*13+7));
							if(id>addID) {
								wrong_answers=wrong_answers.substring(0, j*13)+questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11)+wrong_answers.substring(j*13);
								biggest=false;
								//System.out.println(id+" "+addID);
								break;
							}
						}
						//System.out.println(cate+" "+addCate);
					}
					if(biggest) {
						wrong_answers=wrong_answers.concat(questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11));
					}
				}
				System.out.println(wrong_answers);
			}
			System.out.println(answered_questions);
		}
		int[] T= {0,0,0,0,0,0,0,0,0,0};
		int[] F= {0,0,0,0,0,0,0,0,0,0};
		int[] totalTime= {0,0,0,0,0,0,0,0,0,0};
		if(error_rates==null) {
			error_rates="-2-2-2-2-2-2-2-2-2-2";
		}
		if(time_consumption==null) {
			time_consumption="--1--1--1--1--1--1--1--1--1--1";
		}
		for(int i=0;i<answered_questions.length()/17;i++) {
			if(answered_questions.charAt(i*17+7)=='T') {
				T[answered_questions.charAt(i*17)-'0']++;
			}else {
				F[answered_questions.charAt(i*17)-'0']++;
			}
			totalTime[answered_questions.charAt(i*17)-'0']+=Integer.parseInt(answered_questions.substring(i*17+8, i*17+11));
		}
		for(int i=0;i<10;i++) {
			String R=((T[i]+F[i])==0?"-2":Integer.toString((F[i]*100)/(T[i]+F[i])));
			String Time=((T[i]+F[i])==0?"--1":Integer.toString(totalTime[i]/(T[i]+F[i])));
			if(R.length()==1) {
				R="0"+R;
			}
			if(R.length()==3) {
				R="-1";
			}
			switch(Time.length()) {
			case 1:
				Time="00"+Time;
				break;
			case 2:
				Time="0"+Time;
				break;
			}
			error_rates=error_rates.substring(0, 2*i)+R+error_rates.substring(2*i+2);
			time_consumption=time_consumption.substring(0,3*i)+Time+time_consumption.substring(3*i+3);
		}
		System.out.println(error_rates+" "+time_consumption+" "+time_spent);
		String sql="update User set answered_questions=?,wrong_answers=?,error_rates=?,average_time_consumption=?,time_spent=? where user_id=?";
		jdbcTemplate.update(sql, new Object[] {answered_questions,wrong_answers,error_rates,time_consumption,time_spent,userID});
	}
	@Override
	public void updateRecommendedQuestions(String questions_to_add, String if_needed, String answered_questions,
			String recommended_questions, String wrong_answers, String error_rates, String time_consumption,
			String time_spent, String userID) {
		// TODO Auto-generated method stub
		if(questions_to_add==null) {
			return;
		}
		if(answered_questions==null) {
			answered_questions="";
		}
		System.out.println(questions_to_add);
		for(int i=0;i<questions_to_add.length()/17;i++) {
			int addCate=Integer.parseInt(questions_to_add.substring(i*17, i*17+4).stripTrailing());
			boolean biggest=true;
			for(int j=0;j<answered_questions.length()/17;j++) {//add to answered_questions
				int cate=Integer.parseInt(answered_questions.substring(j*17, j*17+4).stripTrailing());
				if(cate>addCate) {
					answered_questions=answered_questions.substring(0, j*17)+questions_to_add.substring(i*17, i*17+17)+answered_questions.substring(j*17);
					biggest=false;
					break;
				}else if(cate==addCate) {
					int addID=Integer.parseInt(questions_to_add.substring(i*17+4, i*17+7));
					int id=Integer.parseInt(answered_questions.substring(j*17+4, j*17+7));
					if(id>addID) {
						answered_questions=answered_questions.substring(0, j*17)+questions_to_add.substring(i*17, i*17+17)+answered_questions.substring(j*17);
						biggest=false;
						//System.out.println(id+" "+addID);
						break;
					}
				}
				//System.out.println(cate+" "+addCate);
			}
			if(biggest) {
				answered_questions=answered_questions.concat(questions_to_add.substring(i*17, i*17+17));
			}
			biggest=true;
			if(questions_to_add.charAt(i*17+7)=='F') {
				if(wrong_answers==null) {
					wrong_answers=questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11);
				}else {
					for(int j=0;j<wrong_answers.length()/13;j++) {//add to wrong_qnswers
						int cate=Integer.parseInt(wrong_answers.substring(j*13, j*13+4).stripTrailing());
						if(cate>addCate) {
							wrong_answers=wrong_answers.substring(0, j*13)+questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11)+wrong_answers.substring(j*13);
							biggest=false;
							break;
						}else if(cate==addCate) {
							int addID=Integer.parseInt(questions_to_add.substring(i*17+4, i*17+7));
							int id=Integer.parseInt(wrong_answers.substring(j*13+4, j*13+7));
							if(id>addID) {
								wrong_answers=wrong_answers.substring(0, j*13)+questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11)+wrong_answers.substring(j*13);
								biggest=false;
								//System.out.println(id+" "+addID);
								break;
							}
						}
						//System.out.println(cate+" "+addCate);
					}
					if(biggest) {
						wrong_answers=wrong_answers.concat(questions_to_add.substring(i*17, i*17+7)+"10"+questions_to_add.substring(i*17+7, i*17+11));
					}
				}
				System.out.println(wrong_answers);
			}
			biggest=true;
			if(if_needed.charAt(i)!=' ') {
				if(recommended_questions==null) {
					recommended_questions=questions_to_add.substring(i*17, i*17+7)+if_needed.charAt(i);
				}else {
					for(int j=0;j<recommended_questions.length()/8;j++) {//add to recommended_questions
						int cate=Integer.parseInt(recommended_questions.substring(j*8, j*8+4).stripTrailing());
						if(cate>addCate) {
							recommended_questions=recommended_questions.substring(0, j*8)+questions_to_add.substring(i*17, i*17+7)+if_needed.charAt(i)+recommended_questions.substring(j*8);
							biggest=false;
							break;
						}else if(cate==addCate) {
							int addID=Integer.parseInt(questions_to_add.substring(i*17+4, i*17+7));
							int id=Integer.parseInt(recommended_questions.substring(j*8+4, j*8+7));
							if(id>addID) {
								recommended_questions=recommended_questions.substring(0, j*8)+questions_to_add.substring(i*17, i*17+7)+if_needed.charAt(i)+recommended_questions.substring(j*8);
								biggest=false;
								//System.out.println(id+" "+addID);
								break;
							}
						}
						//System.out.println(cate+" "+addCate);
					}
					if(biggest) {
						recommended_questions=recommended_questions.concat(questions_to_add.substring(i*17, i*17+7)+if_needed.charAt(i));
					}
				}
				System.out.println(recommended_questions);
			}
			System.out.println(answered_questions);
		}
		int[] T= {0,0,0,0,0,0,0,0,0,0};
		int[] F= {0,0,0,0,0,0,0,0,0,0};
		int[] totalTime= {0,0,0,0,0,0,0,0,0,0};
		if(error_rates==null) {
			error_rates="-2-2-2-2-2-2-2-2-2-2";
		}
		if(time_consumption==null) {
			time_consumption="--1--1--1--1--1--1--1--1--1--1";
		}
		for(int i=0;i<answered_questions.length()/17;i++) {
			if(answered_questions.charAt(i*17+7)=='T') {
				T[answered_questions.charAt(i*17)-'0']++;
			}else {
				F[answered_questions.charAt(i*17)-'0']++;
			}
			totalTime[answered_questions.charAt(i*17)-'0']+=Integer.parseInt(answered_questions.substring(i*17+8, i*17+11));
		}
		for(int i=0;i<10;i++) {//update error_rates andaverage_time_consumption
			String R=((T[i]+F[i])==0?"-2":Integer.toString((F[i]*100)/(T[i]+F[i])));
			String Time=((T[i]+F[i])==0?"--1":Integer.toString(totalTime[i]/(T[i]+F[i])));
			if(R.length()==1) {
				R="0"+R;
			}
			if(R.length()==3) {
				R="-1";
			}
			switch(Time.length()) {
			case 1:
				Time="00"+Time;
				break;
			case 2:
				Time="0"+Time;
				break;
			}
			error_rates=error_rates.substring(0, 2*i)+R+error_rates.substring(2*i+2);
			time_consumption=time_consumption.substring(0,3*i)+Time+time_consumption.substring(3*i+3);
		}
		System.out.println(error_rates+" "+time_consumption+" "+time_spent);
		String sql="update User set answered_questions=?,recommended_questions=?,wrong_answers=?,error_rates=?,average_time_consumption=?,time_spent=? where user_id=?";
		jdbcTemplate.update(sql, new Object[] {answered_questions,recommended_questions,wrong_answers,error_rates,time_consumption,time_spent,userID});
	}
	@Override
	public void updateErrorReview(String questions_to_set, String wrong_answers, String answered_questions,
			 String time_spent, String userID) {
		// TODO Auto-generated method stub
		if(questions_to_set==null) {
			return;
		}
		System.out.println(questions_to_set);
		for(int i=0;i<questions_to_set.length()/17;i++) {
			for(int j=0;j<wrong_answers.length()/13;j++) {
				if(questions_to_set.substring(i*17, i*17+4).equals(wrong_answers.substring(j*13, j*13+4))
						&&questions_to_set.substring(i*17+4, i*17+7).equals(wrong_answers.substring(j*13+4, j*13+7))) {
					int wrongTimes=wrong_answers.charAt(j*13+7)-'0';
					int rightTimes=wrong_answers.charAt(j*13+8)-'0';
					int avgT=Integer.parseInt(wrong_answers.substring(j*13+10, j*13+13));
					String new_avgT=Integer.toString((((wrongTimes+rightTimes)*avgT+Integer.parseInt(questions_to_set.substring(i*17+8, i*17+11)))/(wrongTimes+rightTimes+1)));
					if(questions_to_set.charAt(i*17+7)=='T') {
						if(rightTimes==9) {
							switch(new_avgT.length()) {
							case 1:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString((int)(wrongTimes*0.9+0.5))+"9T"
								+"00"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							case 2:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString((int)(wrongTimes*0.9+0.5))+"9T"
										+"0"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							default:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString((int)(wrongTimes*0.9+0.5))+"9T"
										+new_avgT+wrong_answers.substring(j*13+13);	
							}
						}else {
							switch(new_avgT.length()) {
							case 1:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes)+Integer.toString(rightTimes+1)+"T"
										+"00"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							case 2:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes)+Integer.toString(rightTimes+1)+"T"
										+"0"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							default:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes)+Integer.toString(rightTimes+1)+"T"
										+new_avgT+wrong_answers.substring(j*13+13);	
							}
						}
					}else {
						if(wrongTimes==9) {
							switch(new_avgT.length()) {
							case 1:
								wrong_answers=wrong_answers.substring(0, j*13+7)+"9"+Integer.toString((int)(rightTimes*0.9+0.5))+"F"
										+"00"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							case 2:
								wrong_answers=wrong_answers.substring(0, j*13+7)+"9"+Integer.toString((int)(rightTimes*0.9+0.5))+"F"
										+"0"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							default:
								wrong_answers=wrong_answers.substring(0, j*13+7)+"9"+Integer.toString((int)(rightTimes*0.9+0.5))+"F"
										+new_avgT+wrong_answers.substring(j*13+13);
							}
						}else {
							switch(new_avgT.length()) {
							case 1:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes+1)+Integer.toString(rightTimes)+"F"
										+"00"+new_avgT+wrong_answers.substring(j*13+13);
								break;
							case 2:
								wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes+1)+Integer.toString(rightTimes)+"F"
										+"0"+new_avgT+wrong_answers.substring(j*13+13);
								break;
						    default:
						    	wrong_answers=wrong_answers.substring(0, j*13+7)+Integer.toString(wrongTimes+1)+Integer.toString(rightTimes)+"F"
										+new_avgT+wrong_answers.substring(j*13+13);
							}
						}
					}
					System.out.println(wrong_answers);
				}
			}
			for(int j=0;j<answered_questions.length()/17;j++) {
				if(questions_to_set.substring(i*17, i*17+4).equals(answered_questions.substring(j*17, j*17+4))
						&&questions_to_set.substring(i*17+4, i*17+7).equals(answered_questions.substring(j*17+4, j*17+7))) {
					answered_questions=answered_questions.substring(0, j*17+11)+questions_to_set.substring(i*17+11, i*17+17)+answered_questions.substring(j*17+17);
				}
			}
		}
		String sql="update User set answered_questions=?,wrong_answers=?,time_spent=? where user_id=?";
		jdbcTemplate.update(sql, new Object[] {answered_questions,wrong_answers,time_spent,userID});
	}

}
