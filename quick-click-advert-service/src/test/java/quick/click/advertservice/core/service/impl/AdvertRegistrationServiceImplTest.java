package quick.click.advertservice.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.advertservice.core.converter.impl.AdvertCreateDtoToAdvertConverter;
import quick.click.advertservice.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.repository.FileReferenceRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.advertservice.factory.AdvertDtoFactory.*;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;
import static quick.click.advertservice.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertRegistrationServiceImpl")
class AdvertRegistrationServiceImplTest {
    @Mock
    private AdvertRepository advertRepository;
    private AdvertToAdvertReadDtoConverter typeConverterReadDto =mock(AdvertToAdvertReadDtoConverter.class);

    private AdvertCreateDtoToAdvertConverter typeConverterCreateDto = mock(AdvertCreateDtoToAdvertConverter.class);
    @Mock
    private FileReferenceRepository fileReferenceRepository;

    AdvertRegistrationServiceImpl advertRegistrationService ;
    private Advert advert;
    private AdvertReadDto advertReadDto;
    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertCreateDto = createAdvertCreateDto();
        advertRegistrationService =new AdvertRegistrationServiceImpl(advertRepository,
                typeConverterReadDto, typeConverterCreateDto);

    }

    @Nested
    @DisplayName("When register a Advert")
    class RegisterAdvertTests {
        @Test
        void registerAdvert_shouldReturnAdvertReadDto() {
            /**
             System.out.println("!!!!! advert "+advert);
             System.out.println("!!!!! advertCreateDto "+advertCreateDto);
             System.out.println("!!!!! advertReadDto "+advertReadDto);
             when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.of(advert));
             when(advertRepository.saveAndFlush(advert)).thenReturn(advert);
             when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);
             AdvertReadDto result = advertEditingService.editAdvert(ADVERT_ID, advertEditingDto);

             AdvertReadDto advertReadDto= Optional.of(advertCreateDto)
             .map(typeConverterCreateDto::convert)
             .map(advertRepository::saveAndFlush)
             .map(typeConverterReadDto::convert)
             .orElseThrow();
             */
            when(typeConverterCreateDto.convert(advertCreateDto)).thenReturn(advert);
            when(advertRepository.saveAndFlush(advert)).thenReturn(advert);
            when(typeConverterReadDto.convert(any(Advert.class))).thenReturn(advertReadDto);

            AdvertReadDto result = advertRegistrationService.registerAdvert(advertCreateDto);

            assertEquals(advertReadDto, result);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
            verify(advertRepository).saveAndFlush(any(Advert.class));
            assertNotNull(result);
        }
        @Test
        void testRegisterAdvert_shouldThrowException() {

            assertThrows(NoSuchElementException.class,
                    () -> advertRegistrationService.registerAdvert(advertCreateDto));

        }

    }

}