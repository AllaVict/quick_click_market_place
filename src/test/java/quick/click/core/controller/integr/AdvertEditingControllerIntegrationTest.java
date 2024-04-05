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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.controller.AdvertEditingController;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertEditingService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;

@WithMockUser
@WebMvcTest(AdvertEditingController.class)
@DisplayName("AdvertEditingController")
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
        void testEditAdvert_InvalidAdvertData() throws Exception {
            AdvertEditingDto invalidDto = new AdvertEditingDto();

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEditAdvert_AdvertDoesNotExist() throws Exception {
            given(advertEditingService.editAdvert(anyLong(), any(AdvertEditingDto.class)))
                    .willThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertEditingDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
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
    class  ArchiveAdvertTests {
        @Test
        void testArchiveAdvert_shouldReturnAdvertReadDto() throws Exception {
            given(advertEditingService.archiveAdvert(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(put(VERSION_1_0 + ADVERTS_URL + "/archive/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testArchiveAdvert_AdvertDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).archiveAdvert(ADVERT_ID);

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
        void testDeleteAdvert_shouldReturnAdvertReadDto() throws Exception {
          doNothing().when(advertEditingService).deleteAdvert(any(Long.class));

          mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL+"/"+ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andExpect(content().string(containsString("The Advert has deleted successfully")))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteAdvert_AdvertDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).deleteAdvert(ADVERT_ID);

            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
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

        @Test
        void testDeleteAdvert_MissingCsrf() throws Exception {
            mockMvc.perform(delete(VERSION_1_0 + ADVERTS_URL + "/" + ADVERT_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }

}
