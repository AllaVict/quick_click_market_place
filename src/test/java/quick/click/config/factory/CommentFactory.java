package quick.click.config.factory;


import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static quick.click.config.factory.AdvertFactory.createAdvertOne;

public class CommentFactory {

    private static final long COMMENT_ONE_ID = 101L;

    private static final long COMMENT_TWO_ID = 102L;

    private static final String MESSAGE_ONE = "perfect!! Do you propose some discount?";

    private static final String MESSAGE_TWO = "das ist exzellent, guter Preis";

    private static final String USERNAME = "John Johnson";

    private static final long USER_ID = 101L;

    private static final LocalDateTime CREATED_DATE_ONE = LocalDateTime.of(2024, 2, 24, 20, 24);

    private static final LocalDateTime CREATED_DATE_TWO = LocalDateTime.of(2024, 3, 24, 20, 24);

    public static List<Comment> createCommentList() {
        return Arrays.asList(
                createCommentOne(),
                createCommentTwo()
        );
    }

    public static Comment createCommentOne() {
        final Comment comment = new Comment();
        comment.setId(COMMENT_ONE_ID);
        comment.setMessage(MESSAGE_ONE);
        comment.setAdvert(createAdvertOne());
        comment.setUsername(USERNAME);
        comment.setUserId(USER_ID);
        comment.setCreatedDate(CREATED_DATE_ONE);
        return comment;
    }

    public static Comment createCommentTwo() {
        final Comment comment = new Comment();
        comment.setId(COMMENT_TWO_ID);
        comment.setMessage(MESSAGE_TWO);
        comment.setAdvert(createAdvertOne());
        comment.setUsername(USERNAME);
        comment.setUserId(USER_ID);
        comment.setCreatedDate(CREATED_DATE_TWO);
        return comment;
    }

    public static Comment createCommentOneWithUserAndAdvert(final User user,
                                                            final Advert advert) {
        final Comment comment = new Comment();
        comment.setId(COMMENT_ONE_ID);
        comment.setMessage(MESSAGE_ONE);
        comment.setAdvert(advert);
        comment.setUsername(USERNAME);
        comment.setUserId(user.getId());
        comment.setCreatedDate(CREATED_DATE_ONE);
        return comment;
    }

    public static Comment createCommentTwoWithUserAndAdvert(final User user,
                                                            final Advert advert) {
        final Comment comment = new Comment();
        comment.setId(COMMENT_TWO_ID);
        comment.setMessage(MESSAGE_TWO);
        comment.setAdvert(advert);
        comment.setUsername(USERNAME);
        comment.setUserId(user.getId());
        comment.setCreatedDate(CREATED_DATE_TWO);
        return comment;
    }

}
