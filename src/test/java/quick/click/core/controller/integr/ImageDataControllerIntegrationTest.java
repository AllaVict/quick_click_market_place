package quick.click.core.controller.integr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.ImageDataController;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.COMMENTS_URL;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;
import static quick.click.core.controller.ImageDataController.BASE_URL;

@WithMockAuthenticatedUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(ImageDataController.class)
class ImageDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    @DisplayName("When find all images to an advert")
    class UploadImagesListToAdvertTests {
        @Test
        void uploadImagesListToAdvert() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/" + ADVERT_ID)
                            .file("file", "test data".getBytes())
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Files are uploaded successfully."));
        }


    }

    @Nested
    @DisplayName("When find all images to an advert")
    class FindAllImagesToAdvertTests {
        @Test
        void findAllImagesToAdvert() throws Exception {
            when(imageDataService.findByteListToAdvert(ADVERT_ID)).thenReturn(Collections.singletonList(new byte[10]));

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + ADVERT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

    }

    @Nested
    @DisplayName("When find an image by id and by advertId")
    class FindImageByIdAndByAdvertIdTests {
        @Test
        void findImageByIdAndByAdvertId() throws Exception {
            when(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID)).thenReturn(imageData);

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + ADVERT_ID + "/" + IMAGE_ID)
                            .accept(MediaType.APPLICATION_OCTET_STREAM))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(imageData.getImageData()));
        }

    }

    @Nested
    @DisplayName("When Delete an image by id and by advertId")
    class DeleteImageByIdAndByAdvertIdTests {
        @Test
        void deleteImageByIdAndByAdvertId() throws Exception {
            doNothing().when(imageDataService).deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            mockMvc.perform(delete(BASE_URL + "/" + ADVERT_ID + "/" + IMAGE_ID)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("Image has been deleted successfully."));

        }

    }

}

