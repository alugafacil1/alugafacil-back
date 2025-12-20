package br.edu.ufape.alugafacil.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufape.alugafacil.dtos.PlanRequestDTO;
import br.edu.ufape.alugafacil.dtos.PlanResponseDTO;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.services.PlanService;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // <--- Importante

@WebMvcTest(PlanController.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanService planService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreatePlanAndReturn201() throws Exception {
        PlanRequestDTO request = new PlanRequestDTO("Plano Silver", 1500, false, 5, 2, false, false, UserType.TENANT);
        
        Plan fakePlan = new Plan();
        fakePlan.setPlanId(UUID.randomUUID());
        fakePlan.setName("Plano Silver");
        PlanResponseDTO responseService = new PlanResponseDTO(fakePlan); 

        when(planService.createPlan(any())).thenReturn(responseService);

        mockMvc.perform(post("/api/plans")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Plano Silver"));
    }
}