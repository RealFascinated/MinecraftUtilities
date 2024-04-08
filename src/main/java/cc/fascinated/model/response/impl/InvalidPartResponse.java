package cc.fascinated.model.response.impl;

import cc.fascinated.model.response.Response;
import org.springframework.http.HttpStatus;

public class InvalidPartResponse extends Response {

    public InvalidPartResponse() {
        super(HttpStatus.NOT_FOUND, "Invalid part name.");
    }
}
