package quick.click.advertservice.core.controller;

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
import org.springframework.web.server.ResponseStatusException;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.service.AdvertSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchController")
class AdvertSearchControllerTest {
    @InjectMocks
    private AdvertSearchController advertSearchController;

    @Mock
    private AdvertSearchService advertSearchService;

    @Mock
    private AdvertRepository advertRepository;

    private static final long ADVERT_ID = 101L;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    @Spy
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
        void testFindAdvertById_ShouldReturnAdvertReadDTO() {
            when(advertSearchService.findAdvertById(ADVERT_ID)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertSearchController.findAdvertById(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testFindAdvertById_shouldThrowException() {

            assertThrows(ResponseStatusException.class,
                        () ->advertSearchController.findAdvertById(ADVERT_ID));
            }
        }

    @Nested
    @DisplayName("When Find All Adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_shouldReturnAllAdverts() {
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

            ResponseEntity<?> responseEntity = advertSearchController.findAllAdverts();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDtoList, responseEntity.getBody());
        }

        @Test
        void testFindAllAdverts_shouldThrowException() {
            advertReadDtoList =new ArrayList<>();
            when(advertSearchService.findAllAdverts()).thenReturn(advertReadDtoList);

            ResponseEntity<?> responseEntity = advertSearchController.findAllAdverts();

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        }

    }

}