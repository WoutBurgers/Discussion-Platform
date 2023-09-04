package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.User;

import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.SessionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class DatabaseTest {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    Session s1;
    Session s2;
    Session s3;
    Student st1;
    Student st2;
    Moderator m1;
    Moderator m2;
    Question q1;
    Question q2;

    @BeforeEach
    void setup() {
        s1 = new Session("session1", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode1", "modCode1");
        s2 = new Session("session2", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode2", "modCode2");
        s3 = new Session("session3", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode3", "modCode3");

        st1 = new Student("user1", 1);
        st2 = new Student("user2", 2);

        m1 = new Moderator("mod1", 3);
        m2 = new Moderator("mod2", 4);

        Student savedSt1 = userRepository.save(st1);
        Student savedSt2 = userRepository.save(st2);

        q1 = new Question(savedSt1, "Question1", new Timestamp(2000, 12, 12, 0, 0, 0, 0));
        q2 = new Question(savedSt2, "Question1", new Timestamp(2000, 12, 12, 0, 0, 0, 0));

        questionRepository.save(q1);
        questionRepository.save(q2);

        userRepository.save(m1);
        userRepository.save(m2);

        s1.addQuestion(q1);

        s2.addQuestion(q1);
        s2.addQuestion(q2);

        s1.addStudent(st1);
        s1.addStudent(st2);

        s2.addStudent(st2);

        sessionRepository.save(s1);
        sessionRepository.save(s2);
        sessionRepository.save(s3);
    }

    @Test
    void testFindByStudentCode() {
        Session retrieved = sessionRepository.findByStudentCode("studentCode2");
        assertEquals(s2, retrieved);
    }

    @Test
    void testFindByModCode() {
        Session retrieved = sessionRepository.findByModCode("modCode3");
        assertEquals(s3, retrieved);
    }

    @Test
    void testFindAll() {
        List<Session> retrieved = sessionRepository.findAll();
        assertEquals(List.of(s1, s2, s3), retrieved);
    }

    @Test
    void testWithUsers() {
        Session expected = new Session("session1", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode1", "modCode1");

        expected.addStudent(st1);
        expected.addStudent(st2);

        expected.addQuestion(q1);

        Session retrieved = sessionRepository.findByStudentCode("studentCode1");

        assertEquals(expected, retrieved);
    }

    @Test
    void testWithUsersNotEquals() {
        Session expected = new Session("session1", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode1", "modCode1");

        expected.addStudent(st1);

        Session retrieved = sessionRepository.findByStudentCode("studentCode1");

        assertNotEquals(expected, retrieved);
    }

    @Test
    void testWithUsersQuestionsNotEquals() {
        Session expected = new Session("session1", new Timestamp(120, 12, 11, 0, 0, 0, 0),
                "studentCode1", "modCode1");

        expected.addStudent(st1);
        expected.addStudent(st2);

        Session retrieved = sessionRepository.findByStudentCode("studentCode1");

        assertNotEquals(expected, retrieved);
    }

    @Test
    void testRetrievedQuestions() {
        Session retrieved = sessionRepository.findByStudentCode("studentCode1");

        assertNotEquals(List.of(q1, q2), retrieved.getQuestions());
    }

    @Test
    void testAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(st1);
        users.add(st2);
        users.add(m1);
        users.add(m2);

        assertEquals(users, userRepository.findAll());
    }
}
