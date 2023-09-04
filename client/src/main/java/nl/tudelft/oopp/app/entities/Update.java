package nl.tudelft.oopp.app.entities;

import java.util.ArrayList;
import java.util.List;

public class Update {

    /**Class used to represent updates sent from the server.
     *
     * @param type - the type of the update
     * @param updatedQuestions - the list of updated question connected to this update
     */
    private String type;
    private List<Question> updatedQuestions;
    private int updatedNumberOfStudents;
    private int updatedTooSlowVotes;
    private int updatedTooFastVotes;
    private Question updatedQuestion;

    /**Constructor for the Update class.
     *
     * @param type - the type of the update
     * @param updatedQuestions - the list of updated questions
     */
    public Update(String type, List<Question> updatedQuestions) {
        this.type = type;
        this.updatedQuestions = updatedQuestions;
    }

    /**Constructor for the Update class.
     *
     * @param type - the type of the update
     * @param updatedQuestion - the updated question
     */
    public Update(String type, Question updatedQuestion) {
        this.type = type;
        this.updatedQuestion = updatedQuestion;
    }

    /**Constructor for the Update class.
     *
     * @param type - the type of the update
     */
    public Update(String type) {
        this.type = type;
        this.updatedQuestions = new ArrayList<>();
    }

    /**Constructor for the Update class.
     * @param type - the type of the update
     * @param tooFastVotes - an integer representing the number of users that voted "too fast"
     * @param tooSlowVotes - an integer representing the number of users that voted "too slow"
     * @param numberOfStudents - an integer representing the current total number of users
     */
    public Update(String type, int tooFastVotes, int tooSlowVotes, int numberOfStudents) {
        this.updatedTooFastVotes = tooFastVotes;
        this.updatedTooSlowVotes = tooSlowVotes;
        this.updatedNumberOfStudents = numberOfStudents;
        this.type = type;
    }

    /**Constructor for the Update class.
     * @param type - the type of the update
     * @param numberOfStudents - an integer representing the current total number of users
     */
    public Update(String type, int numberOfStudents) {
        this.updatedNumberOfStudents = numberOfStudents;
        this.type = type;
    }

    /**Getters for all the attributes.
     */
    public String getType() {
        return type;
    }

    public List<Question> getUpdatedQuestions() {
        return updatedQuestions;
    }

    public int getUpdatedNumberOfStudents() {
        return updatedNumberOfStudents;
    }

    public int getUpdatedTooSlowVotes() {
        return updatedTooSlowVotes;
    }

    public int getUpdatedTooFastVotes() {
        return updatedTooFastVotes;
    }

    public Question getUpdatedQuestion() {
        return updatedQuestion;
    }

    /**Setters for all of the attributes.
     */
    public void setType(String type) {
        this.type = type;
    }

    public void setUpdatedQuestions(List<Question> updatedQuestions) {
        this.updatedQuestions = updatedQuestions;
    }

    public void setUpdatedNumberOfStudents(int updatedNumberOfStudents) {
        this.updatedNumberOfStudents = updatedNumberOfStudents;
    }

    public void setUpdatedTooSlowVotes(int updatedTooSlowVotes) {
        this.updatedTooSlowVotes = updatedTooSlowVotes;
    }

    public void setUpdatedTooFastVotes(int updatedTooFastVotes) {
        this.updatedTooFastVotes = updatedTooFastVotes;
    }


    /**Override the default toString method.
     *
     * @return - a String representation of this
     */
    @Override
    public String toString() {
        return "Update<" + this.type + ", " + this.updatedQuestions + ">";
    }
}
