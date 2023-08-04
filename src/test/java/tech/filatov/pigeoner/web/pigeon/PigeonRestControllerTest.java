package tech.filatov.pigeoner.web.pigeon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.filatov.pigeoner.dto.FilterParams;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PigeonRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/pigeons"))
                .andExpect(status().isOk());
    }

    @Test
    void get_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/pigeons/100017"))
                .andExpect(status().isOk());
    }

    @Test
    void getFiltered_isOkStatus() throws Exception {
        mockMvc.perform(post("/api/v1/pigeons/filter")
                        .content(objectMapper.writeValueAsString(new FilterParams()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getWithAncestors_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/pigeons/100017/with-ancestors"))
                .andExpect(status().isOk());
    }
}