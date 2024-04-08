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
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertEditingService;
import quick.click.security.commons.model.AuthenticatedUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.core.enums.AdvertStatus.ARCHIVED;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertEditingController")
class AdvertEditingControllerTest {

    @Mock
    private AdvertEditingService advertEditingService;

    @InjectMocks
    AdvertEditingController advertEditingController;

    private static final long ADVERT_ID = 101L;

    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;
    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);


    @BeforeEach
    void setUp() {
        advertReadDto = createAdvertReadDto();
        advertEditingDto = createAdvertEditingDto();
    }

    @Nested
    @DisplayName("When edit an advert")
    class EditAdvertTests {
        @Test
        void testEditAdvert_shouldReturnAdvertReadDto() {
            when(advertEditingService.editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser))
                    .thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertEditingController
                    .editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testEditAdvert_ShouldnReturn400Status_WhenInvalidRequested() {
            advertEditingDto = new AdvertEditingDto();

            ResponseEntity<?> responseEntity = advertEditingController
                    .editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testEditAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() {
            when(advertEditingService.editAdvert(eq(ADVERT_ID), any(AdvertEditingDto.class), any(AuthenticatedUser.class)))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = advertEditingController.editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }

    @Nested
    @DisplayName("When archive an advert")
    class ArchiveAdvertTests {
        @Test
        void testArchiveAdvert_shouldReturnAdvertReadDto() {
            advertReadDto.setStatus(ARCHIVED);
            when(advertEditingService.archiveAdvert(any(Long.class), any(AuthenticatedUser.class))).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testArchiveAdvert_ShouldnReturn400Status_WhenInvalidRequest() {

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(null, authenticatedUser);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testArchiveAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() {
            when(advertEditingService.archiveAdvert(ADVERT_ID, authenticatedUser))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }

    @Nested
    @DisplayName("When delete an advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_ShouldReturnAdvertReadDto() {
            doNothing().when(advertEditingService).deleteAdvert(any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("The Advert has deleted successfully", responseEntity.getBody());
        }

        @Test
        void testDeleteAdvert_ShouldnReturn404Status_WhenAdvertIdDoesNotExist() {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).deleteAdvert(ADVERT_ID, authenticatedUser);

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void testDeleteAdvert_UnauthorizedUser() {
            doThrow(new AuthorizationException("Unauthorized access"))
                    .when(advertEditingService).deleteAdvert(ADVERT_ID, authenticatedUser);

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        }

    }
}