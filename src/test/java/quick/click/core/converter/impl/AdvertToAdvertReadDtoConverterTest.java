package quick.click.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;
import quick.click.config.factory.AdvertFactory;
import quick.click.config.factory.UserFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("AdvertToAdvertReadDtoConverterTest")
class AdvertToAdvertReadDtoConverterTest {

    @Mock
    private TypeConverter<User, UserReadDto> userToUserReadDtoConverter;

    @Mock
    private TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto;

    @InjectMocks
    private TypeConverter<Advert, AdvertReadDto>  advertToAdvertReadDtoConverter;

    private Advert advert;

    private User user;

    private UserReadDto userReadDto;

    @BeforeEach
    public void setUp() {
        user = UserFactory.createUser();
        advert = AdvertFactory.createAdvert();
        userToUserReadDtoConverter =new UserToUserReadDtoConverter();
        typeConverterCommentReadDto =new CommentToCommentReadDtoConverter();
        advertToAdvertReadDtoConverter = new AdvertToAdvertReadDtoConverter(
                userToUserReadDtoConverter, typeConverterCommentReadDto);
    }

    @Test
    void shouldGetTargetClass() {
        assertEquals(AdvertReadDto.class, advertToAdvertReadDtoConverter.getTargetClass());
    }

    @Test
    void shouldGetSourceClass() {
        assertEquals(Advert.class, advertToAdvertReadDtoConverter.getSourceClass());
    }

    @Test
    void testConvert_shouldConvertAdvertToAdvertReadDto() {
        AdvertReadDto result = advertToAdvertReadDtoConverter.convert(advert);

        assertNotNull(result);
        assertEquals(advert.getDescription(), result.getDescription());
        assertEquals(advert.getTitle(), result.getTitle());
    }

}