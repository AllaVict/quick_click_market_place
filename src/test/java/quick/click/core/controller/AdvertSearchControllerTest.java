package quick.click.core.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchController")
class AdvertSearchControllerTest {
    @InjectMocks
    private AdvertSearchController advertSearchController;

    @Mock
    private AdvertSearchService advertSearchService;

    private static final long ADVERT_ID = 101L;

    private static final long USER_ID = 101L;

    private AdvertReadDto advertReadDto;

    @Spy
    private List<AdvertReadDto> advertReadDtoList;

    @BeforeEach
    void setUp() {
        advertReadDto = createAdvertReadDto();
    }

    @Nested
    @DisplayName("When find an advert by id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_ShouldReturnAdvertReadDTO() {
            when(advertSearchService.findAdvertById(ADVERT_ID)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertSearchController.findAdvertById(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testEditAdvert_ShouldReturn404Status_WhenAdvertIdDoesNotExist() {
            when(advertSearchService.findAdvertById(ADVERT_ID))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = advertSearchController.findAdvertById(ADVERT_ID);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Nested
        @DisplayName("When find all adverts")
        class FindAllAdvertsTests {

            @Test
            void testFindAllAdverts_ShouldReturnAllAdverts() {
                advertReadDtoList = List.of(advertReadDto, advertReadDto);
                when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

                ResponseEntity<?> responseEntity = advertSearchController.findAllAdverts();

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(advertReadDtoList, responseEntity.getBody());
            }

            @Test
            void testFindAllAdverts_ShouldReturn200Status_WhenReturnEmptyList() {
                advertReadDtoList = new ArrayList<>();
                when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

                ResponseEntity<?> responseEntity = advertSearchController.findAllAdverts();

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(advertReadDtoList, responseEntity.getBody());

            }

        }

        @Nested
        @DisplayName("When find all adverts by user id ")
        class FindAllAdvertsByUserIdTests {

            @Test
            void testFindAllAdvertsByUserId_ShouldReturnAllAdverts() {
                advertReadDtoList = List.of(advertReadDto, advertReadDto);
                when(advertSearchService.findAllAdvertsByUserId(USER_ID)).thenReturn(advertReadDtoList);

                ResponseEntity<?> responseEntity = advertSearchController.findAllAdvertsByUserId(USER_ID);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(advertReadDtoList, responseEntity.getBody());
            }

            @Test
            void testFindAllAdvertsByUserId_ShouldReturn200Status_WhenReturnEmptyList() {
                advertReadDtoList = new ArrayList<>();
                when(advertSearchService.findAllAdvertsByUserId(USER_ID)).thenReturn(advertReadDtoList);

                ResponseEntity<?> responseEntity = advertSearchController.findAllAdvertsByUserId(USER_ID);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(advertReadDtoList, responseEntity.getBody());

            }

        }

    }
}

