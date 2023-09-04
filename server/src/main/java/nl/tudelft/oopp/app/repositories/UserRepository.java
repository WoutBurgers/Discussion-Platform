package nl.tudelft.oopp.app.repositories;

import java.util.List;
import nl.tudelft.oopp.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface used to make queries on the users table.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**Query to retrieve all users in the database.
     *
     * @return - list of all the users in existence
     */
    List<User> findAll();

    /**Find a user based on the id of the user.
     *
     * @param id - the id of the user
     * @return - the found user
     */
    User findById(int id);
}
