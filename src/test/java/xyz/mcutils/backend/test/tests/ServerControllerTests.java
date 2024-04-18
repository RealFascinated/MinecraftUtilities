package xyz.mcutils.backend.test.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.mcutils.backend.test.config.TestRedisConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { TestRedisConfig.class })
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerControllerTests {

    private final String testServer = "play.hypixel.net";
    private final String testInvalidServer = "invalidhostnamehahahahahayesslmaooo";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ensureServerLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/java/" + testServer)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostname").value(testServer));
    }

    @Test
    public void ensureServerLookupFailure() throws Exception {
        mockMvc.perform(get("/server/java/" + testInvalidServer)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ensureServerIconLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/icon/" + testServer)
                .contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk());
    }

    @Test
    public void ensureBlockedServerLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/blocked/" + testServer)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(false));
    }
}
