package nl.tudelft.oopp.app.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The abstract class User that is the parent of Student and Moderator.
 */
@Entity
@Table(name = "users")
public abstract class User {

    /**Class responsible for defining the User role.
     * @param id - a unique code that is associated with the user
     * @param username - the name that is assigned to the user
     * @param IP - the IP of the user
     */

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "IP")
    private String ip;

    /**
     * Empty constructor used by the H2 Database.
     */
    public User() {

    }

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

    public String getIP() {
        return ip;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    /**
     * Check if 2 User Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
