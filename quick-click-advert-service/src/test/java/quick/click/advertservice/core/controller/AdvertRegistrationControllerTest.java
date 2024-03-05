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
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.service.AdvertRegistrationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertRegistrationController")
class AdvertRegistrationControllerTest {
    @Mock
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

    /**
     @GetMapping()
     public  ResponseEntity<AdvertReadDto> registerAdvert(@Valid @RequestBody AdvertCreateDto advertCreateDto) {
     final AdvertReadDto advertReadDto = advertRegistrationService.registerAdvert(advertCreateDto);
     LOGGER.debug("In registerAdvert received POST advert register successfully with id {} ", advertReadDto.getId());
     return ResponseEntity.status(HttpStatus.CREATED)
     .body(advertReadDto);
     }
     */
    @Nested
    @DisplayName("When Register a Advert")
    class RegisterAdvertTests {
        @Test
        void testRegisterAdvert_ShouldReturnAdvertReadDTO() {
            when(advertRegistrationService.registerAdvert(advertCreateDto)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertRegistrationController.registerAdvert(advertCreateDto);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testRegisterAdvert_InvalidData() {
            advertCreateDto = new AdvertCreateDto();

            ResponseEntity<?> responseEntity = advertRegistrationController.registerAdvert(advertCreateDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

    }

}