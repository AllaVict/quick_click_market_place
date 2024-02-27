package quick.click.advertservice.core.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.factory.AdvertFactory;
import quick.click.advertservice.factory.UserFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdvertRepositoryTest {


        @Autowired
        private AdvertRepository advertRepository;

        @Autowired
        private FileReferenceRepository fileReferenceRepository;

        @Autowired
        private TestEntityManager em;
        private static final long ADVERT_ID = 101L;

        private Advert advert;

        private User user;
        @BeforeEach
        public void setUp() {
            user = UserFactory.createUser();
            em.persistAndFlush(user);
            advert = AdvertFactory.createAdvert(user);
        }

        @Nested
        @DisplayName("When Find Advert by Id")
        class FindAdvertByIdTests {

            @Test
            @Transactional
            void testFindAdvertById_shouldReturnExistingAdvertWithGivenId() {
                Advert saveAdvert =   advertRepository.save(advert);
                Optional<Advert> foundAdvert = advertRepository.findAdvertById(saveAdvert.getId());
                assertTrue(foundAdvert.isPresent());
                assertEquals(advert.getId(), foundAdvert.get().getId());
            }

            @Test
            void testFindUserById_shouldReturnNoUserWithGivenId() {
                Optional<Advert> foundAdvert = advertRepository.findAdvertById(ADVERT_ID);

                assertFalse(foundAdvert.isPresent());
                assertThat(foundAdvert).isEmpty();
            }
        }

    /**
     @Test
     @DisplayName("It should save the Manager to the database")
     void saveManager() {
     managerRepository.save(managerDorota);
     Manager newManager = managerRepository.save(managerDorota);
     assertNotNull(newManager);
     assertThat(newManager.getId()).isNotEqualTo(null);
     assertThat(newManager.getFirstName()).isEqualTo("Dorota");
     }

     @Test
     @DisplayName("It should return the managers list with size of 2")
     void getAllManagers() {
     managerRepository.save(managerDorota);
     managerRepository.save(managerWiktor);

     List<Manager> list = managerRepository.findAll();

     assertNotNull(list);
     assertThat(list).isNotNull();
     assertEquals(7, list.size());
     }



     @Test
     @DisplayName("It should update the Manager with new Description")
     void updateManager() {
     managerRepository.save(managerDorota);
     Manager existingManager = managerRepository.findById(managerDorota.getId()).get();
     existingManager.setDescription("new Description");
     Manager updatedManager = managerRepository.save(existingManager);
     assertEquals("new Description", updatedManager.getDescription());
     assertEquals("Dorota", updatedManager.getFirstName());
     }

     @Test
     @DisplayName("It should delete the existing Manager")
     void deleteManager() {

     managerRepository.save(managerDorota);
     Long id = managerDorota.getId();
     managerRepository.save(managerWiktor);
     managerRepository.delete(managerDorota);
     List<Manager> list = managerRepository.findAll();
     Optional<Manager> exitingManager = managerRepository.findById(id);
     assertEquals(6, list.size());
     assertThat(exitingManager).isEmpty();

     }


     */


}