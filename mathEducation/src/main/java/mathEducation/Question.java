package mathEducation;

public class Question {
	private String category;
	private String knowledge_points;
	private String id;
	private int _type;
	private String content;
	private String answer;
	private int length;
	private int difficulty_of_understanding;
	private int difficulty_of_solving;
	private String average_time;
	private String error_rate;
	private String error_randomness;
	private int practicality;
	private String feedback_rate;
	public Question() {
		super();
	}
	public Question(String Category,String KnowPoints,String ID,int Type,String Content,String Answer,
			int Length,int diffU,int diffS,int Practility) {
		super();
		this.category=Category;
		this.knowledge_points=KnowPoints;
		this.id=ID;
		this._type=Type;
		this.content=Content;
		this.answer=Answer;
		this.length=Length;
		this.difficulty_of_understanding=diffU;
		this.difficulty_of_solving=diffS;
		this.practicality=Practility;
		this.average_time=this.error_rate=this.error_randomness=this.feedback_rate=null;
	}
	public String getCategory() {
		return this.category;
	}
	public String getKnowledge_points() {
		return this.knowledge_points;
	}
	public String getID() {
		return this.id;
	}
	public int getType() {
		return this._type;
	}
	public String getContent() {
		return this.content;
	}
	public String getAnswer() {
		return this.answer;
	}
	public int getLength() {
		return this.length;
	}
	public int getDifficulty_of_understanding() {
		return this.difficulty_of_understanding;
	}
	public int getDifficulty_of_solving() {
		return this.difficulty_of_solving;
	}
	public String getAverage_time() {
		return this.average_time;
	}
	public void setAverage_time(String averageTime) {
		this.average_time=averageTime;
	}
	public String getError_rate() {
		return this.error_rate;
	}
	public void setError_rate(String errorRate) {
		this.error_rate=errorRate;
	}
	public String getError_randomness() {
		return this.error_randomness;
	}
	public void setError_randomness(String errorRandomness) {
		this.error_randomness=errorRandomness;
	}
	public int getPracticality() {
		return this.practicality;
	}
	public String getFeedback_rate() {
		return this.feedback_rate;
	}
	public void setFeedback_rate(String feedbackRate) {
		this.feedback_rate=feedbackRate;
	}
}
