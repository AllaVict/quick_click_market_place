package quick.click.core.controller.integr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.controller.CommentController;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Comment;
import quick.click.core.service.CommentService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.COMMENTS_URL;
import static quick.click.config.factory.CommentDtoFactory.*;
import static quick.click.config.factory.CommentDtoFactory.createCommentCreatingDto;
import static quick.click.config.factory.CommentFactory.createCommentList;
import static quick.click.config.factory.CommentFactory.createCommentOne;

@WithMockAuthenticatedUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private static final long COMMENT_ID = 101L;

    private static final long ADVERT_ID = 101L;

    private List<Comment> commentList;
    private List<CommentReadDto> commentReadDtoList;
    private Comment comment;
    private CommentReadDto commentReadDto;
    private CommentCreatingDto commentCreatingDto;
    private CommentEditingDto commentEditingDto;
    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

    @BeforeEach
    public void setUp() {
        commentController = new CommentController(commentService);
        commentList = createCommentList();
        comment = createCommentOne();
        commentReadDto = createCommentReadDtoOne();
        commentReadDtoList = createCommentReadDtoList();
        commentEditingDto = createCommentEditingDto();
        commentCreatingDto = createCommentCreatingDto();
    }

    @Nested
    @DisplayName("When register a Comment")
    class RegisterCommentTests {
        @Test
        void testRegisterComment_ShouldReturnComment() throws Exception {
            given(commentService.registerComment(anyLong(), any(CommentCreatingDto.class), any(AuthenticatedUser.class)))
                    .willReturn(commentReadDto);

            mockMvc.perform(post(VERSION_1_0 + COMMENTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreatingDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());

        }

        @Test
        void testRegisterComment_ShouldReturn400Status_WhenCommentDtoIsNull() throws Exception {
            commentCreatingDto = null;
            mockMvc.perform(post(VERSION_1_0 + COMMENTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreatingDto)))
                    .andDo(print())
                    .andExpect(result -> result.getResponse().getContentAsString().equals("Please fill all fields"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRegisterComment_ShouldReturn404Status_WhenInvalidRequested() throws Exception {

            mockMvc.perform(post(VERSION_1_0 + COMMENTS_URL + "/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDto)))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void testRegisterComment_UnauthorizedUser() throws Exception {
            given(commentService.registerComment(anyLong(), any(CommentCreatingDto.class), any(AuthenticatedUser.class)))
                    .willThrow(new AuthorizationException("Unauthorized access"));

            mockMvc.perform(post(VERSION_1_0 + COMMENTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDto)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }


    @Nested
    @DisplayName("When Find All Comments")
    class FindAllCommentsTests {

        @Test
        void testFindAllComments_ShouldReturnAllComments() throws Exception {
            given(commentService.findAllCommentsForAdvert(ADVERT_ID)).willReturn(commentReadDtoList);

            mockMvc.perform(get(VERSION_1_0 + COMMENTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testFindAllComments_ShouldReturn200Status_WhenReturnEmptyList() throws Exception {
            commentReadDtoList = new ArrayList<>();
            given(commentService.findAllCommentsForAdvert(ADVERT_ID)).willReturn(commentReadDtoList);

            mockMvc.perform(get(VERSION_1_0 + COMMENTS_URL + "/" + ADVERT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDtoList)))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void testFindAllComments_ShouldReturn404Status_WhenInvalidRequested() throws Exception {

            mockMvc.perform(get(VERSION_1_0 + COMMENTS_URL + "/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("When Delete a Comment")
    class DeleteCommentTests {
        @Test
        void testDeleteComment_ShouldReturnComment() throws Exception {
            doNothing().when(commentService).deleteComment(COMMENT_ID, authenticatedUser);

            mockMvc.perform(delete(VERSION_1_0 + COMMENTS_URL + "/" + COMMENT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("The Comment has deleted successfully")));


        }

        @Test
        void testDeleteComment_ShouldnReturn404Status_WhenCommentDoesNotExist() throws Exception {
            doThrow(new ResourceNotFoundException("Comment", "id", COMMENT_ID))
                    .when(commentService).deleteComment(anyLong(), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + COMMENTS_URL + "/" + COMMENT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteComment_ShouldnReturn404Status_WhenInvalidRequested() throws Exception {
            mockMvc.perform(delete(VERSION_1_0 + "/invalid")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentReadDto)))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());

        }

        @Test
        void testDeleteComment_UnauthorizedUser() throws Exception {
            doThrow(new AuthorizationException("Unauthorized access"))
                    .when(commentService).deleteComment(anyLong(), any(AuthenticatedUser.class));

            mockMvc.perform(delete(VERSION_1_0 + COMMENTS_URL + "/" + COMMENT_ID)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }

    }

}