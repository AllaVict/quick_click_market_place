package quick.click.core.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import quick.click.config.factory.UserFactory;
import quick.click.core.domain.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileReferenceRepository fileReferenceRepository;

    private static final String EMAIL =  "test@example.com";

    private User user;

    @BeforeEach
    public void setUp() {
        user = UserFactory.createUser();
    }

    @Nested
    @DisplayName("When find User By Email")
    class FindUserByEmailTests {
        @Test
        void testFindUserByEmail_shouldReturnExistingUserWithGivenEmail() {
            userRepository.save(user);
            Optional<User> foundUser = userRepository.findUserByEmail(EMAIL);

            assertTrue(foundUser.isPresent());
            assertEquals(EMAIL, foundUser.get().getEmail());
        }

        @Test
        void testFindUserByEmail_shouldReturnNoUserWithGivenEmail() {
            Optional<User> foundUser = userRepository.findUserByEmail(EMAIL);

            assertFalse(foundUser.isPresent());
            assertThat(foundUser).isEmpty();
        }

    }
    @Nested
    @DisplayName("When User Exists with Email")
    class ExistsByEmailTests {
        @Test
        void testExistsByEmail_shouldReturnTrue() {
            userRepository.save(user);
            boolean exists = userRepository.existsByEmail(EMAIL);

            assertTrue(exists);
        }

        @Test
        void testExistsByEmail_shouldReturnFalse() {
            boolean exists = userRepository.existsByEmail(EMAIL);

            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("When Find User by Id")
    class FindUserByIdTests {

        @Test
        void testFindUserById_shouldReturnExistingUserWithGivenId() {
            userRepository.save(user);
            Optional<User> foundUser = userRepository.findUserById(user.getId());

            assertTrue(foundUser.isPresent());
            assertEquals(user.getId(), foundUser.get().getId());
        }

        @Test
        void testFindUserById_shouldReturnNoUserWithGivenId() {
            Optional<User> foundUser = userRepository.findUserById(user.getId());

            assertFalse(foundUser.isPresent());
            assertThat(foundUser).isEmpty();
        }
    }

}