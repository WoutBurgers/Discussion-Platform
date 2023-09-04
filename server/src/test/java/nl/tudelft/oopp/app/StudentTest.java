package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.oopp.app.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class StudentTest {

    Student s1;
    Student s2;
    Student s3;

    @BeforeEach
    private void setup() {
        s1 = new Student("cheng", 99);
        s2 = new Student("cheng", 99);
        s3 = new Student("jnl", 44);
    }

    @Test
    void getUsernameTest() {
        assertEquals("cheng", s1.getUsername());
    }

    @Test
    void getIdTest() {
        assertEquals(99, s1.getId());
    }

    @Test
    void setUsernameTest() {
        s2.setUsername("chengj");
        assertEquals("chengj", s2.getUsername());
    }

    @Test
    void setIdTest() {
        s2.setId(3);
        assertEquals(3, s2.getId());
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
        assertNotEquals(s1,s3);
    }

    @Test
    void isBannedTest() {
        assertEquals(false, s1.isBanned());
    }

    @Test
    void getFeedbackTest() {
        assertEquals(0, s1.getFeedback());
    }

    @Test
    void setBannedStatusTest() {
        s1.setBannedStatus(true);
        assertEquals(true, s1.isBanned());
    }

    @Test
    void giveFeedbackTest() {
        s1.giveFeedback(2);
        assertEquals(2, s1.getFeedback());
    }

    @Test
    void toStringTest() {
        assertEquals("Student: User<id: 99, username: cheng>", s1.toString());
    }

    @Test
    void hashTest() {
        assertTrue(s1.hashCode() == s2.hashCode());
    }

    @Test
    void equalsNullTest() {
        Student student = null;
        assertNotEquals(s1, student);
    }

}