package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.util.Date;

import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class QuestionTest {
    Student s1;
    Question q1;
    Question q2;
    Question q3;

    @BeforeEach
    private void setup() {
        s1 = new Student("jnl", 44);
        q1 = new Question(s1, "world", new Timestamp(44));
        q2 = new Question(s1, "world", new Timestamp(44));
        q3 = new Question(s1, "planet", new Timestamp(44));

    }

    @Test
    void isAnsweredTest() {
        assertEquals(false, q1.isAnswered());
    }

    @Test
    void setAnsweredTest() {
        q1.setAnswered(true);
        assertEquals(true, q1.isAnswered());
    }

    @Test
    void getUpvotesTest() {
        assertEquals(0, q1.getUpvotes());
    }

    @Test
    void getAgeTest() {
        assertEquals(0, q1.getAge());
    }

    @Test
    void setAgeTest() {
        q1.setAge(22);
        assertEquals(22, q1.getAge());
    }

    @Test
    void upvoteQuestionTest() {
        q1.upvoteQuestion();
        q1.upvoteQuestion();
        q1.upvoteQuestion();

        assertEquals(3, q1.getUpvotes());

    }

    @Test
    void testEquals() {
        assertEquals(q1, q2);
    }

    @Test
    void testEqualsSame() {
        assertEquals(q1, q1);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(q1, q3);
    }

    @Test
    void equalsNullTest() {
        Question question = null;
        assertNotEquals(q1, question);
    }

    @Test
    void hashTest() {
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void toStringTest() {
        q1.setAnswer("Is this the answer?");
        assertEquals("Question<Student: User<id: 44, username: jnl>, "
                + "world, 0, Is this the answer?>", q1.toString());
    }

    @Test
    void setUserTest() {
        Student student = new Student("name", 2);
        q1.setUser(student);
        assertEquals(student, q1.getUser());
    }
}