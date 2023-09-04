package nl.tudelft.oopp.app.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * The abstract class Message that is the parent of Answer and Question.
 */
public abstract class Message {

    /**Class responsible for sending a message by the User.
     * @param id - unique id for the message
     * @param user - the User that sends the message
     * @param message - the String containing the message of the user
     * @param date - the date at which the message is sent
     */

    private int id;

    private User user;

    private String message;

    private Timestamp date;


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
     * A getter for the time in the format hh:mm.
     */
    public String getTime() {
        String time = "";

        if (getDate().getHours() <= 9 && getDate().getMinutes() >= 10) {
            time += "0" + date.getHours() + ":" + date.getMinutes();
        } else if (getDate().getHours() >= 10 && getDate().getMinutes() <= 9) {
            time += date.getHours() + ":0" + date.getMinutes();
        } else if (getDate().getHours() <= 9 && getDate().getMinutes() <= 9) {
            time += "0" + date.getHours() + ":0" + date.getMinutes();
        } else {
            time += date.getHours() + ":" + date.getMinutes();
        }

        return time;
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
