package quick.click.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import quick.click.config.factory.AdvertFactory;
import quick.click.config.factory.UserFactory;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static quick.click.config.factory.CommentFactory.createCommentOne;

@DisplayName("CommentToCommentReadDtoConverter")
class CommentToCommentReadDtoConverterTest {

    @InjectMocks
    private TypeConverter<Comment, CommentReadDto> commentToCommentReadDtoTypeConverter;

    private Comment comment;

    @BeforeEach
    public void setUp() {
        comment = createCommentOne();
        commentToCommentReadDtoTypeConverter = new CommentToCommentReadDtoConverter();

    }

    @Test
    void shouldGetTargetClass() {
        assertEquals(CommentReadDto.class, commentToCommentReadDtoTypeConverter.getTargetClass());
    }

    @Test
    void shouldGetSourceClass() {

        assertEquals(Comment.class, commentToCommentReadDtoTypeConverter.getSourceClass());
    }

    @Test
    void testConvert_shouldConvertCommentToCommentReadDto() {
        CommentReadDto result = commentToCommentReadDtoTypeConverter.convert(comment);

        assertNotNull(result);
        assertEquals(comment.getMessage(), result.getMessage());
        assertEquals(comment.getUsername(), result.getUsername());
    }

}