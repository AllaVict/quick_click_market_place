package quick.click.advertservice.core.controller_i;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import quick.click.advertservice.core.controller.AdvertRegistrationController;
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.service.AdvertRegistrationService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.advertservice.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.advertservice.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@WebMvcTest(AdvertRegistrationController.class)
@DisplayName("INT_AdvertRegistrationController")
class AdvertRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdvertRegistrationService advertRegistrationService;

    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertCreateDto = createAdvertCreateDto();
    }

    @Nested
    @DisplayName("When Register a Advert")
    class RegisterAdvertTests {

        @Test
        void testRegisterAdvert_ShouldReturnAdvertReadDTO() throws Exception {
            given(advertRegistrationService.registerAdvert(advertCreateDto)).willReturn(advertReadDto);

            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL) //"/v1.0/adverts"
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void testRegisterAdvert_InvalidData() throws Exception {
            advertCreateDto= null;
            mockMvc.perform(post(VERSION_1_0 + ADVERTS_URL) //"/v1.0/adverts"
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertCreateDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }
}

