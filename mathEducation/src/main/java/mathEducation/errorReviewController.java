package mathEducation;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class errorReviewController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private QuestionDAO questionDAO;
	@RequestMapping(value = "/error_review",method=RequestMethod.POST)
	public ModelAndView errorReviewPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession=request.getSession();
		int index=(int)httpSession.getAttribute("index");
		if(index==0) {
			String wrong_answers=(String)httpSession.getAttribute("wrong_answers");
			String questions="";//store questions categories and IDs to answer
			if(wrong_answers!=null) {
				int i;
				for(i=0;i<wrong_answers.length()/13;i++) {
					if(wrong_answers.charAt(13*i+9)=='0'||wrong_answers.charAt(13*i+9)=='1') {
						break;
					}
				}
				if(i<wrong_answers.length()/13) {
					int error_no=0;
					for(int j=i;error_no<5&&j<wrong_answers.length()/13;j++,error_no++) {
						questions+=wrong_answers.substring(j*13, j*13+4).stripTrailing()+"/"+wrong_answers.substring(j*13+4, j*13+7)+"/";
						if((j==wrong_answers.length()/13-1)&&(j!=0)) {
							wrong_answers=wrong_answers.substring(0, 9)+(wrong_answers.charAt(9)=='T'?'1':'0')+wrong_answers.substring(10, 13*j+9)
							+(wrong_answers.charAt(13*j+9)=='1'?'T':'F')+wrong_answers.substring(13*j+10);
						}else if(j<wrong_answers.length()/13-1) {
							wrong_answers=wrong_answers.substring(0, 13*j+9)+(wrong_answers.charAt(13*j+9)=='1'?'T':'F')+wrong_answers.substring(13*j+10, 13*j+22)
							+(wrong_answers.charAt(13*j+22)=='T'?'1':'0')+wrong_answers.substring(13*j+23);
						}
					}
				}else {
					int error_no=0;
					for(int j=0;error_no<5&&j<wrong_answers.length()/13;j++,error_no++) {
						questions+=wrong_answers.substring(j*13, j*13+4).stripTrailing()+"/"+wrong_answers.substring(j*13+4, j*13+7)+"/";
						if(j==0) {
							if(j==wrong_answers.length()/13-1) {
								wrong_answers=wrong_answers.substring(0, 9)+(wrong_answers.charAt(9)=='T'?'1':'0')+wrong_answers.substring(10);
							}else {
								wrong_answers=wrong_answers.substring(0, 22)+(wrong_answers.charAt(22)=='T'?'1':'0')+wrong_answers.substring(23);
							}
						}else {
							if(j==wrong_answers.length()/13-1) {
								wrong_answers=wrong_answers.substring(0, 9)+(wrong_answers.charAt(9)=='T'?'1':'0')+wrong_answers.substring(10, 13*j+9)
								+(wrong_answers.charAt(13*j+9)=='1'?'T':'F')+wrong_answers.substring(13*j+10);
							}else {
								wrong_answers=wrong_answers.substring(0, 13*j+9)+(wrong_answers.charAt(13*j+9)=='1'?'T':'F')+wrong_answers.substring(13*j+10, 13*j+22)
								+(wrong_answers.charAt(13*j+22)=='T'?'1':'0')+wrong_answers.substring(13*j+23);
							}
						}
					}
					System.out.println(wrong_answers);
				}
			}
			List<Question> Questions=this.questionDAO.getWrongQuestionsByID(questions,wrong_answers,(String)httpSession.getAttribute("user_id"));
			if(Questions==null){
				ModelAndView Mav=new ModelAndView("summary");
				Mav.addObject("user_id", httpSession.getAttribute("user_id"));
				Mav.addObject("questions", "There's no available question that you answered wrong for you to review!<input type=\"hidden\" name=\"page\" value=\"no_question\"><br>");
				return Mav;
			}
			httpSession.setAttribute("wrong_answers", wrong_answers);//update session wrong_answers
			httpSession.setAttribute("Questions", Questions);//store questions that are to be provided to user
			httpSession.setAttribute("questions2add", "");//store questions that are to be added in answered_questions
			httpSession.setAttribute("submitted_answers", "");//store answers that user submit
			//System.out.println(((List<Question>)httpSession.getAttribute("Questions")).get(this.index).getAnswer());
			//System.out.println(httpSession.getAttribute("questions2add"));
		}else {
			List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
			String questions2add=(String)httpSession.getAttribute("questions2add");
			String submitted_answers=(String)httpSession.getAttribute("submitted_answers");
			if(Integer.parseInt(request.getParameter("submit_index"))==(index-1)&&//if the submitted answer is the current question's answer
					(!request.getParameter("answer").equals(""))&&(!request.getParameter("time").equals("-1"))) {//if the submitted answer is not empty and the time hasn't been used up
				String catg=Questions.get(index-1).getCategory();
				switch(catg.length()) {
				case 1:
					questions2add+=catg+"   "+Questions.get(index-1).getID();
					break;
				case 2:
					questions2add+=catg+"  "+Questions.get(index-1).getID();
					break;
				default:
					questions2add+=catg+" "+Questions.get(index-1).getID();
				}
				if(request.getParameter("answer").stripTrailing().equals(Questions.get(index-1).getAnswer())) {
					questions2add+="T";
				}else {
				    questions2add+="F";
				}
				String date=LocalDate.now().toString();
				String T=request.getParameter("time");
				switch(T.length()) {
				case 1:
					questions2add+="00"+request.getParameter("time")+date.substring(2,4)+date.split("-")[1]+date.split("-")[2];
					break;
				case 2:
					questions2add+="0"+request.getParameter("time")+date.substring(2,4)+date.split("-")[1]+date.split("-")[2];
					break;
				default:
					questions2add+=request.getParameter("time")+date.substring(2,4)+date.split("-")[1]+date.split("-")[2];
				}
				httpSession.setAttribute("time_spent", (int)httpSession.getAttribute("time_spent")+Integer.parseInt(request.getParameter("time")));
				submitted_answers+=request.getParameter("answer").replace('/', '#')+"/";
			}else {
				questions2add+="#################";
				submitted_answers+=" /";
			}
			System.out.println(request.getParameter("submit_index")+" "+request.getParameter("answer")+" "+request.getParameter("time"));
		    httpSession.setAttribute("questions2add",questions2add);
			httpSession.setAttribute("submitted_answers", submitted_answers);
			if(index>=Questions.size()) {
				index++;
				httpSession.setAttribute("index", index);
				return new ModelAndView("redirect:/error_review_summary");
			}
		}
		List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
		ModelAndView mav =new ModelAndView("error_review");
		mav.addObject("user_id",httpSession.getAttribute("user_id"));
		mav.addObject("startTime",(int)(new Date().getTime()/1000));
		mav.addObject("index",index);
		mav.addObject("questionContent",Questions.get(index).getContent());
		mav.addObject("type", Questions.get(index).getType());
		mav.addObject("category", Questions.get(index).getCategory().charAt(0)-'0');
		System.out.println(httpSession.getAttribute("questions2add"));
		index++;
		httpSession.setAttribute("index", index);
		return mav;
	}
	@RequestMapping(value = "/error_review_summary")
	public ModelAndView errorReviewSummaryPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession=request.getSession();
		String questions2add=(String)httpSession.getAttribute("questions2add");
		List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
		String[] submitted_answers=((String)httpSession.getAttribute("submitted_answers")).split("/");
		this.userDAO.updateErrorReview(questions2add.replaceAll("#", ""), (String)httpSession.getAttribute("wrong_answers"), 
				(String)httpSession.getAttribute("answered_questions"),Integer.toString((int)httpSession.getAttribute("time_spent")),
				(String)httpSession.getAttribute("user_id"));
		ModelAndView mav=new ModelAndView("summary");
		String Q= "";
		for(int i=0;i<Questions.size();i++) {
			Q+="Error "+Integer.toString(i+1)+"<br>"+Questions.get(i).getContent()+"<br>Your Answer:";
			if(questions2add.charAt(17*i)=='#') {
				Q+="Not Answered<br>";
			}else {
				Q+=submitted_answers[i];
				if(questions2add.charAt(17*i+7)=='T') {
					Q+="  <p style='color:green'>Correct!</p>";
				}else {
					Q+="  <p style='color:red'>Wrong!</p>Correct Answer:"+Questions.get(i).getAnswer()+"<br>";
					if(Questions.get(i).getDifficulty_of_solving()>5) {
						Q+="Analysis:"+this.questionDAO.getAnalysis(Questions.get(i).getCategory(), Questions.get(i).getID())+"<br>";
					}
				}
			}
		}
		Q+="<input type=\"hidden\" name=\"error_review\" value=\"error_review\">";
		mav.addObject("user_id", httpSession.getAttribute("user_id"));
		mav.addObject("questions", Q);
		return mav;
	}
}
