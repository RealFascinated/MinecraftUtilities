package xyz.mcutils.backend.exception.impl;


public class MojangAPIRateLimitException extends RateLimitException {

    public MojangAPIRateLimitException() {
        super("Mojang API rate limit exceeded. Please try again later.");
    }
}
