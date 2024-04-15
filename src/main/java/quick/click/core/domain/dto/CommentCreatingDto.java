package quick.click.core.domain.dto;


import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentCreatingDto {

    @NotEmpty
    private String message;
    private LocalDateTime createdDate;

    private Long advertId;

    private Long userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentCreatingDto that = (CommentCreatingDto) o;
        return Objects.equals(message, that.message)  && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, createdDate);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                ", message='" + message + '\'' +
                ", createdDate=" + createdDate +
                 '}';
    }
}
