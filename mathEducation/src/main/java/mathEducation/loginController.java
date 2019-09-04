package mathEducation;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class loginController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private QuestionDAO questionDAO;
	@RequestMapping(value="/login")
	public ModelAndView loginPage(@ModelAttribute("login")Login login) {
		ModelAndView mav=new ModelAndView("login");
		return mav;
	}
	@RequestMapping(value="/loginFailure")
	public ModelAndView reLoginPage(@ModelAttribute("login")Login login) {
		ModelAndView mav=new ModelAndView("loginFailure");
		mav.addObject("message", "invalid logins, please input again!");
		return mav;
	}
	@RequestMapping(value="/logout")
	public ModelAndView logoutPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession();
        httpSession.invalidate();
		return new ModelAndView("redirect:/login");
	}
	@RequestMapping(value = "/home",method=RequestMethod.POST)
	public ModelAndView homePage(HttpServletRequest request, HttpServletResponse response,
			  @ModelAttribute("login") Login login) {
		User user;
		HttpSession httpSession = request.getSession();
		if(request.getParameter("page").equals("summary")) {//if return from summary page
			List<Question> Questions=(List<Question>)httpSession.getAttribute("Questions");
			if((((int)httpSession.getAttribute("index"))>=Questions.size())&&(request.getParameter("error_review")==null)) {
				String questions2add=(String)httpSession.getAttribute("questions2add");
				String category_and_IDs="";
				String new_feedback_rates="";
				String old_feedback_rates="";
				for(int i=0;i<Questions.size();i++) {
					if(questions2add.charAt(17*i)!='#') {
						String d=request.getParameter("r"+Integer.toString(i)+"1");
						String f=request.getParameter("r"+Integer.toString(i)+"2");
						String t=request.getParameter("r"+Integer.toString(i)+"3");
						if((!d.equals(""))||(!f.equals(""))||(!t.equals(""))) {
							new_feedback_rates+=d.equals("")?"0":d;
							new_feedback_rates+=f.equals("")?"0":f;
							new_feedback_rates+=t.equals("")?"0":t;
							new_feedback_rates+="/";
							category_and_IDs+=Questions.get(i).getCategory()+"/"+Questions.get(i).getID()+"/";
							if(Questions.get(i).getFeedback_rate()==null) {
								old_feedback_rates+=" :";
							}else {
								old_feedback_rates+=Questions.get(i).getFeedback_rate()+":";
							}
						}
					}
				}
				if(!new_feedback_rates.equals("")) {//update feedback_rates
					this.questionDAO.updateFeedback(category_and_IDs.split("/"),new_feedback_rates.split("/"),old_feedback_rates.split(":"));
				}
			}
			user=userDAO.getUserById((String) httpSession.getAttribute("user_id"));
			httpSession.setAttribute("error_rates",user.getError_rates());
			httpSession.setAttribute("wrong_answers",user.getWrong_answers());
			httpSession.setAttribute("recommended_questions",user.getRecommended_questions());
			httpSession.setAttribute("answered_questions",user.getAnswered_questions());
			httpSession.setAttribute("average_time_consumption",user.getAverage_time_consumption());
			if(user.getTime_spent()==null) {
				httpSession.setAttribute("time_spent", 0);
			}else {
				httpSession.setAttribute("time_spent",Integer.parseInt(user.getTime_spent()));
			}
			httpSession.setAttribute("index", 0);
		}else if(request.getParameter("page").equals("no_question")){//if return from no question available page 
			user=userDAO.getUserById((String) httpSession.getAttribute("user_id"));
		}else if(request.getParameter("page").equals("login")&&login.getUserid()!=null&&login.getPassword()!=null) {
			user=userDAO.getUserById(login.getUserid());
			if(user!=null) {
				if(user.getPWD().equals(login.getPassword())) {
					httpSession.setAttribute("user_id",user.getID());
					httpSession.setAttribute("error_rates",user.getError_rates());
					httpSession.setAttribute("wrong_answers",user.getWrong_answers());
					httpSession.setAttribute("recommended_questions",user.getRecommended_questions());
					httpSession.setAttribute("answered_questions",user.getAnswered_questions());
					httpSession.setAttribute("average_time_consumption",user.getAverage_time_consumption());
					if(user.getTime_spent()==null) {
						httpSession.setAttribute("time_spent", 0);
					}else {
						httpSession.setAttribute("time_spent",Integer.parseInt(user.getTime_spent()));
					}
					httpSession.setAttribute("index", 0);
				}else {
					return new ModelAndView("redirect:/loginFailure");
				}
			}else {
				return new ModelAndView("redirect:/loginFailure");
			}
		}else {
			return new ModelAndView("redirect:/loginFailure");
		}
		ModelAndView mav=new ModelAndView("home");
		mav.addObject("userid", user.getID());
		mav.addObject("trainingTime", user.getTime_spent()==null?0:Integer.toString(Integer.parseInt(user.getTime_spent())/60)+" min");
		int[] levels=userDAO.getLevels(user.getError_rates(),user.getAverage_time_consumption());
		int level=0;
		for(int i=0;i<10;i++) {
			mav.addObject("level"+i, levels[i]);
			if(httpSession.getAttribute("error_rates")==null) {
				mav.addObject("error_rate"+i, "not available");
			}else {
				String errorRate=((String)httpSession.getAttribute("error_rates")).substring(i*2, i*2+2);
				if(errorRate.equals("-2")) {
					mav.addObject("error_rate"+i, "not available");
				}else if(errorRate.equals("-1")) {
					mav.addObject("error_rate"+i, "100%");
				}else {
					mav.addObject("error_rate"+i, Integer.parseInt(errorRate)+"%");
				}
			}
			if(httpSession.getAttribute("average_time_consumption")==null) {
				mav.addObject("average_time_consumption"+i, "not available");
			}else {
				String avgT=((String)httpSession.getAttribute("average_time_consumption")).substring(i*3, i*3+3);
				if(avgT.equals("--1")) {
					mav.addObject("average_time_consumption"+i, "not available");
				}else {
					mav.addObject("average_time_consumption"+i, Integer.parseInt(avgT)/60+"min");
				}
			}
			level+=levels[i];
		}
		level=(int)(((float)level)/10.0+0.5);
		mav.addObject("level", level);
		return mav;
	}
}
