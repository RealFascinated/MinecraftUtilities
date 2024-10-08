package xyz.mcutils.backend.exception;

import io.micrometer.common.lang.NonNull;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import xyz.mcutils.backend.model.response.ErrorResponse;

@ControllerAdvice
public final class ExceptionControllerAdvice {

    /**
     * Handle a raised exception.
     *
     * @param ex the raised exception
     * @return the error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(@NonNull Exception ex) {
        HttpStatus status = null; // Get the HTTP status
        if (ex instanceof NoResourceFoundException) { // Not found
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof UnsupportedOperationException) { // Not implemented
            status = HttpStatus.NOT_IMPLEMENTED;
        }
        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) { // Get from the @ResponseStatus annotation
            status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        }
        String message = ex.getLocalizedMessage(); // Get the error message
        if (message == null) { // Fallback
            message = "An internal error has occurred.";
        }
        // Print the stack trace if no response status is present
        if (status == null) {
            ex.printStackTrace();
        }
        if (status == null) { // Fallback to 500
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            Sentry.captureException(ex); // Capture the exception with Sentry
        }
        return new ResponseEntity<>(new ErrorResponse(status, message), status);
    }
}