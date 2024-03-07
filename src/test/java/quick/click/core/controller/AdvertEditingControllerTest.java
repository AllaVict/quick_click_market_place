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
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.service.AdvertEditingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertEditingDto;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertEditingController")
class AdvertEditingControllerTest {

    @Mock
    private AdvertEditingService advertEditingService;

    @InjectMocks
    AdvertEditingController advertEditingController;

    private static final long ADVERT_ID = 101L;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertEditingDto =  createAdvertEditingDto();
    }

    @Nested
    @DisplayName("When Edit a Advert")
    class  EditAdvertTests {
        @Test
        void testEditAdvert_shouldReturnAdvertReadDto() {
            when(advertEditingService.editAdvert(ADVERT_ID, advertEditingDto)).thenReturn(advertReadDto);

            ResponseEntity<?> responseEntity = advertEditingController.editAdvert(ADVERT_ID, advertEditingDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(advertReadDto, responseEntity.getBody());
        }

    }

    @Nested
    @DisplayName("When Delete a Advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_shouldReturnAdvertReadDto() {
            doNothing().when(advertEditingService).deleteAdvert(any(Long.class));

            ResponseEntity<String> responseEntity = advertEditingController.deleteAdvert(ADVERT_ID);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("The Advert has deleted successfully", responseEntity.getBody());
        }

    }
}