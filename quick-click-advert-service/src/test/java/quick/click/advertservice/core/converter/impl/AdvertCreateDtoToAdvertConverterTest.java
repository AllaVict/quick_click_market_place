package quick.click.advertservice.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.UserRepository;
import quick.click.advertservice.factory.AdvertDtoFactory;
import quick.click.advertservice.factory.AdvertFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DisplayName("AdvertCreateDtoToAdvertConverterTest")
class AdvertCreateDtoToAdvertConverterTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TypeConverter<AdvertCreateDto, Advert>  advertCreateDtoAdvertTypeConverter;

    private Advert advert;

    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    public void setUp() {
        advert = AdvertFactory.createAdvert();
        advertCreateDto = AdvertDtoFactory.createAdvertCreateDto();
        userRepository = Mockito.mock(UserRepository.class);
        advertCreateDtoAdvertTypeConverter = new AdvertCreateDtoToAdvertConverter(userRepository);
    }

    @Test
    void shouldGetTargetClass() {
        assertEquals(Advert.class, advertCreateDtoAdvertTypeConverter.getTargetClass());
    }

    @Test
    void shouldGetSourceClass() {
        assertEquals(AdvertCreateDto.class, advertCreateDtoAdvertTypeConverter.getSourceClass());
    }

    @Test
    void testConvert_shouldConvertAdvertCreateDtoAdvert() {
        Advert result = advertCreateDtoAdvertTypeConverter.convert(advertCreateDto);

        assertNotNull(result);
        assertEquals(advert.getDescription(), result.getDescription());
        assertEquals(advert.getTitle(), result.getTitle());
    }

}