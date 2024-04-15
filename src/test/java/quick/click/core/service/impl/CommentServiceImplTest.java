package quick.click.core.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.CommentRepository;
import quick.click.core.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.CommentDtoFactory.*;
import static quick.click.config.factory.CommentFactory.createCommentList;
import static quick.click.config.factory.CommentFactory.createCommentOne;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentServiceImpl")
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AdvertRepository advertRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TypeConverter<CommentCreatingDto, Comment> commentCreatingDtoCommentTypeConverter;
    @Mock
    private TypeConverter<Comment, CommentReadDto> commentToCommentReadDtoTypeConverter;

    @InjectMocks
    private CommentServiceImpl commentService;

    private static final long COMMENT_ID = 101L;

    private static final long ADVERT_ID = 101L;

    private List<Comment> commentList;
    private List<CommentReadDto> commentReadDtoList;
    private Comment comment;
    private CommentReadDto archiveCommentDTO;
    private CommentReadDto commentReadDto;

    private CommentCreatingDto commentCreatingDto;

    private CommentEditingDto commentEditingDto;

    private Advert advert;

    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceImpl(commentRepository, advertRepository, userRepository,
                commentCreatingDtoCommentTypeConverter, commentToCommentReadDtoTypeConverter);
        commentList = createCommentList();
        comment = createCommentOne();
        archiveCommentDTO = createCommentReadDtoOne();
        commentReadDto = createCommentReadDtoOne();
        commentReadDtoList = createCommentReadDtoList();
        commentEditingDto = createCommentEditingDto();
        commentCreatingDto = createCommentCreatingDto();
        advert = createAdvertOne();
    }

    @Nested
    @DisplayName("When Find Comment By Id")
    class FindCommentByIdTests {
        @Test
        void testFindCommentById_ShouldReturnComment() {
            when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.ofNullable(comment));
            when(commentToCommentReadDtoTypeConverter.convert(comment)).thenReturn(commentReadDto);

            CommentReadDto result = commentService.findCommentById(COMMENT_ID);

            assertEquals(commentReadDto, result);
            verify(commentRepository).findById(any(Long.class));
            assertNotNull(result);
            assertEquals(result, commentReadDto);
            assertThat(result.getMessage()).isEqualTo(commentReadDto.getMessage());

        }

        @Test
        void testFindBaseFeeById_ShouldThrowException() {

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.findCommentById(COMMENT_ID));
        }

    }

    @Nested
    @DisplayName("When Find All Comments")
    class FindAllBaseFeesTests {

        @Test
        void testFindAllComments_ShouldReturnAllComments() {
            commentList = List.of(comment, comment);
            commentReadDtoList = List.of(commentReadDto, commentReadDto);
            when(commentRepository.findAllByAdvertId(anyLong())).thenReturn(commentList);
            when(commentToCommentReadDtoTypeConverter.convert(any(Comment.class))).thenReturn(commentReadDto);

            List<CommentReadDto> result = commentService.findAllCommentsForAdvert(ADVERT_ID);

            verify(commentRepository).findAllByAdvertId(anyLong());
            assertNotNull(result);
            assertEquals(result, commentReadDtoList);
            Assertions.assertThat(result.size()).isEqualTo(commentReadDtoList.size());
        }

        @Test
        void testFindAllComments_ShouldReturnEmptyList() {
            commentRepository.deleteAll();
            commentReadDtoList = new ArrayList<>();
            commentList = new ArrayList<>();
            when(commentRepository.findAllByAdvertId(ADVERT_ID)).thenReturn(commentList);

            List<CommentReadDto> result = commentService.findAllCommentsForAdvert(ADVERT_ID);

            verify(commentRepository).findAllByAdvertId(ADVERT_ID);
            assertNotNull(result);
            assertEquals(result, commentReadDtoList);
            assertTrue(result.isEmpty());

        }

    }

    @Nested
    @DisplayName("When Create a Comment")
    class RegisterCommentTests {
        @Test
        void testRegisterComment_ShouldReturnComment() {
            when(commentCreatingDtoCommentTypeConverter.convert(commentCreatingDto)).thenReturn(comment);
            when(commentRepository.saveAndFlush(comment)).thenReturn(comment);
            when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.ofNullable(advert));
            when(commentToCommentReadDtoTypeConverter.convert(comment)).thenReturn(commentReadDto);

            CommentReadDto result = commentService.registerComment(ADVERT_ID,commentCreatingDto);

            assertEquals(commentReadDto, result);
            assertThat(result.getMessage()).isEqualTo(commentReadDto.getMessage());
            verify(commentRepository).saveAndFlush(any(Comment.class));
            assertNotNull(result);
        }

        @Test
        void testRegisterComment_shouldThrowException() {
            commentReadDto = null;
            assertThrows(Exception.class,
                    () -> commentService.registerComment(ADVERT_ID,commentCreatingDto));
        }
    }


    @Nested
    @DisplayName("When edit a Comment")
    class EditCommentTests {
        @Test
        void testEditComment_shouldReturnCommentWithArchiveStatus() {
            when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.ofNullable(comment));
            when(commentRepository.saveAndFlush(comment)).thenReturn(comment);
            when(commentToCommentReadDtoTypeConverter.convert(comment)).thenReturn(archiveCommentDTO);

            CommentReadDto result = commentService.editComment(COMMENT_ID, commentEditingDto);

            assertEquals(archiveCommentDTO, result);
            assertThat(result.getMessage()).isEqualTo(commentReadDto.getMessage());
            assertNotNull(result);
            verify(commentRepository).saveAndFlush(any(Comment.class));

        }

        @Test
        void testEditComment_shouldThrowException() {
            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.editComment(COMMENT_ID, commentEditingDto));

        }

    }

    @Nested
    @DisplayName("When Delete a Comment")
    class DeleteCommentTests {
        @Test
        void testDeleteComment_shouldReturnBaseFee() {
            when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
            commentService.deleteComment(COMMENT_ID);
            verify(commentRepository, times(1)).delete(comment);

        }

        @Test
        void testDeleteComment_shouldThrowException() {

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.deleteComment(COMMENT_ID));
            verify(commentRepository, times(0)).delete(comment);

        }

    }

}