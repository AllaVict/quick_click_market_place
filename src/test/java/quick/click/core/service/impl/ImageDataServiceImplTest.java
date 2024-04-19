package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.ImageDataRepository;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.AuthenticatedUser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;

@ExtendWith(SpringExtension.class)
class ImageDataServiceImplTest {
    @Mock
    private ImageDataRepository imageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertRepository advertRepository;

    @InjectMocks
    private ImageDataServiceImpl imageDataService;

    private static final long ADVERT_ID = 101L;

    private static final long IMAGE_ID = 101L;

    private static final String EMAIL = "test@example.com";

    private Advert advert;

    private User user;

    private ImageData imageData;

    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

    @BeforeEach
    public void setUp() {
        user = createUser();
        advert = createAdvertOne(user);
        imageData = new ImageData();
        imageData.setName("Test Image");
        imageData.setType("image/png");
        imageData.setImageData(new byte[] {1, 2, 3});
        imageData.setAdvert(advert);
    }

    @Nested
    @DisplayName("When upload an image to an advert")
    class UploadImageToAdvertTests {
        @Test
        void testUploadImageToAdvert() throws IOException {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            MockMultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "Hello, World!".getBytes());
            when(advertRepository.findAdvertById(anyLong())).thenReturn(Optional.of(advert));
            when(userRepository.findUserById(anyLong())).thenReturn(Optional.ofNullable(user));
            when(imageRepository.saveAndFlush(any(ImageData.class))).thenReturn(imageData);

            ImageData result = imageDataService.uploadImageToAdvert(ADVERT_ID, file, authenticatedUser);

            verify(imageRepository).saveAndFlush(any(ImageData.class));
            assertNotNull(result);
        }

        @Test
        void testUploadImageToAdvert_AdvertNotFound() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            MockMultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "Hello, World!".getBytes());
            when(userRepository.findUserById(anyLong())).thenReturn(Optional.ofNullable(user));
            when(advertRepository.findAdvertById(ADVERT_ID))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            assertThrows(ResourceNotFoundException.class, () -> {
                imageDataService.uploadImageToAdvert(ADVERT_ID, file,authenticatedUser);
            });
        }
    }
    @Nested
    @DisplayName("When Find all images by AdvertId")
    class FindAllImageByAdvertIdTests {
        @Test
        void testFindByteListToAdvert_ShouldReturnByteList() {
            List<ImageData> images = List.of(imageData);
            when(imageRepository.findAllByAdvertId(anyLong())).thenReturn(images);

            List<byte[]> result = imageDataService.findByteListToAdvert(ADVERT_ID);

            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        void testFindByteListToAdvert_NoImagesFound() {
            when(imageRepository.findAllByAdvertId(ADVERT_ID)).thenReturn(Collections.emptyList());

            List<byte[]> result = imageDataService.findByteListToAdvert(ADVERT_ID);
            assertTrue(result.isEmpty(), "Expected no byte arrays to be returned for an advert with no images");
        }
    }
    @Nested
    @DisplayName("When Find a image by id and by advertId")
    class FindImageByIdAndAdvertIdTests {
        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturnImageData() {
            when(imageRepository.findByIdAndAdvertId(anyLong(), anyLong())).thenReturn(Optional.of(imageData));

            ImageData found = imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID);

            assertNotNull(found);
            assertEquals(found.getName(), imageData.getName());
        }

        @Test
        void testFindImageByIdAndByAdvertId_NotFound() {
            when(imageRepository.findByIdAndAdvertId(IMAGE_ID, ADVERT_ID))
                    .thenThrow(new ResourceNotFoundException("Image", "id", IMAGE_ID));
            assertThrows(ResourceNotFoundException.class, () -> {
                imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID);
            });
        }
    }
    @Nested
    @DisplayName("When Delete a image By Id")
    class DeleteImageByIdTests {
        @Test
        void testDeleteImageByIdAndByAdvertId_ShouldDeleteImageData() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(imageRepository.findByIdAndAdvertId(anyLong(), anyLong())).thenReturn(Optional.of(imageData));

            imageDataService.deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            verify(imageRepository).delete(imageData);
        }

        @Test
        void testDeleteImageByIdAndByAdvertId_NotFound() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(imageRepository.findByIdAndAdvertId(IMAGE_ID, ADVERT_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                imageDataService.deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);
            });
        }
    }

}