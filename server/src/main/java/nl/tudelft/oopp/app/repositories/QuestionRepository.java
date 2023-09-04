package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface used to make queries on the questions table.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    /**Find a question based on the id of the question.
     *
     * @param id - the id of the question
     * @return - the found question
     */
    public Question findById(int id);


}
