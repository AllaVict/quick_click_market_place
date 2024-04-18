package quick.click.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import quick.click.config.factory.AdvertFactory;
import quick.click.config.factory.UserFactory;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static quick.click.config.factory.CommentDtoFactory.createCommentCreatingDto;

@DisplayName("CommentCreatingDtoToCommentConverter")
class CommentCreatingDtoToCommentConverterTest {
    @Mock

    private UserRepository userRepository;
    @Mock
    private AdvertRepository advertRepository;


    @InjectMocks
        private TypeConverter<CommentCreatingDto, Comment> commentCreatingDtoCommentTypeConverter;

    private CommentCreatingDto commentCreatingDto;

        @BeforeEach
        public void setUp() {
            commentCreatingDto = createCommentCreatingDto();
            userRepository = Mockito.mock(UserRepository.class);
            advertRepository = Mockito.mock(AdvertRepository.class);
            commentCreatingDtoCommentTypeConverter = new CommentCreatingDtoToCommentConverter(userRepository, advertRepository);
        }

        @Test
        void shouldGetTargetClass() {
            assertEquals(Comment.class, commentCreatingDtoCommentTypeConverter.getTargetClass());
        }

        @Test
        void shouldGetSourceClass() {
            assertEquals(CommentCreatingDto.class, commentCreatingDtoCommentTypeConverter.getSourceClass());
        }

        @Test
        void testConvert_shouldConvertAdvertToAdvertReadDto() {
            Comment result = commentCreatingDtoCommentTypeConverter.convert(commentCreatingDto);

            assertNotNull(result);
            assertEquals(commentCreatingDto.getMessage(), result.getMessage());
            assertEquals(commentCreatingDto.getCreatedDate(), result.getCreatedDate());
        }

    }