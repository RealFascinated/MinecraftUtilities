package xyz.mcutils.backend.tests;

import cc.fascinated.config.TestRedisConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import xyz.mcutils.backend.config.TestMongoConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestRedisConfig.class})
@DataMongoTest()
@ExtendWith(SpringExtension.class)
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
