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
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertEditingService;

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

    @BeforeEach
    void setUp() {
        advertReadDto = createAdvertReadDto();
        advertEditingDto =  createAdvertEditingDto();
    }

    @Nested
    @DisplayName("When edit an advert")
    class  EditAdvertTests {
        @Test
        void testEditAdvert_shouldReturnAdvertReadDto() {
            when(advertEditingService.editAdvert(ADVERT_ID, advertEditingDto)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertEditingController.editAdvert(ADVERT_ID, advertEditingDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testEditAdvert_InvalidData() {
            advertEditingDto = new AdvertEditingDto();

            ResponseEntity<?> responseEntity = advertEditingController.editAdvert(ADVERT_ID, advertEditingDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testEditAdvert_AdvertIdDoesNotExist() {
            when(advertEditingService.editAdvert(eq(ADVERT_ID), any(AdvertEditingDto.class)))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = advertEditingController.editAdvert(ADVERT_ID, advertEditingDto);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }

    @Nested
    @DisplayName("When archive an advert")
    class  ArchiveAdvertTests {
        @Test
        void testArchiveAdvert_shouldReturnAdvertReadDto() {
            advertReadDto.setStatus(ARCHIVED);
            when(advertEditingService.archiveAdvert(ADVERT_ID)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

        @Test
        void testArchiveAdvert_InvalidData() {

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(null);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testArchiveAdvert_AdvertIdDoesNotExist() {
            when(advertEditingService.archiveAdvert(ADVERT_ID))
                    .thenThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID));

            ResponseEntity<?> responseEntity = advertEditingController.archiveAdvert(ADVERT_ID);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }

    @Nested
    @DisplayName("When delete an advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_shouldReturnAdvertReadDto() {
            doNothing().when(advertEditingService).deleteAdvert(any(Long.class));

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("The Advert has deleted successfully", responseEntity.getBody());
        }
        @Test
        void testDeleteAdvert_AdvertIdDoesNotExist() {
            doThrow(new ResourceNotFoundException("Advert", "id", ADVERT_ID))
                    .when(advertEditingService).deleteAdvert(ADVERT_ID);

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

    }
}