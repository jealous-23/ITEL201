package finalproject.gamestate;

public class Question {
    private String question;
    private String[] options;
    private String answer;

    public Question(String q, String[] opts, String ans) {
        this.question = q;
        this.options = opts;
        this.answer = ans;
    }

    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public String getAnswer() { return answer; }
}