package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class SessionTest {

    Session s1;
    Session s2;
    Session s3;
    Student student;
    Moderator mod;
    List<Student> students;
    List<Moderator> mods;

    @BeforeEach
    private void setup() {
        s1 = new Session("session1", new Timestamp(2001), "xzczx", "xzczx");
        s2 = new Session("session1", new Timestamp(2001), "xzczx", "xzczx");
        s3 = new Session("session1", new Timestamp(1999));
        student = new Student("cheng", 99);
        s1.getStudents().add(student);
        s2.getStudents().add(student);
        mod = new Moderator("jnl", 743);
        s1.getModerators().add(mod);
        s2.getModerators().add(mod);

        students = new ArrayList<>();
        mods = new ArrayList<>();
        students.add(student);
        mods.add(mod);

    }

    @Test
    void isActiveTest() {
        assertEquals(true, s1.isActive());
    }

    @Test
    void setActiveTest() {
        s1.setActive(false);
        assertEquals(false, s1.isActive());
    }

    @Test
    void getDateTest() {
        assertEquals(new Timestamp(2001), s1.getDate());
    }

    @Test
    void setDateTest() {
        s1.setDate(new Timestamp(1998));
        assertEquals(new Timestamp(1998), s1.getDate());
    }

    @Test
    void getStudentCodeTest() {
        assertEquals("xzczx", s1.getStudentCode());
    }


    @Test
    void setStudentCodeTest() {
        s1.setStudentCode("casaaseq2");
        assertEquals("casaaseq2", s1.getStudentCode());
    }

    @Test
    void getModCodeTest() {
        assertEquals("xzczx", s1.getModCode());
    }

    @Test
    void setModCodeTest() {
        s1.setModCode("p,nvc");
        assertEquals("p,nvc", s1.getModCode());
    }

    @Test
    void getStudentsTest() {
        assertEquals(students, s1.getStudents());
    }

    @Test
    void getModeratorsTest() {
        assertEquals(mods, s1.getModerators());
    }

    @Test
    void generateRandomStudentCodeTest() {
        String random = s1.generateRandomCode();
        s1.setStudentCode(random);
        assertEquals(random, s1.getStudentCode());
    }

    @Test
    void generateRandomModeratorCodeTest() {
        String random = s1.generateRandomCode();
        s1.setModCode(random);
        assertEquals(random, s1.getModCode());
    }

    @Test
    void testEquals() {
        assertEquals(s1, s2);
    }

    @Test
    void testEqualsSame() {
        assertEquals(s1, s1);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(s1, s3);
    }

    @Test
    void testEmptyConstructor() {
        Session session = new Session();
        assertNotNull(session);
    }

    @Test
    void testGetId() {
        assertEquals(0, s1.getId());
    }

    @Test
    void testGetName() {
        s1.setName("test");
        assertEquals("test", s1.getName());
    }

    @Test
    void testQuestionsList() {
        Session session = new Session();
        List<Question> questions = new ArrayList<>();
        session.setQuestions(questions);
        session.addQuestion(new Question(student, "test", new Timestamp(1999)));

        assertEquals(1, session.getQuestions().size());
    }

    @Test
    void testModeratorList() {
        Session session = new Session();
        List<Moderator> moderators = new ArrayList<>();
        session.setModerators(moderators);
        session.addModerator(mod);

        assertEquals(1, session.getModerators().size());

    }

    @Test
    void testStudentsList() {
        Session session = new Session();
        List<Student> studentsList = new ArrayList<>();
        session.setStudents(studentsList);
        session.addStudent(student);

        assertEquals(1, session.getStudents().size());
    }

    @Test
    void testToString() {
        assertEquals("Session<id: 0, studentCode: xzczx, modCode: xzczx>", s1.toString());
    }

    @Test
    void hashTest() {
        assertTrue(s1.hashCode() == s2.hashCode());
    }

    @Test
    void equalsNullTest() {
        Session session = null;
        assertNotEquals(s1, session);
    }
}