package cc.fascinated.exception.impl;


public class MojangAPIRateLimitException extends RateLimitException {

    public MojangAPIRateLimitException() {
        super("Mojang API rate limit exceeded. Please try again later.");
    }
}
