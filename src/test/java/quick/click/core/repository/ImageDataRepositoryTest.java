package quick.click.core.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DataJpaTest
class ImageDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImageDataRepository imageDataRepository;

    @Test
    void testFindAllByAdvertId() {
        // Setup data
        Advert advert = new Advert(); // Assuming Advert is correctly defined elsewhere
        entityManager.persist(advert);

        ImageData imageData = new ImageData();
        imageData.setName("Test Image");
        imageData.setType("image/png");
        imageData.setImageData(new byte[] {1, 2, 3});
        imageData.setAdvert(advert);
        entityManager.persist(imageData);
        entityManager.flush();

        // Test findAllByAdvertId
        List<ImageData> images = imageDataRepository.findAllByAdvertId(advert.getId());
        assertThat(images).hasSize(1).extracting(ImageData::getName).containsOnly("Test Image");
    }


    @Test
    void testFindByIdAndAdvertId() {
        // Setup data
        Advert advert = new Advert();
        entityManager.persist(advert);

        ImageData imageData = new ImageData();
        imageData.setName("Sample Image");
        imageData.setType("image/jpeg");
        imageData.setImageData(new byte[] {1, 2, 3, 4});
        imageData.setAdvert(advert);
        entityManager.persist(imageData);
        entityManager.flush();

        // Test findByIdAndAdvertId
        Optional<ImageData> foundImage = imageDataRepository.findByIdAndAdvertId(imageData.getId(), advert.getId());
        assertTrue(foundImage.isPresent());
        assertThat(foundImage.get().getType()).isEqualTo("image/jpeg");
    }

    @Test
    void testSaveImage() {
        // Setup data
        ImageData imageData = new ImageData();
        imageData.setName("New Image");
        imageData.setType("image/jpeg");
        imageData.setImageData(new byte[] {5, 6, 7, 8});

        // Test save
        ImageData savedImage = imageDataRepository.save(imageData);
        assertNotNull(savedImage);
        assertNotNull(savedImage.getId());
        assertThat(savedImage.getName()).isEqualTo("New Image");
    }

    @Test
    void testDeleteImage() {
        // Setup data
        ImageData imageData = new ImageData();
        imageData.setName("Delete Me");
        imageData.setType("image/png");
        imageData.setImageData(new byte[] {9, 10, 11, 12});
        entityManager.persist(imageData);
        entityManager.flush();

        // Test delete
        assertThat(imageDataRepository.findById(imageData.getId())).isNotEmpty();
        imageDataRepository.delete(imageData);
        assertThat(imageDataRepository.findById(imageData.getId())).isEmpty();
    }
}