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
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

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

//    @Test    void testUploadImagesListToAdvert() {    }
//    @Test    void findAllImagesToAdvert() {    }
//    @Test    void findImageByIdAndByAdvertId() {    }

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