package br.edu.ufape.alugafacil.integration;

import br.edu.ufape.alugafacil.controllers.UserSearchPreferenceController;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.services.interfaces.IUserPreferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSearchPreferenceController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserPreferenceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserPreferenceService userPreferenceService;

    @Test
    @DisplayName("Deve criar preferência de busca com sucesso")
    void shouldCreateUserPreference() throws Exception {

        UserPreferenceRequest request = new UserPreferenceRequest(
                "Busca Recife",
                300000,
                2,
                1,
                true,
                false,
                "Recife",
                "Boa Viagem",
                5000,
                1,
                null,
                null,
                null
        );

        UserPreferenceResponse response = new UserPreferenceResponse(
                UUID.randomUUID(),
                "Busca Recife",
                300000,
                2,
                1,
                true,
                false,
                "Recife",
                "Boa Viagem",
                5000,
                1,
                null,
                null
        );

        when(userPreferenceService.save(any(UserPreferenceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Busca Recife"))
                .andExpect(jsonPath("$.city").value("Recife"));
    }
}
