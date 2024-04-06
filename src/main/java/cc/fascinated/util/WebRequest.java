package cc.fascinated.util;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

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
    public static <T> T get(String url) {
        try {
            ResponseEntity<T> profile = CLIENT.get()
                    .uri(url)
                    .retrieve()
                    .toEntity((Class<T>) Object.class);

            if (profile.getStatusCode().isError()) {
                return null;
            }
            return profile.getBody();
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }
}
