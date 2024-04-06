package quick.click.core.controller.integr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.AdvertEditingController;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertEditingService;
import quick.click.security.commons.model.AuthenticatedUser;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;

@WithMockAuthenticatedUser
@WebMvcTest(AdvertEditingController.class)
@DisplayName("AdvertEditingController")
public class AdvertEditingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdvertEditingService advertEditingService;

    @InjectMocks
    AdvertEditingController advertEditingController;

    private static final long ADVERT_ID = 101L;

    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;

    @Autowired
    private WebApplicationContext context;

    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        advertReadDto = createAdvertReadDto();
        advertEditingDto = createAdvertEditingDto();
        authenticatedUser = mock(AuthenticatedUser.class);
    }

    @Nested
    @DisplayName("When Edit a Advert")
    class EditAdvertTests {
        @Test
        void testEditAdvert_ShouldReturnAdvertReadDto() throws Exception {
            given(advertEditingService.editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser)).willReturn(advertReadDto);

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)//"/v1.0/adverts/101"
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testEditAdvert_ShouldnReturn400Status_WhenInvalidAdvertData() throws Exception {
            AdvertEditingDto invalidDto = new AdvertEditingDto();

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEditAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() throws Exception {
            given(advertEditingService.editAdvert(anyLong(), any(AdvertEditingDto.class), any(AuthenticatedUser.class)))
                    .willThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testEditAdvert_ShouldnReturn400Status_WhenInvalidRequested() throws Exception {
            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testArchiveAdvert_MissingCsrf() throws Exception {
            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("When archive an advert")
    class ArchiveAdvertTests {
        @Test
        void testArchiveAdvert_ShouldReturnAdvertReadDto() throws Exception {
            given(advertEditingService.archiveAdvert(ADVERT_ID, authenticatedUser)).willReturn(advertReadDto);

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/archive/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testArchiveAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).archiveAdvert(any(Long.class), any(AuthenticatedUser.class));

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/archive/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testArchiveAdvert_MissingCsrf() throws Exception {
            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/archive/" + ADVERT_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("When Delete a Advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_ShouldReturnAdvertReadDto() throws Exception {

            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andExpect(content().string(containsString("The Advert has deleted successfully")))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).deleteAdvert(any(Long.class), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteAdvert_ShouldnReturn400Status_WhenInvalidRequested() throws Exception {
            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/invalid")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testDeleteAdvert_MissingCsrf() throws Exception {
            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void testDeleteAdvert_UnauthorizedUser() throws Exception {

/**/            doThrow(new AuthorizationException("Unauthorized access"))
                    .when(advertEditingService).deleteAdvert(any(Long.class), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }

}
