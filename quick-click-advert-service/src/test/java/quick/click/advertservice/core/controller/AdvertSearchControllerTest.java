package quick.click.advertservice.core.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.core.service.AdvertSearchService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;
import static quick.click.advertservice.factory.UserDtoFactory.createUserReadDto;
import static quick.click.advertservice.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchController")
class AdvertSearchControllerTest {
    @InjectMocks
    private AdvertSearchController advertSearchController;

    @Mock
    private AdvertSearchService advertSearchService;

    private static final long ADVERT_ID = 101L;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    @BeforeEach
    void setUp() {
        User user = createUser();
        advert = createAdvert(user);
        advertReadDto = createAdvertReadDto(createUserReadDto());
    }
    @Test
    void findAdvertByIdAndStatus() {    }
    /**
     @GetMapping()
     public ResponseEntity<AdvertReadDto> findAdvertById(@Valid @RequestBody Long storyId) {
     final AdvertReadDto advertReadDto = advertSearchService.findAdvertById(storyId);
     LOGGER.debug("In findAdvertById received GET advert find successfully with id {} ", advertReadDto.getId());
     return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);
     }
     */
    @Nested
    @DisplayName("When Register a Advert")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_ShouldReturnAdvertReadDTO() {
            when(advertSearchService.findAdvertById(ADVERT_ID)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertSearchController.findAdvertById(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testFindAdvertById_Should() {

        }

    }

}

