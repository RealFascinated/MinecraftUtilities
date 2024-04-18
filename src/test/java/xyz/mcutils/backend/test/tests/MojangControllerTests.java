package xyz.mcutils.backend.test.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.mcutils.backend.test.config.TestRedisConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { TestRedisConfig.class })
@AutoConfigureMockMvc
class MojangControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ensureEndpointStatusLookupSuccess() throws Exception {
        mockMvc.perform(get("/mojang/status")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
