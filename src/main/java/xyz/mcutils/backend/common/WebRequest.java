package xyz.mcutils.backend.common;

import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import xyz.mcutils.backend.exception.impl.RateLimitException;

@UtilityClass
public class WebRequest {

    /**
     * The web client.
     */
    private static final RestClient CLIENT;

    static {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2500); // 2.5 seconds
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
        ResponseEntity<T> responseEntity = CLIENT.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {}) // Don't throw exceptions on error
                .toEntity(clazz);

        if (responseEntity.getStatusCode().isError()) {
            return null;
        }
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
            throw new RateLimitException("Rate limit reached");
        }
        return responseEntity.getBody();
    }

    /**
     * Gets a response from the given URL.
     *
     * @param url the url
     * @return the response
     */
    public static ResponseEntity<?> get(String url, Class<?> clazz) {
        return CLIENT.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {}) // Don't throw exceptions on error
                .toEntity(clazz);
    }

    /**
     * Gets a response from the given URL.
     *
     * @param url the url
     * @return the response
     */
    public static ResponseEntity<?> head(String url, Class<?> clazz) {
        return CLIENT.head()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {}) // Don't throw exceptions on error
                .toEntity(clazz);
    }
}
