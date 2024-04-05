package quick.click.core.controller;

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
import quick.click.commons.exeptions.AdvertRegistrationException;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertRegistrationService;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertRegistrationController")
class AdvertRegistrationControllerTest {
    @Mock
    private AdvertRegistrationService advertRegistrationService;
    @InjectMocks
    private AdvertRegistrationController advertRegistrationController;

    private AdvertReadDto advertReadDto;

    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    void setUp() {
        advertReadDto = createAdvertReadDto();
        advertCreateDto = createAdvertCreateDto();
    }

    @Nested
    @DisplayName("When register an advert")
    class RegisterAdvertTests {
        @Test
        void testRegisterAdvert_ShouldReturnAdvertReadDTO() {
            when(advertRegistrationService.registerAdvert(advertCreateDto)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertRegistrationController.registerAdvert(advertCreateDto);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testRegisterAdvert_ShouldReturn400Status_WhenInvalidRequest() {
            advertCreateDto = new AdvertCreateDto();

            ResponseEntity<?> responseEntity = advertRegistrationController.registerAdvert(advertCreateDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testRegisterAdvert_ShouldReturn400Status_WhenThrowsException() {
            when(advertRegistrationService.registerAdvert(any(AdvertCreateDto.class)))
                    .thenThrow(new AdvertRegistrationException("Registration failed due to XYZ"));

            ResponseEntity<?> responseEntity = advertRegistrationController.registerAdvert(advertCreateDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
            assertTrue(responseEntity.getBody().toString().contains("Registration failed due to XYZ"));
        }

    }

}