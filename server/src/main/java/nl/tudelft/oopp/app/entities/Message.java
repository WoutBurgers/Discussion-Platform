package nl.tudelft.oopp.app.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


/**
 * The abstract class Message that is the parent of Answer and Question.
 */
@MappedSuperclass
public abstract class Message {
    /**Class responsible for sending a message by the User.
     * @param id - unique id for the message
     * @param user - the User that sends the message
     * @param message - the String containing the message of the user
     * @param date - the date at which the message is sent
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private Timestamp date;

    /**
     * Empty constructor used by the H2 Database.
     */
    public Message() {

    }

    /**
     * Constructs a new Message.
     */
    public Message(User user, String message, Timestamp date) {
        this.user = user;
        this.message = message;
        this.date = date;
    }

    /**
     * Getters and Setters for the attributes of class Message.
     */

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }


    /**
     * Check if 2 Message Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message1 = (Message) o;
        return Objects.equals(getUser(), message1.getUser())
                && Objects.equals(getMessage(), message1.getMessage())
                && Objects.equals(getDate(), message1.getDate());
    }

    /**Generate a hash code for the message.
     *
     * @return - the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getMessage(), getDate());
    }
}
