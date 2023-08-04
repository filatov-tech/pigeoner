package tech.filatov.pigeoner.web.flight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/pigeons"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/pigeons"))
                .andExpect(status().isOk());
    }
}