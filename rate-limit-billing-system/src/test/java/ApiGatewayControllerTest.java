import com.example.MainApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainApplication.class)
@AutoConfigureMockMvc
public class ApiGatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateLimiting() throws Exception {
        for (int i = 0; i < 1001; i++) {
            mockMvc.perform(get("/api/api1?user=user1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Request processed successfully"));

            /*mockMvc.perform(post("/api/api2?user=user1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Request processed successfully"));

            mockMvc.perform(put("/api/api3?user=user1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Request processed successfully"));*/
        }

        // 超过限制
        mockMvc.perform(get("/api/api1?user=user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rate limit exceeded. Please try again later."));

        /*mockMvc.perform(post("/api/api2?user=user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rate limit exceeded. Please try again later."));

        mockMvc.perform(put("/api/api3?user=user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rate limit exceeded. Please try again later."));*/
    }
}