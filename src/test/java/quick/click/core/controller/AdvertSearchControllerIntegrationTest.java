package quick.click.core.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.service.AdvertRegistrationService;
import quick.click.security.core.controller.LoginController;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.AdvertFactory.createAdvert;
import static quick.click.config.factory.UserDtoFactory.createUserReadDto;
import static quick.click.config.factory.UserFactory.createUser;


@WebMvcTest(AdvertSearchController.class)
@DisplayName("AdvertSearchControllerIntegration")
class AdvertSearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    @Mock
    private AdvertRegistrationService advertRegistrationService;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertCreateDto advertCreateDto;
    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        User user = createUser();
        advert = createAdvert(user);
        advertReadDto = createAdvertReadDto(createUserReadDto());
        advertCreateDto = createAdvertCreateDto(user.getId());
    }

    @Nested
    @DisplayName("When Register a Advert")
    class RegisterAdvertTests {

        @Test
        @WithMockAuthenticatedUser
        void testRegisterAdvert_ShouldReturnAdvertReadDTO() throws Exception {
            given(advertRegistrationService.registerAdvert(advertCreateDto)).willReturn(advertReadDto);

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andExpect(status().isCreated());
        }

    }

}