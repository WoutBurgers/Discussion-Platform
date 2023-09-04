package nl.tudelft.oopp.app.repositories;

import java.util.List;

import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface used to make queries on the sessions table.
 */
public interface SessionRepository extends JpaRepository<Session, Integer> {

    /**Find all sessions in existence.
     *
     * @return - the list of all sessions
     */
    List<Session> findAll();

    /**Find the session corresponding to the student code.
     *
     * @param studentCode - the student code
     * @return - the session which can be joined into as a student with the student code
     */
    Session findByStudentCode(String studentCode);

    /**Find the session corresponding to the moderator code.
     *
     * @param modCode - the mod code
     * @return - the session which can be joined into as a mod with the mod code
     */
    Session findByModCode(String modCode);
}
