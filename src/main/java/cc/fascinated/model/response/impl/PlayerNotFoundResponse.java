package cc.fascinated.model.response.impl;

import cc.fascinated.model.response.Response;
import org.springframework.http.HttpStatus;

public class PlayerNotFoundResponse extends Response {

    public PlayerNotFoundResponse() {
        super(HttpStatus.NOT_FOUND, "Player not found.");
    }
}
