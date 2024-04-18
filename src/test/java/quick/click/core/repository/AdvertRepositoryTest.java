package quick.click.core.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import quick.click.config.factory.AdvertFactory;
import quick.click.config.factory.UserFactory;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdvertRepositoryTest {

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long ADVERT_ID = 101L;

    private Advert expectedAdvertOne;

    private Advert expectedAdvertTwo;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(UserFactory.createUser());
        expectedAdvertOne = AdvertFactory.createAdvertOne(user);
        expectedAdvertTwo = AdvertFactory.createAdvertTwo(user);
    }

    @Nested
    @DisplayName("When find advert by id")
    class FindAdvertByIdTests {

        @Test
        @Transactional
        void testFindAdvertById_shouldReturnExistingAdvertWithGivenId() {
            Advert saveAdvert = advertRepository.save(expectedAdvertOne);

            Optional<Advert> foundAdvert = advertRepository.findAdvertById(saveAdvert.getId());

            assertTrue(foundAdvert.isPresent());
            assertEquals(expectedAdvertOne.getId(), foundAdvert.get().getId());
        }

        @Test
        void testFindUserById_shouldReturnNoAdvertWithGivenId() {
            Optional<Advert> foundAdvert = advertRepository.findAdvertById(ADVERT_ID);

            assertFalse(foundAdvert.isPresent());
            assertThat(foundAdvert).isEmpty();
        }
    }

    @Nested
    @DisplayName("When find all adverts")
    class FindAllAdvertsTests {
        @Test
        @Transactional
        void testFindAllAdverts_shouldReturnAllAdverts() {
            advertRepository.deleteAll();
            Advert savedAdvertOne = advertRepository.save(expectedAdvertOne);
            Advert savedAdvertTwo = advertRepository.save(expectedAdvertTwo);

            List<Advert> result = advertRepository.findAll();

            assertNotNull(result);
            assertThat(result).isNotNull();
            assertEquals(2, result.size());
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(savedAdvertOne, savedAdvertTwo);
        }

        @Test
        void testFindAllAdverts_shouldReturnEmptyAdvertList() {
            advertRepository.deleteAll();
            List<Advert> result = advertRepository.findAll();

            assertThrows(DataIntegrityViolationException.class, () -> advertRepository.save(new Advert()));
            assertEquals(0, result.size());
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("When find all adverts by created date desc")
    class FindAllByOrderByCreatedDateDescTests {

        @Test
        @Transactional
        void testFindAllByOrderByCreatedDateDesc_shouldReturnAllAdverts() {
            advertRepository.deleteAll();
            expectedAdvertOne.setCreatedDate(LocalDateTime.now());
            Advert savedAdvertOne = advertRepository.save(expectedAdvertOne);
            Advert savedAdvertTwo = advertRepository.save(expectedAdvertTwo);

            List<Advert> result = advertRepository.findAllByOrderByCreatedDateDesc();

            assertNotNull(result);
            assertThat(result).isNotNull();
            assertEquals(2, result.size());
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(savedAdvertOne, savedAdvertTwo);
        }

        @Test
        void testFindByOrderByCreatedDateDesc_shouldReturnEmptyAdvertList() {
            advertRepository.deleteAll();
            List<Advert> result = advertRepository.findAllByOrderByCreatedDateDesc();

            assertThrows(DataIntegrityViolationException.class, () -> advertRepository.save(new Advert()));
            assertEquals(0, result.size());
            assertThat(result).isEmpty();
        }

    }
    @Nested
    @DisplayName("When find all adverts by user sorted by CreatedDateDesc")
    class FindAllAdvertsByUserIdTests {
        @Test
        @Transactional
        void testFindAllAdvertsByUserId_shouldReturnAllAdverts() {
            advertRepository.deleteAll();
            Advert savedAdvertOne = advertRepository.save(expectedAdvertOne);
            Advert savedAdvertTwo = advertRepository.save(expectedAdvertTwo);

            List<Advert> result = advertRepository.findAllByUserOrderByCreatedDateDesc(user);

            assertThat(result).containsExactlyInAnyOrder(savedAdvertOne, savedAdvertTwo);
            assertNotNull(result);
            assertThat(result).isNotNull();
            assertEquals(2, result.size());
            assertThat(result).hasSize(2);
        }

        @Test
        void testFindAllAdvertsByUserId_shouldReturnEmptyAdvertList() {
            advertRepository.deleteAll();
            List<Advert> result = advertRepository.findAllByUserOrderByCreatedDateDesc(user);

            assertThrows(DataIntegrityViolationException.class, () -> advertRepository.save(new Advert()));
            assertEquals(0, result.size());
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("When save a advert")
    class SaveAdvertTests {

        @Test
        void testSaveAdvert_shouldReturnSavedAdvert() {
            Advert savedAdvert = advertRepository.save(expectedAdvertOne);

            assertNotNull(savedAdvert);
            assertThat(savedAdvert.getId()).isNotNull();
            assertThat(savedAdvert.getTitle()).isEqualTo(expectedAdvertOne.getTitle());
            assertEquals(expectedAdvertOne.getId(), savedAdvert.getId());
            assertEquals(expectedAdvertOne.getUser(), savedAdvert.getUser());
        }

        @Test
        void testSaveAdvert_shouldThrowException() {
            assertThrows(DataIntegrityViolationException.class,
                    () -> advertRepository.save(new Advert()));

        }
    }

    @Nested
    @DisplayName("When delete advert")
    class DeleteAdvertTests {
        @Test
        @Transactional
        void testDeleteAdvert_shouldDeleteAdvert() {
            advertRepository.deleteAll();
            advertRepository.save(expectedAdvertTwo);
            Advert advertForDelete = advertRepository.save(expectedAdvertOne);

            advertRepository.deleteById(advertForDelete.getId());
            Optional<Advert> exitingAdvert = advertRepository.findById(advertForDelete.getId());
            List<Advert> adverts = advertRepository.findAll();

            assertEquals(1, adverts.size());
            assertThat(exitingAdvert).isEmpty();

        }

    }

}