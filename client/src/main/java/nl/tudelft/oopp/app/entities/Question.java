package nl.tudelft.oopp.app.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * The class Question.
 */
public class Question extends Message {
    /**Class responsible for sending a question by the Student.
     * @param answered - the status of a question: answered or not
     * @param upvotes - the number of upvotes received by a question (default 1)
     * @param age - the age of a question, based on seconds
     *            passed from the moment it was added (default 0)
     * @param selected - boolean to specify if the question has been selected
     * @param upvoted - boolean to specify if the question has been upvoted
     * @param answer - the written answer of the question
     */

    private boolean answered;

    private int upvotes;

    private int age;

    private boolean selected;

    private boolean upvoted;

    private double sortVariable;

    private String answer;

    /**
     * Constructs a new Question.
     */
    public Question(User user, String message, Timestamp date) {
        super(user, message, date);
        this.answered = false;
        this.upvotes = 0;
        this.age = 0;
        this.selected = false;
        this.upvoted = false;
        this.sortVariable = 0;
        this.answer = "";
    }

    /**
     * Getters and setters for the attributes of class Question.
     */
    public boolean isAnswered() {
        return answered;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getAge() {
        return age;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    /**
     * Method used to upvote a question.
     */
    public void upvoteQuestion() {
        this.upvotes++;
    }

    /**
     * Method used to calculate the sorting variable based on upvotes and age.
     * The number -0.0000033 is calculated using a constant -0.2
     * which is meant to make it so every 5 minutes it goes down
     * by 1 upvote in value. The -0.2 is divided by 60000 to convert from
     * milliseconds to minutes.
     */
    public double calculateSortVariable() {
        sortVariable = upvotes - 0.0000033
                * (System.currentTimeMillis() - getDate().getTime());
        return sortVariable;
    }

    /**
     * Check if 2 Question Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return super.equals(question) && this.getId() == ((Question) o).getId();
    }

    /**Generate a hash code for the question.
     *
     * @return - the hash code of the question
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isAnswered(), getUpvotes(), getAge(), getAnswer());
    }

    /**Override the default toString method.
     *
     * @return - a String representation of this
     */
    @Override
    public String toString() {
        return "Question<" + this.getUser() + ", "
                + this.getMessage() + ", "
                + this.getUpvotes() + ", "
                + this.getAnswer() + ">";
    }

    /**
     * Method to return a toJson type representation of a Question.
     * @return the Json format of a Question.
     */
    public String toJson() {
        return "{\"message\":\"" + this.getMessage() + "\",\"user\":\""
                + this.getUser().getUsername() + "\",\"answer\":\"" + this.getAnswer()
                + "\",\"timestamp\":\""
                + this.getDate().toString() + "\"}";
    }
}
