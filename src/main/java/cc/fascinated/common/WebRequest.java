package cc.fascinated.common;

import cc.fascinated.exception.impl.RateLimitException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@UtilityClass
public class WebRequest {

    /**
     * The web client.
     */
    private static final RestClient CLIENT;

    static {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000); // 5 seconds
        CLIENT = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * Gets a response from the given URL.
     *
     * @param url the url
     * @return the response
     * @param <T> the type of the response
     */
    public static <T> T getAsEntity(String url, Class<T> clazz) throws RateLimitException {
        ResponseEntity<T> profile = CLIENT.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {}) // Don't throw exceptions on error
                .toEntity(clazz);

        if (profile.getStatusCode().isError()) {
            return null;
        }
        if (profile.getStatusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
            throw new RateLimitException("Rate limit reached");
        }
        return profile.getBody();
    }

    /**
     * Gets a response from the given URL.
     *
     * @param url the url
     * @return the response
     */
    public static ResponseEntity<?> getAndIgnoreErrors(String url) {
        return CLIENT.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {}) // Don't throw exceptions on error
                .toEntity(String.class);
    }
}
