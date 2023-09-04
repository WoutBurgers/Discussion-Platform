package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.util.Date;
import nl.tudelft.oopp.app.entities.Answer;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerTest {

    Answer a1;
    Answer a2;
    Answer a3;
    Moderator m1;

    @BeforeEach
    private void setup() {
        m1 = new Moderator("MR M", 23);
        a1 = new Answer(m1, "hello", new Timestamp(2022));
        a2 = new Answer(m1, "hello", new Timestamp(2022));
        a3 = new Answer(m1, "Goodbye", new Timestamp(2023));

    }

    @Test
    void getUserTest() {
        assertEquals(m1, a1.getUser());
    }

    @Test
    void getMessageTest() {
        assertEquals("hello", a1.getMessage());
    }

    @Test
    void getDateTest() {
        assertEquals(new Timestamp(2022), a1.getDate());
    }

    @Test
    void setMessageTest() {
        a1.setMessage("hi");
        assertEquals("hi", a1.getMessage());
    }

    @Test
    void setDateTest() {
        a1.setDate(new Timestamp(2000));
        assertEquals(new Timestamp(2000), a1.getDate());
    }

    @Test
    void testEquals() {
        assertEquals(a1, a2);
    }

    @Test
    void testEqualsSame() {
        assertEquals(a1, a1);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(a1, a3);
    }

    @Test
    void testNotInstanceOfAnswerEquals() {
        assertNotEquals(a1, new Question());
    }

}