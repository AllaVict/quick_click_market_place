package quick.click.core.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;

@DataJpaTest
class ImageDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImageDataRepository imageDataRepository;
    private Advert advert;
    private User user;

    private ImageData imageData;

    @BeforeEach
    public void setUp() {
        user = entityManager.merge(createUser());
        advert = entityManager.merge(createAdvertOne(user));
        imageData = new ImageData();
        imageData.setName("Test Image");
        imageData.setType("image/png");
        imageData.setImageData(new byte[] {1, 2, 3});
        imageData.setAdvert(advert);
        entityManager.persist(imageData);
        entityManager.flush();
    }

    @Nested
    @DisplayName("When Find all images by AdvertId")
    class FindAllImageByAdvertIdTests {

        @Test
        void testFindAllByAdvertId_ShouldReturnImagesList() {

            List<ImageData> images = imageDataRepository.findAllByAdvertId(advert.getId());
            assertThat(images).hasSize(1).extracting(ImageData::getName).containsOnly("Test Image");
        }

        @Test
        void testFindAllByAdvertId_NoImagesFound() {

            List<ImageData> images = imageDataRepository.findAllByAdvertId(999L);
            assertTrue(images.isEmpty(), "Expected no images to be found for non-existing advert ID.");
        }
    }

    @Nested
    @DisplayName("When Find a image by id and by advertId")
    class FindImageByIdAndAdvertIdTests {

        @Test
        void testFindByIdAndAdvertId_ShouldReturnImage() {

            Optional<ImageData> foundImage = imageDataRepository.findByIdAndAdvertId(imageData.getId(), advert.getId());
            assertTrue(foundImage.isPresent());
            assertThat(foundImage.get().getType()).isEqualTo("image/png");
        }

        @Test
        void testFindByIdAndAdvertId_NotFound() {

            Optional<ImageData> foundImage = imageDataRepository.findByIdAndAdvertId(999L, 999L);
            assertFalse(foundImage.isPresent(), "Expected no image to be found with non-existing IDs.");
        }

    }

    @Nested
    @DisplayName("When Save a image")
    class SaveImageTests {
        @Test
        void testSaveImage_ShouldSaveSuccessfully() {

            ImageData savedImage = imageDataRepository.save(imageData);
            assertNotNull(savedImage);
            assertNotNull(savedImage.getId());
            assertThat(savedImage.getName()).isEqualTo("Test Image");
        }

        @Test
        void testSaveImage_InvalidImage() {
            ImageData invalidImage = new ImageData();
            assertThrows(ConstraintViolationException.class, () -> {
                imageDataRepository.saveAndFlush(invalidImage);
            }, "Expected a constraint violation when saving an invalid image.");
        }

    }

    @Nested
    @DisplayName("When Delete a image By Id")
    class DeleteImageByIdTests {
        @Test
        void testDeleteImage_ShouldDeleteSuccessfully() {

            assertThat(imageDataRepository.findById(imageData.getId())).isNotEmpty();
            imageDataRepository.delete(imageData);
            assertThat(imageDataRepository.findById(imageData.getId())).isEmpty();
        }

        @Test
        void testDeleteImage_AlreadyDeleted() {
            ImageData anotherImage = new ImageData();
            anotherImage.setName("Another Test Image");
            anotherImage.setType("image/png");
            anotherImage.setImageData(new byte[]{4, 5, 6});
            anotherImage.setAdvert(advert);
            entityManager.persist(anotherImage);
            entityManager.flush();

            Long imageId = anotherImage.getId();
            imageDataRepository.delete(anotherImage);
            entityManager.flush();

            Optional<ImageData> deletedImage = imageDataRepository.findById(imageId);
            assertFalse(deletedImage.isPresent(), "Image should not exist after being deleted");

            assertThrows(IllegalArgumentException.class, () -> {
                Optional<ImageData> imageOptional = imageDataRepository.findById(imageId);
                if (imageOptional.isPresent()) {
                    imageDataRepository.delete(imageOptional.get());
                } else {
                    throw new IllegalArgumentException("Image does not exist");
                }
            }, "Expected an exception when attempting to delete a non-existing image.");
        }

    }

}