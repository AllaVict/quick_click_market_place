package quick.click.advertservice.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.core.repository.AdvertRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertCreateDto;
import static quick.click.advertservice.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.advertservice.factory.AdvertFactory.createAdvert;
import static quick.click.advertservice.factory.UserDtoFactory.createUserReadDto;
import static quick.click.advertservice.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertRegistrationServiceImpl")
class AdvertRegistrationServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    @Mock
    private TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto;

    @InjectMocks
    AdvertRegistrationServiceImpl advertRegistrationService;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    void setUp() {
        User user = createUser();
        advert = createAdvert(user);
        advertReadDto = createAdvertReadDto(createUserReadDto());
        advertCreateDto = createAdvertCreateDto(user.getId());
    }

    /**
     advertCreateDto.setCreatedDate(LocalDateTime.now());
     AdvertReadDto advertReadDto= Optional.of(advertCreateDto)
     .map(advert -> typeConverterCreateDto.convert(advertCreateDto))
     .map(advertRepository::saveAndFlush)
     .map(typeConverterReadDto::convert)
     .orElseThrow();
     */

    @Nested
    @DisplayName("When register a Advert")
    class registerAdvertTests {
        @Test
        void registerAdvert_shouldReturnsAdvertReadDto() {
            when(typeConverterCreateDto.convert(advertCreateDto)).thenReturn(advert);
            when(advertRepository.save(any(Advert.class))).thenReturn(advert);
            when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertRegistrationService.registerAdvert(advertCreateDto);

            verify(advertRepository).saveAndFlush(any(Advert.class));
            assertNotNull(result);
            assertEquals(advertReadDto, result);
            assertThat(result.getTitle()).isEqualTo(advertCreateDto.getTitle());
        }
    }

}