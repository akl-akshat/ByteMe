// Feedback.java
public class Feedback<T> {
    private String studentUsername;
    private T feedback;

    public Feedback(String studentUsername, T feedback) {
        this.studentUsername = studentUsername;
        this.feedback = feedback;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public T getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "Feedback by " + studentUsername + " is " + feedback.toString();
    }
}