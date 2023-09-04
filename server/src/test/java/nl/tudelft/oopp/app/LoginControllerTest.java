package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;

import nl.tudelft.oopp.app.controllers.LoginController;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.repositories.SessionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(LoginController.class)
class LoginControllerTest {

    private Session session;
    private Session session2;
    private Student student;
    private Moderator moderator;

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private LoginController loginController;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        session = new Session("test", new Timestamp(2001));
        session2 = new Session("test2", new Timestamp(2024));
        session.setStudentCode("abcdef");
        session.setModCode("ghijkl");
        session2.setStudentCode("aaa");
        student = new Student("usernameStudent", 10);
        moderator = new Moderator("usernameModerator", 20);
        session.addStudent(student);
        session2.setActive(false);
    }

    @Test
    public void joinRoomTestStudent() throws Exception {

        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/joinRoom?code=abcdef&username=30000"))
                 .andExpect(status().isOk());

    }

    @Test
    public void joinRoomTestModerator() throws Exception {

        when(sessionRepository.findByModCode("ghijkl")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/joinRoom?code=ghijkl&username=30000"))
                .andExpect(status().isOk());

    }

    @Test
    public void joinRoomNullTest() throws Exception {

        mvc.perform(get("http://localhost:8080/joinRoom?code=1&username=30000"))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentCodeTest() throws Exception {
        when(sessionRepository.findByModCode("ghijkl")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/getStudentCode?code=ghijkl"))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentCodeNullRoomTest() throws Exception {
        mvc.perform(get("http://localhost:8080/getStudentCode?code=nullCode"))
                .andExpect(status().isOk());
    }

    @Test
    public void joinRoomBannedUserTest() throws Exception {
        when(sessionRepository.findByStudentCode("aaa")).thenReturn(session2);
        mvc.perform(get("http://localhost:8080/joinRoom?code=aaa&username=30000"))
                .andExpect(status().isOk());
    }

    @Test
    void createRoom() throws Exception {
        mvc.perform(post("http://localhost:8080/createRoom").contentType(MediaType.APPLICATION_JSON)
                .content("room&2018-09-01 09:01:15.0")).andExpect(status().isOk());


    }

    @Test
    void ipIsBannedTest() {
        Session session = new Session("test", new Timestamp(2021));
        String ip =  "255.255.255.255";

        Student student = new Student("username", 42);
        student.setIP(ip);

        session.addBannedUser(student);

        assertEquals(true, loginController.ipIsBanned(session, ip));
    }

    @Test
    void ipIsNotBannedTest() {
        Session session = new Session("test", new Timestamp(2021));
        String ip =  "255.255.255.255";

        Student student = new Student("username", 42);
        student.setIP(ip);

        session.addBannedUser(new Student("username2", 43));

        assertEquals(false, loginController.ipIsBanned(session, ip));
    }
}