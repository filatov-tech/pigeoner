package tech.filatov.pigeoner.web.section;

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
class SectionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSectionsData_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/sections"))
                .andExpect(status().isOk());
    }

    @Test
    void getTopLevelSectionInfo_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/sections/info"))
                .andExpect(status().isOk());
    }

    @Test
    void getSectionInfo_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/sections/info/100003"))
                .andExpect(status().isOk());
    }

    @Test
    void getHierarchicalStructure_isOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/sections/hierarchy"))
                .andExpect(status().isOk());
    }
}