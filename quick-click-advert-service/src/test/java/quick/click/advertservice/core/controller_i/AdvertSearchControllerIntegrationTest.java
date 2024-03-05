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
import org.springframework.web.context.WebApplicationContext;
import quick.click.advertservice.core.controller.AdvertRegistrationController;
import quick.click.advertservice.core.controller.AdvertSearchController;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.service.AdvertSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.advertservice.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.advertservice.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@WebMvcTest(AdvertSearchController.class)
@DisplayName("INT_AdvertSearchControllerIntegration")
class AdvertSearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;


    @MockBean
    private AdvertSearchService advertSearchService;

    private static final long ADVERT_ID = 101L;

    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private List<AdvertReadDto> advertReadDtoList;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
    }

    @Nested
    @DisplayName("When Find Advert By Id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_ShouldReturnAdvertReadDTO() throws Exception {
            given(advertSearchService.findAdvertById(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAdvertById_ShouldNoAdvertFound() throws Exception {
            advertReadDto =null;
            given(advertSearchService.findAdvertById(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testFindAdvertById_statusCode400WhenInvalidRequested() throws Exception {
            advertReadDto =null;
            given(advertSearchService.findAdvertById(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/invalid")
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("When Find All Adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_shouldReturnAllAdverts() throws Exception{
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            given(advertSearchService.findAllAdverts()).willReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL)
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAllAdverts_shouldThrowException() throws Exception{
            advertReadDtoList =new ArrayList<>();
            when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL)
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testFindAllAdverts_statusCode400WhenInvalidIdRequested() throws Exception{

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/invalid")
                            //with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

}