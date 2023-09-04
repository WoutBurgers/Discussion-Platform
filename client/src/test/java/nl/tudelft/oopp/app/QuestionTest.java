package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import jdk.jfr.Timespan;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class QuestionTest {
    Student s1;
    Student s2;
    Question q1;
    Question q2;
    Question q3;
    Question q4;
    Question q5;

    @BeforeEach
    private void setup() {
        s1 = new Student("jnl", 44);
        s2 = new Student("jnl", 44);
        q1 = new Question(s1, "world", new Timestamp(44));
        q2 = new Question(s1, "world", new Timestamp(44));
        q3 = new Question(s2, "planet", new Timestamp(44));
        q4 = new Question(s2, "planet", new Timestamp(2020,
                11, 11, 03, 03, 0, 0));
        q5 = new Question(s2, "planet", new Timestamp(2020,
                11, 11, 12, 30, 0, 0));

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
    void testToString() {
        q1.setAnswer("This is the answer");
        assertEquals("Question<Student: User<id: 44, username: jnl>, "
                + "world, 0, This is the answer>", q1.toString());
    }

    @Test
    void hashTest() {
        assertTrue(q1.hashCode() == q2.hashCode());
    }

    @Test
    void isSelectedTest() {
        q1.setSelected(true);
        assertEquals(true, q1.isSelected());
    }

    @Test
    void equalsNullTest() {
        Question question = null;
        assertNotEquals(q1, question);
    }

    @Test
    void setMessageTest() {
        q1.setMessage("testtest");
        assertEquals("testtest", q1.getMessage());
    }

    @Test
    void setDateTest() {
        q1.setDate(new Timestamp(1999));
        assertEquals(new Timestamp(1999), q1.getDate());

    }

    @Test
    void setUserTest() {
        Student student = new Student("name", 2);
        q1.setUser(student);
        assertEquals(student, q1.getUser());
    }

    @Test
    void isUpvotedTest() {
        assertEquals(false, q1.isUpvoted());
    }

    @Test
    void setUpvotedTest() {
        q1.setUpvoted(true);
        assertEquals(true, q1.isUpvoted());
    }

    @Test
    void getTimeTest() {
        String time = "12:30";
        assertEquals(time, q5.getTime());
    }

    @Test
    void getTimeZerosTest() {
        String time = "03:03";
        assertEquals(time, q4.getTime());

    }

    @Test
    void calculateSortVariableTest() {
        assertEquals(q1.getUpvotes() - 0.0000033
                        * (System.currentTimeMillis() - q1.getDate().getTime()),
                q1.calculateSortVariable());
    }

    @Test
    void toJsonTest() {
        assertEquals(q1.toJson(), q2.toJson());
    }

}
