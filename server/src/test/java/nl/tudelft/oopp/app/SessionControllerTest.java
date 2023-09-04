package nl.tudelft.oopp.app;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.Timestamp;
import java.util.List;

import nl.tudelft.oopp.app.controllers.SessionController;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
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


@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private ObjectMapper mapper;
    private Session session;
    private Moderator moderator;
    private Question question;
    private Question question2;
    private Student student;
    private Student student2;

    @Autowired
    private MockMvc mvc;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
     QuestionRepository questionRepository;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        session = new Session("test", new Timestamp(2001));
        session.setStudentCode("abcdef");
        session.setModCode("ghijkl");
        student = new Student("usernameStudent", 10);
        student2 = new Student("user", 42);
        moderator = new Moderator("usernameModerator", 20);
        mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        question = new Question(student, "test", new Timestamp(2022));
        question2 = new Question(student2, "test2", new Timestamp(2012));


        session.addStudent(student);
        session.addStudent(student2);
        session.addModerator(moderator);
        session.addQuestion(question);
        session.addQuestion(question2);
    }

    @Test
    void requestQuestionsStudentTest() throws Exception {
        mvc.perform(get("http://localhost:8080/room/abcdef/requestQuestions?userId=10"))
                .andExpect(status().isOk());

        List<Question> questionList = List.of(question, question2);
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        when(questionRepository.findAll()).thenReturn(questionList);

        mvc.perform(get("http://localhost:8080/room/abcdef/requestQuestions?userId=10"))
                .andExpect(status().isOk());
    }

    @Test
    void requestQuestionsModTest() throws Exception {
        Question question2 = new Question(student, "test2", new Timestamp(2021));

        List<Question> questionList = List.of(question, question2);
        when(sessionRepository.findByModCode("ghijkl")).thenReturn(session);
        when(questionRepository.findAll()).thenReturn(questionList);

        mvc.perform(get("http://localhost:8080/room/ghijkl/requestQuestions?userId=20"))
                .andExpect(status().isOk());
    }

    @Test
    void getUpdatesTest() throws Exception {
        mvc.perform(get("http://localhost:8080/room/abcdef/getUpdates?userId=10"))
                .andExpect(status().isOk());


    }

    @Test
    void addQuestionTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = mapper.writeValueAsString(question);
        mvc.perform(post("http://localhost:8080/room/abcdef/addQuestion?userId=10")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                        .andExpect(status().isOk()).andReturn();

    }

    @Test
    void addQuestionInactiveRoomTest() throws Exception {
        session.setActive(false);
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = mapper.writeValueAsString(question);
        mvc.perform(post("http://localhost:8080/room/abcdef/addQuestion?userId=10")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void closeRoomTest() throws Exception {
        when(sessionRepository.findByModCode("ghijkl")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/room/ghijkl/closeRoom"))
                .andExpect(status().isOk());

    }

    @Test
    void closeInactiveRoomTest() throws Exception {
        session.setActive(false);
        when(sessionRepository.findByModCode("ghijkl")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/room/ghijkl/closeRoom"))
                .andExpect(status().isOk());

    }

    @Test
    void leaveRoomTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = student.getId() + "&1&0";
        mvc.perform(post("http://localhost:8080/room/abcdef/leaveRoom")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void deleteQuestionsTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);

        when(questionRepository.findById(question.getId())).thenReturn(question);
        when(questionRepository.findById(question2.getId())).thenReturn(question2);

        String requestBody = question.getId() + "&" + question2.getId();

        mvc.perform(post("http://localhost:8080/room/abcdef/deleteQuestions")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void updateFeedbackVariablesTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = 2 + "&" + 3;

        mvc.perform(post("http://localhost:8080/room/abcdef/updateFeedbackVariables")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void initializeFeedbackVariablesTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        mvc.perform(get("http://localhost:8080/room/abcdef/initializeFeedbackVariables"))
                .andExpect(status().isOk());
    }

    @Test
    void banUsersTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = student.getId() + "&" + student2.getId();

        mvc.perform(post("http://localhost:8080/room/abcdef/banUsers")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void upvoteQuestionTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        Question newQuestion = new Question(student, "questionToBeUpvoted", new Timestamp(2021));
        String requestBody = newQuestion.getId() + "";
        session.addQuestion(newQuestion);

        when(questionRepository.findById(newQuestion.getId())).thenReturn(newQuestion);

        mvc.perform(post("http://localhost:8080/room/abcdef/upvoteQuestion")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void upvoteQuestionInactiveRoomTest() throws Exception {
        Session newSession = new Session("room", new Timestamp(2900));
        newSession.setActive(false);
        newSession.setStudentCode("aaa");
        when(sessionRepository.findByStudentCode("aaa")).thenReturn(newSession);
        Question newQuestion = new Question(student, "questionToBeUpvoted", new Timestamp(2021));
        String requestBody = newQuestion.getId() + "";
        session.addQuestion(newQuestion);

        when(questionRepository.findById(newQuestion.getId())).thenReturn(newQuestion);

        mvc.perform(post("http://localhost:8080/room/aaa/upvoteQuestion")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void markAnsweredTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        Question newQuestion = new Question(student,
                "questionToBeMarkedAnswered", new Timestamp(2021));
        String requestBody = newQuestion.getId() + "";
        session.addQuestion(newQuestion);

        when(questionRepository.findById(newQuestion.getId())).thenReturn(newQuestion);

        mvc.perform(post("http://localhost:8080/room/abcdef/markAnswered")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void rephraseQuestionTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        Question newQuestion = new Question(student,
                "questionToBeMarkedAnswered", new Timestamp(2021));
        String requestBody = "Rephrased";
        session.addQuestion(newQuestion);

        when(questionRepository.findById(newQuestion.getId())).thenReturn(newQuestion);

        mvc.perform(post("http://localhost:8080/room/abcdef/rephraseQuestion?questionId=" + newQuestion.getId())
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void answerQuestionTest() throws Exception {
        when(sessionRepository.findByStudentCode("abcdef")).thenReturn(session);
        String requestBody = "Answered";

        when(questionRepository.findById(question.getId())).thenReturn(question);

        mvc.perform(post("http://localhost:8080/room/abcdef//answerQuestion?questionId=" + question.getId())
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();
    }

}