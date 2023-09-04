package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.oopp.app.entities.Moderator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class ModeratorTest {

    Moderator m1;
    Moderator m2;
    Moderator m3;

    @BeforeEach
    private void setup() {
        m1 = new Moderator("MR M", 23);
        m2 = new Moderator("Perfect number", 42);
        m3 = new Moderator("MR M", 23);
    }

    @Test
    void isTeacherViewTest() {
        assertEquals(false, m1.isTeacherView());
    }

    @Test
    void setTeacherViewTest() {
        m1.setTeacherView(true);
        assertEquals(true, m1.isTeacherView());
    }

    @Test
    void testEquals() {
        assertEquals(m1, m3);
    }

    @Test
    void testEqualsSame() {
        assertEquals(m1, m1);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(m1, m2);
    }

    @Test
    void testToString() {
        assertEquals("Moderator: User<id: 23, username: MR M>", m1.toString());
    }

    @Test
    void hashTest() {
        assertTrue(m1.hashCode() == m3.hashCode());
    }

    @Test
    void equalsNullTest() {
        Moderator moderator = null;
        assertNotEquals(m1, moderator);
    }
}