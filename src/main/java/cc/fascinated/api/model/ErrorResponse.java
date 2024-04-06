package cc.fascinated.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@ToString
public final class ErrorResponse {
    /**
     * The status code of this error.
     */
    @NonNull
    private HttpStatus status;

    /**
     * The message of this error.
     */
    @NonNull private String message;

    /**
     * The timestamp this error occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    public ErrorResponse(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status;
        this.message = message;
        timestamp = new Date();
    }
}