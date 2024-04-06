package cc.fascinated.mojang;

import cc.fascinated.Main;
import cc.fascinated.mojang.types.MojangApiProfile;
import cc.fascinated.mojang.types.MojangSessionServerProfile;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MojangAPIService {

    @Value("${mojang.session-server}")
    private String mojangSessionServerUrl;

    @Value("${mojang.api}")
    private String mojangApiUrl;

    /**
     * Gets the Session Server profile of the player with the given UUID.
     *
     * @param id the uuid or name of the player
     * @return the profile
     */
    @SneakyThrows
    public MojangSessionServerProfile getSessionServerProfile(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mojangSessionServerUrl + "/session/minecraft/profile/" + id))
                .GET()
                .build();

        HttpResponse<String> response = Main.getCLIENT().send(request, HttpResponse.BodyHandlers.ofString());
        return Main.getGSON().fromJson(response.body(), new TypeToken<MojangSessionServerProfile>(){}.getType());
    }

    /**
     * Gets the Mojang API profile of the player with the given UUID.
     *
     * @param id the name of the player
     * @return the profile
     */
    @SneakyThrows
    public MojangApiProfile getApiProfile(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mojangApiUrl + "/users/profiles/minecraft/" + id))
                .GET()
                .build();

        HttpResponse<String> response = Main.getCLIENT().send(request, HttpResponse.BodyHandlers.ofString());
        return Main.getGSON().fromJson(response.body(), new TypeToken<MojangApiProfile>(){}.getType());
    }
}
