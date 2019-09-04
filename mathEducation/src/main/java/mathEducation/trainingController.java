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
public class trainingController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private QuestionDAO questionDAO;
	@RequestMapping(value = "/training",method=RequestMethod.POST)
	public ModelAndView trainingPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession=request.getSession();
		int index=(int)httpSession.getAttribute("index");
		if(index==0) {
			String answered_questions=(String)httpSession.getAttribute("answered_questions");
			String[] questions= {"","","","","","","","","",""};//store questions already answered
			if(answered_questions!=null) {
				for(int i=0;i<answered_questions.length()/17;i++) {
					questions[answered_questions.charAt(i*17)-'0']+=answered_questions.substring(i*17+4, i*17+7);
				}
			}
			List<Question> Questions=this.questionDAO.getQuestionsByID(questions);
			if(Questions==null){
				ModelAndView Mav=new ModelAndView("summary");
				Mav.addObject("user_id", httpSession.getAttribute("user_id"));
				Mav.addObject("questions", "There's no available question for you to answer!<input type=\"hidden\" name=\"page\" value=\"no_question\"><br>");
				return Mav;
			}
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
					this.questionDAO.updateAnsweredRight(Integer.parseInt(request.getParameter("time")), Questions.get(index-1).getCategory(),
							Questions.get(index-1).getID(),Questions.get(index-1).getAverage_time(),
						    Questions.get(index-1).getError_rate());
					questions2add+="T";
				}else {
					String wrong_answer=request.getParameter("answer").stripTrailing();
				    if(wrong_answer.length()>15) {
				    	wrong_answer=wrong_answer.substring(0, 15);
				    }
				    wrong_answer=wrong_answer.replace('/', '#');//replace '/' with '#' to match error_randomness format
				    this.questionDAO.updateAnsweredWrong(Integer.parseInt(request.getParameter("time")), wrong_answer,
				    		Questions.get(index-1).getCategory(),Questions.get(index-1).getID(),
						    Questions.get(index-1).getType(),Questions.get(index-1).getAverage_time(),
						    Questions.get(index-1).getError_rate(),Questions.get(index-1).getError_randomness());
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
				return new ModelAndView("redirect:/training_summary");
			}
		}
		List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
		ModelAndView mav =new ModelAndView("training");
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
	@RequestMapping(value = "/training_summary")
	public ModelAndView trainingSummaryPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession=request.getSession();
		String questions2add=(String)httpSession.getAttribute("questions2add");
		List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
		String[] submitted_answers=((String)httpSession.getAttribute("submitted_answers")).split("/");
		this.userDAO.updateAnsweredQuestions(questions2add.replaceAll("#", ""), (String)httpSession.getAttribute("answered_questions"), 
				(String)httpSession.getAttribute("wrong_answers"), (String)httpSession.getAttribute("error_rates"),
				(String)httpSession.getAttribute("average_time_consumption"), Integer.toString((int)httpSession.getAttribute("time_spent")), (String)httpSession.getAttribute("user_id"));
		ModelAndView mav=new ModelAndView("summary");
		String Q= "";
		for(int i=0;i<Questions.size();i++) {
			Q+="Question "+Integer.toString(i+1)+"<br>"+Questions.get(i).getContent()+"<br>Your Answer:";
			if(questions2add.charAt(17*i)=='#') {
				Q+="Not Answered<br>";
			}else {
				Q+=submitted_answers[i];
				if(questions2add.charAt(17*i+7)=='T') {
					Q+="  <p style='color:green'>Correct!</p>";
				}else {
					Q+="  <p style='color:red'>Wrong!</p>Correct Answer:"+Questions.get(i).getAnswer()+"<br>";
					if(Questions.get(i).getDifficulty_of_solving()>5) {
						Q+="Analysis:"+this.questionDAO.getAnalysis(Questions.get(i).getCategory(), Questions.get(i).getID());
					}
				}
				Q+="Rate this Question:<br><p>How difficult?<select name=\"r"+Integer.toString(i)+"1\">\r\n" + 
						"          <option value=\"\">Not Rated</option>\r\n" + 
						"          <option value=\"1\">1</option>\r\n" + 
						"          <option value=\"2\">2</option>\r\n" + 
						"          <option value=\"3\">3</option>\r\n" + 
						"          <option value=\"4\">4</option>\r\n" + 
						"          <option value=\"5\">5</option>\r\n" + 
						"        </select> How easy to make a mistake?\r\n" + 
						"        <select name=\"r"+Integer.toString(i)+"2\">\r\n" + 
						"          <option value=\"\">Not Rated</option>\r\n" + 
						"          <option value=\"1\">1</option>\r\n" + 
						"          <option value=\"2\">2</option>\r\n" + 
						"          <option value=\"3\">3</option>\r\n" + 
						"          <option value=\"4\">4</option>\r\n" + 
						"          <option value=\"5\">5</option>\r\n" + 
						"        </select> How untypical?\r\n" + 
						"        <select name=\"r"+Integer.toString(i)+"3\">\r\n" + 
						"          <option value=\"\">Not Rated</option>\r\n" + 
						"          <option value=\"1\">1</option>\r\n" + 
						"          <option value=\"2\">2</option>\r\n" + 
						"          <option value=\"3\">3</option>\r\n" + 
						"          <option value=\"4\">4</option>\r\n" + 
						"          <option value=\"5\">5</option>\r\n" + 
						"        </select></p>";
			}
		}
		mav.addObject("user_id", httpSession.getAttribute("user_id"));
		mav.addObject("questions", Q);
		return mav;
	}
}
