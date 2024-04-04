package quick.click.core.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import quick.click.core.domain.model.User;
import quick.click.config.factory.UserFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private AdvertRepository advertRepository;

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
    @DisplayName("When find user by email")
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
    @DisplayName("When user exists with email")
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
    @DisplayName("When find user by id")
    class FindUserByIdTests {

        @Test
        void testFindUserById_shouldReturnExistingUserWithGivenId() {
            User existingUser = userRepository.save(user);
            Optional<User> foundUser = userRepository.findUserById(existingUser.getId());
            assertEquals(existingUser.getId(), foundUser.get().getId());
            assertTrue(foundUser.isPresent());

        }

        @Test
        void testFindUserById_shouldReturnNoUserWithGivenId() {
            Optional<User> foundUser = userRepository.findUserById(user.getId());

            assertFalse(foundUser.isPresent());
            assertThat(foundUser).isEmpty();
        }
    }
    @Nested
    @DisplayName("When find all user")
    class FindALlUseTests {

        @Test
        void testFindAllUser_shouldReturnUserList() {
            advertRepository.deleteAll();
            userRepository.deleteAll();
            userRepository.save(user);
            List<User> userList = userRepository.findAll();

            assertEquals(userList.size(), 1);
            assertFalse(userList.isEmpty());
        }

        @Test
        void testFindAllUser_shouldReturnEmptyUserList() {
            advertRepository.deleteAll();
            userRepository.deleteAll();
            List<User> userList = userRepository.findAll();

            assertEquals(userList.size(), 0);
            assertTrue(userList.isEmpty());
        }
    }

    @Nested
    @DisplayName("When delete user by id")
    class DeleteUserByIdTests {
        @Test
        void testDeleteUserById_shouldDeleteUserById() {
            User userForDelete = userRepository.save(user);

            userRepository.deleteById(userForDelete.getId());
            Optional<User> deletedUser = userRepository.findById(userForDelete.getId());
            assertFalse(deletedUser.isPresent());
            assertThat(deletedUser).isEmpty();
        }

    }

}