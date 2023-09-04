package nl.tudelft.oopp.app.entities;

import java.util.Objects;

/**
 * The Moderator class.
 */
public class Moderator extends User {

    /** Class responsible for defining the Moderator role.
     * @param teacherView - set a preferential UI for the Teacher,
     *                    that only displays the top 3 questions.
     */
    private boolean teacherView;

    /**
     * Constructs a new Moderator.
     */
    public Moderator(String username, int id) {
        super(username, id);
        this.teacherView = false;
    }

    /**
     * Getter for the attribute of Class Moderator.
     */
    public boolean isTeacherView() {
        return teacherView;
    }

    /**
     * Method used to switch to the TeacherView mode.
     */
    public void setTeacherView(boolean teacherView) {
        this.teacherView = teacherView;
    }

    /**
     * Check if 2 Moderator Objects are equal.
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
        Moderator moderator = (Moderator) o;
        return isTeacherView() == moderator.isTeacherView();
    }

    /**Generate a hash code for the moderator.
     *
     * @return - the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isTeacherView());
    }

    /**Override the default toString method.
     *
     * @return - string representation of the moderator
     */
    @Override
    public String toString() {
        return "Moderator: " + super.toString();
    }
}
