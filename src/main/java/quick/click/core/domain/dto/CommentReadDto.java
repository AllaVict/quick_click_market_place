package quick.click.core.domain.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentReadDto {

    private Long id;
    private String message;

    private String username;

    private Long advertId;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
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
        CommentReadDto that = (CommentReadDto) o;
        return Objects.equals(id, that.id) && Objects.equals(message, that.message) && Objects.equals(username, that.username) && Objects.equals(advertId, that.advertId) && Objects.equals(updatedDate, that.updatedDate) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, username, advertId, updatedDate, createdDate);
    }
}
