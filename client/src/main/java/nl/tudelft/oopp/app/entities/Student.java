package nl.tudelft.oopp.app.entities;

import java.util.Objects;

/**
 * The Student class.
 */
public class Student extends User {
    /** Class responsible for defining the Student role.
     * @param banned - parameter that describes the ability to write in the chat
     * @param feedback - a parameter that describe the pace of the current lecture,
     *              in the eyes of the Student
     *                 0 - not voted yet
     *                 1 - too fast
     *                 2 - too slow
     */
    private boolean banned;
    private int feedback;

    /**
     * Constructs a new Student.
     */
    public Student(String username, int id) {
        super(username, id);
        this.banned = false;
        this.feedback = 0;
    }

    /**
     * Getters for the attributes of class Student.
     */
    public boolean isBanned() {
        return banned;
    }

    public int getFeedback() {
        return feedback;
    }

    /**
     * Method to unban or ban a user.
     */
    public void setBannedStatus(boolean banned) {
        this.banned = banned;
    }

    /**
     * Method used to give the feedback to the lecturer.
     */
    public void giveFeedback(int feedback) {
        this.feedback = feedback;
    }

    /**
     * Check if 2 Student Objects are equal.
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
        Student student = (Student) o;
        return isBanned() == student.isBanned()
                && getFeedback() == student.getFeedback();
    }

    /**Generate a hash code for the student.
     *
     * @return - the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isBanned(), getFeedback());
    }

    /**Override the default toString method.
     *
     * @return - string representation of the student
     */
    @Override
    public String toString() {
        return "Student: " + super.toString();
    }
}
