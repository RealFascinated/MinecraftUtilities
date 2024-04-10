package cc.fascinated.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import cc.fascinated.config.TestRedisConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestRedisConfig.class)
class ServerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ensureServerLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/java/play.hypixel.net")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.server.hostname").value("play.hypixel.net"));
    }

    @Test
    public void ensureServerLookupFailure() throws Exception {
        mockMvc.perform(get("/server/java/invalidhostnamehahahahahayesslmaooo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ensureServerIconLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/icon/play.hypixel.net")
                .contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk());
    }

    @Test
    public void ensureBlockedServerLookupSuccess() throws Exception {
        mockMvc.perform(get("/server/blocked/play.hypixel.net")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(false));
    }
}
