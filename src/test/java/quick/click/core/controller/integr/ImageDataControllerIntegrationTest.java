package quick.click.core.controller.integr;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.ImageDataController;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.*;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;
import static quick.click.core.controller.ImageDataController.BASE_URL;

@WithMockAuthenticatedUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(ImageDataController.class)
class ImageDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        void testFindAllImagesToAdvert_ShouldReturnAllImages() throws Exception {
            when(imageDataService.findByteListToAdvert(ADVERT_ID)).thenReturn(Collections.singletonList(new byte[10]));

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        void testFindAllImagesToAdvert_ShouldReturn200Status_WhenReturnEmptyList() throws Exception {
            given(imageDataService.findByteListToAdvert(ADVERT_ID)).willReturn(Collections.emptyList());

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Collections.emptyList())))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testFindAllImagesToAdvert_ShouldReturn404Status_WhenInvalidRequested() throws Exception {

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL +"/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("When find an image by id and by advertId")
    class FindImageByIdAndByAdvertIdTests {
        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturnImage() throws Exception {
            given(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID)).willReturn(imageData);

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL + "/" + ADVERT_ID + "/" + IMAGE_ID)
                            .accept(MediaType.APPLICATION_OCTET_STREAM))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(imageData.getImageData()));
        }

        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturn200Status_WhenNoImageFound() throws Exception {
            given(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID))
                    .willThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL + "/" + ADVERT_ID + "/" + IMAGE_ID)
                            .accept(MediaType.APPLICATION_OCTET_STREAM))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testFindImageByIdAndByAdvertId_ShouldReturn400Status_WhenInvalidRequested() throws Exception {
            given(imageDataService.findImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID)).willReturn(null);

            mockMvc.perform(get(VERSION_1_0 + IMAGES_URL + "/" + ADVERT_ID + "/invalid")
                                    .accept(MediaType.APPLICATION_OCTET_STREAM))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("When Delete an image by id and by advertId")
    class DeleteImageByIdAndByAdvertIdTests {
        @Test
        void deleteImageByIdAndByAdvertId_ShouldnReturn200Status_WhenDeleteImage() throws Exception {
            doNothing().when(imageDataService).deleteImageByIdAndByAdvertId(IMAGE_ID, ADVERT_ID, authenticatedUser);

            mockMvc.perform(delete(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID +"/" + IMAGE_ID)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("The image has been deleted successfully."));

        }

        @Test
        void testDeleteComment_ShouldnReturn404Status_WhenImageDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Image", "id", IMAGE_ID))
                    .when(imageDataService).deleteImageByIdAndByAdvertId(anyLong(),anyLong(), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID +"/" + IMAGE_ID)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteComment_ShouldnReturn404Status_WhenInvalidRequested() throws Exception {
            mockMvc.perform(delete(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID +"/" + "/invalid")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());

        }

        @Test
        void testDeleteComment_UnauthorizedUser() throws Exception {
            doThrow(new AuthorizationException("Unauthorized access"))
                    .when(imageDataService).deleteImageByIdAndByAdvertId(anyLong(),anyLong(), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + IMAGES_URL + "/"+ADVERT_ID +"/" + IMAGE_ID)
                           .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }

    }

}

