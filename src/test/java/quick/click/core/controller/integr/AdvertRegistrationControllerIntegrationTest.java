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
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.AdvertRegistrationController;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertRegistrationService;
import quick.click.security.commons.model.AuthenticatedUser;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WithMockAuthenticatedUser
@WebMvcTest(AdvertRegistrationController.class)
@DisplayName("AdvertRegistrationController")
class AdvertRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdvertRegistrationService advertRegistrationService;

    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    private AdvertReadDto advertReadDto;

    private AdvertCreateDto advertCreateDto;

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
        advertCreateDto = createAdvertCreateDto();
    }

    @Nested
    @DisplayName("When Register a Advert")
    class RegisterAdvertTests {

        @Test
        void testRegisterAdvert_ShouldReturnAdvertReadDTO() throws Exception {
            given(advertRegistrationService.registerAdvert(any(AdvertCreateDto.class), any(AuthenticatedUser.class)))
                    .willReturn(advertReadDto);

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void testRegisterAdvert_ShouldReturn400Status_WhenAdvertDtoIsNull() throws Exception {
            advertCreateDto = null;
            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> result.getResponse().getContentAsString().equals("Please fill all fields"));
        }

        @Test
        void testRegisterAdvert_ShouldReturn400Status_WhenFieldsAreNull() throws Exception {
            AdvertCreateDto invalidDto = new AdvertCreateDto();

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value(containsString("Please fill all fields")));
        }

        @Test
        void testRegisterAdvert_ShouldReturn404Status_WhenInvalidRequested() throws Exception {

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL + "/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testRegisterAdvert_UnauthorizedUser() throws Exception {
            given(advertRegistrationService.registerAdvert(any(AdvertCreateDto.class), any(AuthenticatedUser.class)))
                    .willThrow(new AuthorizationException("Unauthorized access"));

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }
}

