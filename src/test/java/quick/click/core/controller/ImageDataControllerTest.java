package quick.click.core.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageDataController")
class ImageDataControllerTest {

    @Mock
    private ImageDataService imageDataService;
    @InjectMocks
    private ImageDataController imageDataController;

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
        imageData.setImageData(new byte[]{1, 2, 3});
        imageData.setAdvert(advert);
    }

    @Nested
    @DisplayName("When upload images list to an advert")
    class UploadImagesListToAdvertTests {
        @Test
        void testUploadImagesListToAdvert_ShouldUpload() {
            MockMultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "Hello, World!".getBytes());
            MockMultipartFile[] images = {file};

            ResponseEntity<?> responseEntity = imageDataController.uploadImagesListToAdvert(ADVERT_ID, images, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("Files are uploaded successfully.", responseEntity.getBody());

        }

        @Test
        void testUploadImagesListToAdvert_AdvertNotFound() throws IOException {
            MockMultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "Hello, World!".getBytes());
            MockMultipartFile[] images = {file};
            when(imageDataService.uploadImageToAdvert(ADVERT_ID, file, authenticatedUser))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = imageDataController.uploadImagesListToAdvert(ADVERT_ID, images, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            assertEquals("Advert not found with id : "+"'"+ADVERT_ID+"'", responseEntity.getBody());
        }

    }

    @Nested
    @DisplayName("When find all images to an advert")
    class FindAllImagesToAdvertTests {

        @Test
        void testFindAllImagesToAdvert_ShouldReturnAllImages() {
            List<byte[]> images = List.of(imageData.getImageData());
            when(imageDataService.findAllImagesAsByteListByAdvertId(ADVERT_ID)).thenReturn(images);

            ResponseEntity<?> responseEntity = imageDataController.findAllImagesToAdvert(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(images, responseEntity.getBody());

        }

        @Test
        void testFindAllImagesToAdvert_ShouldReturn200Status_WhenReturnEmptyList() {
            when(imageDataService.findAllImagesAsByteListByAdvertId(ADVERT_ID)).thenReturn(Collections.emptyList());

            ResponseEntity<?> responseEntity = imageDataController.findAllImagesToAdvert(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(Collections.emptyList(), responseEntity.getBody());
        }

    }

    @Nested
    @DisplayName("When find an image by id and by advertId")
    class FindImageByIdAndByAdvertIdTests {

        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturnAImage() throws IOException {
            when(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID)).thenReturn(imageData);

            ResponseEntity<?> responseEntity = imageDataController.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(imageData.getImageData(), responseEntity.getBody());
        }

        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturn404Status_WhenImageDoesNotExist() throws IOException {
            when(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID))
                    .thenThrow(new ResourceNotFoundException("Image", "id", IMAGE_ID));

            ResponseEntity<?> responseEntity = imageDataController.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }

    @Nested
    @DisplayName("When Delete an image by id and by advertId")
    class DeleteImageByIdAndByAdvertIdTests {
        @Test
        void testDeleteImageByIdAndByAdvertId_ShouldReturnComment() {
            doNothing().when(imageDataService).deleteImageByIdAndByAdvertId(any(Long.class),
                    any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = imageDataController.deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("The image has been deleted successfully.", responseEntity.getBody());
        }

        @Test
        void testDeleteImageByIdAndByAdvertI_ShouldnReturn404Status_WhenCommentDoesNotExist() {
            doThrow(new ResourceNotFoundException("Image", "id", IMAGE_ID))
                    .when(imageDataService).deleteImageByIdAndByAdvertId(any(Long.class), any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = imageDataController.deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void testDeleteImageByIdAndByAdvertI_UnauthorizedUser() {
            doThrow(new AuthorizationException("Unauthorized access")).when(imageDataService)
                    .deleteImageByIdAndByAdvertId(any(Long.class), any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = imageDataController.deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        }
    }
}