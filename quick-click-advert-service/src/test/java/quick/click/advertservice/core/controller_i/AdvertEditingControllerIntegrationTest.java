package quick.click.advertservice.core.controller_i;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import quick.click.advertservice.core.controller.AdvertEditingController;
import quick.click.advertservice.core.domain.dto.AdvertEditingDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.service.AdvertEditingService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.advertservice.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.advertservice.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@WebMvcTest(AdvertEditingController.class)
@DisplayName("INT_AdvertEditingControllerTest")
public class AdvertEditingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AdvertEditingService advertEditingService;

    @InjectMocks
    AdvertEditingController advertEditingController;

    private static final long ADVERT_ID = 101L;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;
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
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testEditAdvert_InvalidData() throws Exception{
            advertEditingDto =null;

            mockMvc.perform(put(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)//"/v1.0/adverts/101"
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEditAdvert_statusCode400WhenInvalidRequested() throws Exception{
            mockMvc.perform(put(VERSION_1_0+ADVERTS_URL+"/invalid")
                            //with(csrf())
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
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andExpect(content().string(containsString("The Advert has deleted successfully")))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteAdvert_statusCode400WhenInvalidRequested() throws Exception{
            mockMvc.perform(delete(VERSION_1_0+ADVERTS_URL+"/invalid")
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

}
