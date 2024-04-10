package cc.fascinated.common;

import cc.fascinated.exception.impl.RateLimitException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@UtilityClass
public class WebRequest {

    /**
     * The web client.
     */
    private static final RestClient CLIENT = RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .build();

    /**
     * Gets a response from the given URL.
     *
     * @param url the url
     * @return the response
     * @param <T> the type of the response
     */
    public static <T> T getAsEntity(String url, Class<T> clazz) {
        try {
            ResponseEntity<T> profile = CLIENT.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(clazz);

            if (profile.getStatusCode().isError()) {
                return null;
            }
            if (profile.getStatusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                throw new RateLimitException("Rate limit reached");
            }
            return profile.getBody();
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }
}
