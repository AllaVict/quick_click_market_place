package quick.click.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.UserRepository;
import quick.click.config.factory.AdvertDtoFactory;
import quick.click.config.factory.AdvertFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("AdvertCreateDtoToAdvertConverter")
class AdvertCreateDtoToAdvertConverterTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TypeConverter<AdvertCreateDto, Advert> advertCreateDtoAdvertTypeConverter;

    private Advert advert;

    private AdvertCreateDto advertCreateDto;

    @BeforeEach
    public void setUp() {
        advert = AdvertFactory.createAdvertOne();
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