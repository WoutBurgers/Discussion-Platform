package nl.tudelft.oopp.app.entities;

import java.util.Objects;

/**
 * The class User that is the parent of Student and Moderator.
 * (Here it is not abstract since every message contains User object and
 * gson does not know if this should be a student or a mod)
 */
public class User {
    /** Class responsible for defining the User role.
     * @param username - the name that is assigned to the user
     * @param id - a unique code that is associated to the user
     */
    private String username;
    private int id;

    /**
     * Constructs a new User.
     */
    public User(String username, int id) {
        this.username = username;
        this.id = id;
    }

    /**
     * Getters and setters for the attributes of class User.
     */
    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Check if 2 User Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        User user = (User) o;
        return getId() == user.getId()
                && Objects.equals(getUsername(), user.getUsername());
    }

    /**Generate a hash code for the user.
     *
     * @return - the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getId());
    }

    /**Override the default toString method.
     *
     * @return - string representation of the user
     */
    @Override
    public String toString() {
        return "User<id: " + this.id + ", username: " + this.username + ">";
    }
}
