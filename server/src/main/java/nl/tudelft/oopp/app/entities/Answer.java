package nl.tudelft.oopp.app.entities;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The class Answer.
 */
public class Answer extends Message {
    /**
     * No new attributes compared to it's parent,
     * but used by the moderator to respond to a question.
     * Constructs a new Answer.
     */
    public Answer(User user, String message, Timestamp date) {
        super(user, message, date);
    }

    /**
     * Check if 2 Answer Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Answer) {
            Answer other = (Answer) o;
            return super.equals(other);
        }

        return false;
    }
}
