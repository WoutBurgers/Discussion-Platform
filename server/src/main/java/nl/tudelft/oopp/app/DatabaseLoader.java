package nl.tudelft.oopp.app;

import java.sql.Timestamp;
import java.util.Date;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.SessionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Class used to put arbitrary data into the database.
 */
@Service
public class DatabaseLoader {
    /**
     * Constructor for the DatabaseLoader.
     * Used now for testing
     * @param sessionRepository - the Session Repository
     * @param questionRepository - the Question Repository
     * @param userRepository - the User Repository
     */
    public DatabaseLoader(SessionRepository sessionRepository,
                          QuestionRepository questionRepository,
                          UserRepository userRepository) {

        //Student st1 = new Student("user0", 123);
        //Student st2 = new Student("user1", 126);
        //Student st3 = new Student("user2", 128);

        //userRepository.save(st1);
        //userRepository.save(st2);
        //userRepository.save(st3);

        //Question newQuestion = new Question(st1, "First",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));
        //Question newQuestion2 = new Question(st2, "Second",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));
        //Question newQuestion3 = new Question(st3, "Third",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));
        //Question newQuestion4 = new Question(st3, "Lorem ipsum dolor sid est laborum.",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));
        //Question newQuestion5 = new Question(st3, "Fifth",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));
        //Question newQuestion6 = new Question(st3, "Sixth",
                //new Timestamp(121, 12, 4, 0, 0, 0, 0));


        //questionRepository.save(newQuestion);
        //questionRepository.save(newQuestion2);
        //questionRepository.save(newQuestion3);
        //questionRepository.save(newQuestion4);
        //questionRepository.save(newQuestion5);
        //questionRepository.save(newQuestion6);

        //Session s1 = new Session("session1", new Timestamp(121, 2, 3,0,0,0,0),
                //"abcdef", "ghijkl");

        //s1.addQuestion(newQuestion);
        //s1.addQuestion(newQuestion2);
        //s1.addQuestion(newQuestion3);
        //s1.addQuestion(newQuestion4);
        //s1.addQuestion(newQuestion5);
        //s1.addQuestion(newQuestion6);

        //s1.addStudent(st1);
        //s1.addStudent(st2);
        //s1.addStudent(st3);

        //sessionRepository.save(s1);
    }
}
