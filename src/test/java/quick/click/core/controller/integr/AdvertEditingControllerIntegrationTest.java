package quick.click.core.controller.integr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import quick.click.core.controller.AdvertEditingController;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.service.AdvertEditingService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.AdvertFactory.createAdvert;

@WithMockUser
@WebMvcTest(AdvertEditingController.class)
@DisplayName("INT_AdvertEditingControllerTest")
public class AdvertEditingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdvertEditingService advertEditingService;

    @InjectMocks
    AdvertEditingController advertEditingController;

    private static final long ADVERT_ID = 101L;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;

    @Autowired
    private WebApplicationContext context;
    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertEditingDto =  createAdvertEditingDto();
    }

    @Nested
    @DisplayName("When Edit a Advert")
    class  EditAdvertTests {
        @Test
        void testEditAdvert_shouldReturnAdvertReadDto() throws Exception {
           given(advertEditingService.editAdvert(ADVERT_ID, advertEditingDto)).willReturn(advertReadDto);

           mockMvc.perform(put(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)//"/v1.0/adverts/101"
                             .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testEditAdvert_InvalidData() throws Exception{
            advertEditingDto =null;

            mockMvc.perform(put(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)//"/v1.0/adverts/101"
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEditAdvert_statusCode400WhenInvalidRequested() throws Exception{
            mockMvc.perform(put(VERSION_1_0+ADVERTS_URL+"/invalid")
                             .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("When Delete a Advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_shouldReturnAdvertReadDto() throws Exception {
          doNothing().when(advertEditingService).deleteAdvert(any(Long.class));

          mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL+"/"+ADVERT_ID) //"/v1.0/adverts/101"
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andExpect(content().string(containsString("The Advert has deleted successfully")))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteAdvert_statusCode400WhenInvalidRequested() throws Exception{
            mockMvc.perform(delete(VERSION_1_0+ADVERTS_URL+"/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

}
