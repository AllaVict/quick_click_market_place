package quick.click.advertservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.advertservice.core.domain.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserById(Long id);

}
