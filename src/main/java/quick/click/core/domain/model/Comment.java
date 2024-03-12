package quick.click.core.domain.model;

import jakarta.persistence.*;
import quick.click.core.domain.BaseEntity;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String message;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = " advert_id")
    private Advert advert;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Comment() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(advert, comment.advert) && Objects.equals(username, comment.username) && Objects.equals(userId, comment.userId) && Objects.equals(message, comment.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, advert, username, userId, message);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                '}';
    }
}
