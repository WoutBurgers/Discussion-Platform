package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UpdateTest {

    Update u1;
    Update u2;
    List<Question> questions;
    Question q1;
    Question q2;

    @BeforeEach
    void setup() {
        questions = new ArrayList<>();
        q1 = new Question(new Student("student", 50),
                "I want to test the app", new Timestamp(2021));
        q1.setAnswer("answer");
        q2 = new Question(new Student("notStudent", 60),
                "I don't want to test the app", new Timestamp(2021));
        q2.setAnswer("notAnswer");
        questions.add(q1);
        questions.add(q2);

        u1 = new Update("addQuestions", questions);
        u2 = new Update("removeQuestions");

    }

    @Test
    void getTypeTest() {
        assertEquals("addQuestions", u1.getType());
    }

    @Test
    void getUpdatedQuestionsTest() {
        assertEquals(questions, u1.getUpdatedQuestions());
    }

    @Test
    void setTypeTest() {
        u1.setType("new Type");
        assertEquals("new Type", u1.getType());
    }

    @Test
    void setUpdatedQuestionsTest() {
        List<Question> questions2 = new ArrayList<>();
        u2.setUpdatedQuestions(questions2);
        assertEquals(0, u2.getUpdatedQuestions().size());
    }

    @Test
    void testToStringTest() {
        assertEquals("Update<addQuestions, [Question<Student: User<id: 50, username: student>, "
                + "I want to test the app, 0, answer>, "
                + "Question<Student: User<id: 60, username: notStudent>, "
                + "I don't want to test the app, 0, notAnswer>]>", u1.toString());
    }

    @Test
    void getUpdatedQuestionTest() {
        Update update = new Update("removeQuestion", new Question(new Student("user", 42),
                "message", new Timestamp(2020)));
        assertEquals(new Question(new Student("user", 42),
                "message", new Timestamp(2020)), update.getUpdatedQuestion());
    }

    @Test
    void getTooFastVotesTest() {
        Update update = new Update("feedback", 3, 4, 10);
        assertEquals(3, update.getUpdatedTooFastVotes());
    }

    @Test
    void getTooSlowVotesTest() {
        Update update = new Update("feedback", 3, 4, 10);
        assertEquals(4, update.getUpdatedTooSlowVotes());
    }

    @Test
    void getUpdatedNumberOfStudentsTest() {
        Update update = new Update("totalUsers", 42);
        assertEquals(42, update.getUpdatedNumberOfStudents());
    }

    @Test
    void setUpdatedNumberOfStudentsTest() {
        u1.setUpdatedNumberOfStudents(3);
        assertEquals(3, u1.getUpdatedNumberOfStudents());
    }

    @Test
    void setUpdatedTooSlowVotes() {
        u1.setUpdatedTooSlowVotes(10);
        assertEquals(10, u1.getUpdatedTooSlowVotes());
    }

    @Test
    void setUpdatedTooFastVotesTest() {
        u1.setUpdatedTooFastVotes(20);
        assertEquals(20, u1.getUpdatedTooFastVotes());
    }
}