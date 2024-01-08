package quick.click.core.service;

import quick.click.core.domain.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user);

    void update(User user);

    User findById(Long userId);

    /**
     *Completely deletes user record
     * @return {@code true} if operation was successful, {@code false} if user can not be completely deleted
     * 	according to his role.
     */

    boolean completelyDeleteById(Long userId);
    /**
     *  Clear all information about user and set user's status to 'DELETED'.
     */

    void deleteByIdComplete(Long userId);

}
