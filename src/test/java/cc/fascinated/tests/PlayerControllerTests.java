package cc.fascinated.tests;

import cc.fascinated.model.player.Skin;
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
class PlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ensurePlayerLookupUuidSuccess() throws Exception {
        mockMvc.perform(get("/player/eeab5f8a-18dd-4d58-af78-2b3c4543da48")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ImFascinated"));
    }

    @Test
    public void ensurePlayerLookupUsernameSuccess() throws Exception {
        mockMvc.perform(get("/player/ImFascinated")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ImFascinated"));
    }

    @Test
    public void ensurePlayerLookupFailure() throws Exception {
        mockMvc.perform(get("/player/invalidnamehahahahahayesslmaooo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ensurePlayerSkinPartsLookupSuccess() throws Exception {
        for (Skin.Parts part : Skin.Parts.values()) {
            mockMvc.perform(get("/player/" + part.getName() + "/eeab5f8a-18dd-4d58-af78-2b3c4543da48")
                    .accept(MediaType.IMAGE_PNG)
                    .contentType(MediaType.IMAGE_PNG))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void ensurePlayerSkinPartsLookupFailure() throws Exception {
        mockMvc.perform(get("/player/invalidpart/eeab5f8a-18dd-4d58-af78-2b3c4543da48"))
                .andExpect(status().isBadRequest());
    }
}