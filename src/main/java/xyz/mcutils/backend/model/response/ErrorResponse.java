package xyz.mcutils.backend.model.response;

import io.micrometer.common.lang.NonNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter @ToString @EqualsAndHashCode
public class ErrorResponse {
    /**
     * The status code of this error.
     */
    @NonNull
    private final HttpStatus status;

    /**
     * The HTTP code of this error.
     */
    private final int code;

    /**
     * The message of this error.
     */
    @NonNull private final String message;

    /**
     * The timestamp this error occurred.
     */
    @NonNull private final Date timestamp;

    public ErrorResponse(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status;
        code = status.value();
        this.message = message;
        timestamp = new Date();
    }
}