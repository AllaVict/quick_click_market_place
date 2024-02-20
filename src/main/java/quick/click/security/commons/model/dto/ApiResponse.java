package quick.click.security.commons.model.dto;

import java.util.Objects;

public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, final String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse that = (ApiResponse) o;
        return success == that.success && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
