package nl.tudelft.oopp.app.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The class Question.
 */
@Entity
@Table(name = "questions")
public class Question extends Message {
    /**Class responsible for sending a question by the Student.
     * @param answered - the status of a question: answered or not
     * @param upvotes - the number of upvotes received by a question (default 1)
     * @param age - the age of a question, based on seconds passed
     *            from the moment it was added (default 0)
     * @param answer - the written answer of the question
     */

    @Column(name = "answered")
    private boolean answered;

    @Column(name = "upvotes")
    private int upvotes;

    @Column(name = "age")
    private int age;

    @Column(name = "answer")
    private String answer;


    /**
     * Empty constructor used by the H2 Database.
     */
    public Question() {

    }

    /**
     * Constructs a new Question.
     */
    public Question(User user, String message, Timestamp date) {
        super(user, message, date);
        this.answered = false;
        this.upvotes = 0;
        this.age = 0;
        this.answer = "";
    }

    /**
     * Getters and setters for the attributes of class Question.
     */
    public boolean isAnswered() {
        return answered;
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

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setAge(int age) {
        this.age = age;
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
        if (!super.equals(o)) {
            return false;
        }
        Question question = (Question) o;
        return isAnswered() == question.isAnswered()
                && getUpvotes() == question.getUpvotes()
                && getAge() == question.getAge()
                && getAnswer().equals(question.getAnswer());
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
}
