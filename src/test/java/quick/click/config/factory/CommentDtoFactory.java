package quick.click.config.factory;


import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static quick.click.config.factory.AdvertFactory.createAdvertOne;

public class CommentDtoFactory {

    private static final long COMMENT_ONE_ID = 101L;

    private static final long COMMENT_TWO_ID = 102L;

    private static final String MESSAGE_ONE = "perfect!! Do you propose some discount?";

    private static final String MESSAGE_TWO = "das ist exzellent, guter Preis";

    private static final String USERNAME = "John Johnson";

    private static final long ADVERT_ID = 101L;

    private static final long USER_ID = 101L;

    private static final LocalDateTime CREATED_DATE_ONE = LocalDateTime.of(2024, 2, 24, 20, 24);

    private static final LocalDateTime CREATED_DATE_TWO = LocalDateTime.of(2024, 3, 24, 20, 24);

    public static List<CommentReadDto> createCommentReadDtoList() {
        return Arrays.asList(
                createCommentReadDtoOne(),
                createCommentReadDtoTwo()
        );
    }

    public static CommentReadDto createCommentReadDtoOne() {
        final CommentReadDto commentReadDto = new CommentReadDto();
        commentReadDto.setId(COMMENT_ONE_ID);
        commentReadDto.setMessage(MESSAGE_ONE);
        commentReadDto.setAdvertId(ADVERT_ID);
        commentReadDto.setUsername(USERNAME);
        commentReadDto.setCreatedDate(CREATED_DATE_ONE);
        return commentReadDto;
    }

    public static CommentReadDto createCommentReadDtoTwo() {
        final CommentReadDto commentReadDto = new CommentReadDto();
        commentReadDto.setId(COMMENT_TWO_ID);
        commentReadDto.setMessage(MESSAGE_TWO);
        commentReadDto.setAdvertId(ADVERT_ID);
        commentReadDto.setUsername(USERNAME);
        commentReadDto.setCreatedDate(CREATED_DATE_TWO);
        return commentReadDto;
    }

    public static CommentEditingDto createCommentEditingDto() {
        final CommentEditingDto commentEditingDto = new CommentEditingDto();
        commentEditingDto.setId(COMMENT_ONE_ID);
        commentEditingDto.setMessage(MESSAGE_ONE);
        commentEditingDto.setAdvertId(ADVERT_ID);
        commentEditingDto.setUserId(USER_ID);
        commentEditingDto.setCreatedDate(CREATED_DATE_ONE);
        return commentEditingDto;
    }

    public static CommentCreatingDto createCommentCreatingDto() {
        final CommentCreatingDto commentCreatingDto = new CommentCreatingDto();
        commentCreatingDto.setMessage(MESSAGE_ONE);
        commentCreatingDto.setAdvertId(ADVERT_ID);
        commentCreatingDto.setUserId(USER_ID);
        commentCreatingDto.setCreatedDate(CREATED_DATE_ONE);
        return commentCreatingDto;
    }

}
