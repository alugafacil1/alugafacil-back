package br.edu.ufape.alugafacil.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.exceptions.UserPreferenceNotFound;
import br.edu.ufape.alugafacil.services.interfaces.IUserPreferenceService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserSearchPreferenceController.class)
class UserSearchPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserPreferenceService userPreferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar preferência com sucesso")
    @WithMockUser
    void shouldCreatePreference() throws Exception {
        UserPreferenceRequest request = new UserPreferenceRequest(
                "Preferência Teste",
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

        UserPreferenceResponse response = new UserPreferenceResponse(
                UUID.randomUUID(),
                "Preferência Teste",
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

        when(userPreferenceService.save(any())).thenReturn(response);

        mockMvc.perform(post("/api/preferences")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Preferência Teste"));
    }

    @Test
    @DisplayName("Deve listar preferências do usuário")
    @WithMockUser
    void shouldGetAllPreferences() throws Exception {
        UUID userId = UUID.randomUUID();

        UserPreferenceResponse response = new UserPreferenceResponse(
                UUID.randomUUID(),
                "Preferência 1",
                200000,
                2,
                1,
                false,
                true,
                "Recife",
                "Centro",
                3000,
                1,
                null,
                null
        );

        when(userPreferenceService.getAllPreference(userId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/preferences")
                        .param("idUser", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Preferência 1"));
    }

    @Test
    @DisplayName("Deve buscar preferência por ID")
    @WithMockUser
    void shouldGetPreferenceById() throws Exception {
        UUID preferenceId = UUID.randomUUID();

        UserPreferenceResponse response = new UserPreferenceResponse(
                preferenceId,
                "Preferência X",
                250000,
                3,
                2,
                true,
                true,
                "Recife",
                "Casa Forte",
                4000,
                2,
                null,
                null
        );

        when(userPreferenceService.getPreferenceById(preferenceId))
                .thenReturn(response);

        mockMvc.perform(get("/api/preferences/{id}", preferenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Preferência X"));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar preferência inexistente")
    @WithMockUser
    void shouldReturnBadRequestWhenPreferenceNotFound() throws Exception {
        UUID preferenceId = UUID.randomUUID();

        when(userPreferenceService.getPreferenceById(preferenceId))
                .thenThrow(new UserPreferenceNotFound());

        mockMvc.perform(get("/api/preferences/{id}", preferenceId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar preferência com sucesso")
    @WithMockUser
    void shouldUpdatePreference() throws Exception {
        UUID preferenceId = UUID.randomUUID();

        UserPreferenceRequest request = new UserPreferenceRequest(
                "Atualizada",
                350000,
                3,
                2,
                true,
                true,
                "Recife",
                "Pina",
                2000,
                2,
                null,
                null
        );

        UserPreferenceResponse response = new UserPreferenceResponse(
                preferenceId,
                "Atualizada",
                350000,
                3,
                2,
                true,
                true,
                "Recife",
                "Pina",
                2000,
                2,
                null,
                null
        );

        when(userPreferenceService.updatePreference(preferenceId, request))
                .thenReturn(response);

        mockMvc.perform(put("/api/preferences/{id}", preferenceId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizada"));
    }

    @Test
    @DisplayName("Deve deletar preferência com sucesso")
    @WithMockUser
    void shouldDeletePreference() throws Exception {
        UUID preferenceId = UUID.randomUUID();

        doNothing().when(userPreferenceService).deleteUser(preferenceId);

        mockMvc.perform(delete("/api/preferences/{id}", preferenceId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
