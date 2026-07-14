package com.boosterhub.organization.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsActiveOrganizationsOrderedByName() throws Exception {
        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                // Repository returns active organizations ordered alphabetically by name.
                .andExpect(jsonPath("$[0].name").value("Canyon Cross Country Boosters"))
                .andExpect(jsonPath("$[0].schoolName").value("Canyon High School"))
                .andExpect(jsonPath("$[0].mascot").value("Cobras"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].name").value("Summit Valley Boys Soccer Boosters"))
                .andExpect(jsonPath("$[1].schoolName").value("Summit Valley High School"))
                .andExpect(jsonPath("$[1].mascot").value("Titans"))
                .andExpect(jsonPath("$[1].active").value(true))
                .andExpect(jsonPath("$[*].name", not(hasItem("Redwood Swim & Dive Boosters"))));
    }
}