package quick.click.advertservice.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.advertservice.commons.exceptions.ResourceNotFoundException;
import quick.click.advertservice.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.advertservice.core.domain.dto.AdvertEditingDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.repository.FileReferenceRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static quick.click.advertservice.factory.AdvertDtoFactory.*;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertEditingServiceImpl")
class AdvertEditingServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private FileReferenceRepository fileReferenceRepository;

    @Mock
    private AdvertToAdvertReadDtoConverter typeConverterReadDto;

    @InjectMocks
    AdvertEditingServiceImpl advertEditingService;

    private static final long ADVERT_ID = 101L;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertEditingDto = createAdvertEditingDto();
    }
    @Nested
    @DisplayName("When Edit a Advert")
    class  EditAdvertTests {
        @Test
        void testEditAdvert_shouldReturnAdvertReadDto() {
            when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.of(advert));
            when(advertRepository.saveAndFlush(advert)).thenReturn(advert);
            when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertEditingService.editAdvert(ADVERT_ID, advertEditingDto);

            assertEquals(advertReadDto, result);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
            assertNotNull(result);
            verify(advertRepository).saveAndFlush(any(Advert.class));
        }

        @Test
        void testUpdateAdvert_shouldReturnAdvertReadDto() {
            Advert result = advertEditingService.updateAdvertData(advert, advertEditingDto);

            assertNotNull(result);
            assertEquals(advert, result);
            assertThat(result.getTitle()).isEqualTo(advert.getTitle());
        }
        @Test
        void testEditAdvert_shouldThrowException() {
            assertThrows(ResourceNotFoundException.class,
                    () -> advertEditingService.editAdvert(ADVERT_ID, advertEditingDto));
        }

    }

    @Nested
    @DisplayName("When Delete a Advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_shouldReturnAdvertReadDto() {
            when(advertRepository.findById(anyLong())).thenReturn(Optional.of(advert));
            doNothing().when(advertRepository).delete(any(Advert.class));

            advertEditingService.deleteAdvert(ADVERT_ID);

            verify(advertRepository, times(1)).delete(advert);
       }
        @Test
        void testDeleteAdvert_shouldThrowException() {

            assertThrows(ResourceNotFoundException.class,
                    () -> advertEditingService.deleteAdvert(ADVERT_ID));
            verify(advertRepository, times(0)).delete(advert);
        }

    }

 }

