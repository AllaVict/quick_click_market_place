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
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.AdvertRegistrationController;
import quick.click.core.controller.AdvertSearchController;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;

@WithMockUser
@WebMvcTest(AdvertSearchController.class)
@DisplayName("AdvertSearchController")
class AdvertSearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdvertSearchService advertSearchService;

    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    private static final long ADVERT_ID = 101L;

    private static final long USER_ID = 101L;

    private AdvertReadDto advertReadDto;

    private List<AdvertReadDto> advertReadDtoList;

    @Autowired
    private WebApplicationContext context;

    private AuthenticatedUser authenticatedUser;

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
        authenticatedUser = mock(AuthenticatedUser.class);
    }

    @Nested
    @DisplayName("When find an advert by id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_ShouldReturnAdvertReadDTO() throws Exception {
            given(advertSearchService.findAdvertById(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAdvertById_ShouldReturn200Status_WhenNoAdvertFound() throws Exception {
            given(advertSearchService.findAdvertById(ADVERT_ID))
                    .willThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/"+ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testFindAdvertById_ShouldReturn400Status_WhenInvalidRequested() throws Exception {
            advertReadDto =null;
            given(advertSearchService.findAdvertById(ADVERT_ID)).willReturn(advertReadDto);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("When find all adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_shouldReturnAllAdverts() throws Exception{
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            given(advertSearchService.findAllAdverts()).willReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAllAdverts_ShouldReturn200Status_WhenReturnEmptyList() throws Exception{
            advertReadDtoList =new ArrayList<>();
            when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAllAdverts_ShouldReturn400Status_WhenInvalidRequested() throws Exception{

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("When find all adverts by user")
    @WithMockAuthenticatedUser
    class FindAllAdvertsByUserTests {

        @Test
        void testFindAllAdvertsByUser_ShouldReturnAllAdverts() throws Exception {
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            when(advertSearchService.findAllAdvertsByUser(authenticatedUser)).thenReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0+ADVERTS_URL+"/user")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(advertReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void testFindAllAdvertsByUser_ShouldReturn404Status_WhenReturnEmptyList() throws Exception {
            advertReadDtoList =new ArrayList<>();
            when(advertSearchService.findAllAdvertsByUser(authenticatedUser)).thenReturn(advertReadDtoList);

            mockMvc.perform(get(VERSION_1_0 +ADVERTS_URL+"/user")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

    }

}