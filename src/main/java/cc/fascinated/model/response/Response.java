package cc.fascinated.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter @AllArgsConstructor
public class Response {

    /**
     * The status code of this error.
     */
    private HttpStatus status;

    /**
     * The message of this error.
     */
    private String message;

    /**
     * Gets this response as a {@link ResponseEntity}.
     *
     * @return the response entity
     */
    public ResponseEntity<?> toResponseEntity() {
        return new ResponseEntity<>(this, status);
    }
}
