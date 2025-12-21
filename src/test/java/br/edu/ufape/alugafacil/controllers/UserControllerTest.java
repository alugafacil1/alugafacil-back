package br.edu.ufape.alugafacil.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufape.alugafacil.dtos.notifications.FcmTokenRequest;
import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldCreateUserAndReturn200() throws Exception {
        UserRequest request = new UserRequest(
                "Maria Silva",
                "maria@email.com",
                null,
                "12345678900",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                null,
                null,
                null
        );

        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "Maria Silva",
                "maria@email.com",
                null,
                "12345678900",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                List.of(),
                List.of()
        );

        when(userService.saveUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    @WithMockUser
    void shouldReturnAllUsers() throws Exception {

        UserResponse user = new UserResponse(
                UUID.randomUUID(),
                "João",
                "joao@email.com",
                null,
                "98765432100",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                List.of(),
                List.of()
        );


        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("João"));
    }

    @Test
    @WithMockUser
    void shouldReturnUserById() throws Exception {
        UUID id = UUID.randomUUID();

        UserResponse response = new UserResponse(
                id,
                "Ana",
                "ana@email.com",
                null,
                "11122233344",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                List.of(),
                List.of()
        );



        when(userService.getUserById(id)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana"));
    }

    @Test
    @WithMockUser
    void shouldUpdateUser() throws Exception {
        UUID id = UUID.randomUUID();

        UserRequest request = new UserRequest(
                "Maria Silva",
                "maria@email.com",
                null,
                "12345678900",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                null,
                null,
                null
        );

        UserResponse response = new UserResponse(
                id,
                "Maria Atualizada",
                "maria10@email.com",
                null,
                "12345678900",
                null,
                "senha12343",
                "81999999999",
                UserType.TENANT,
                null,
                List.of(),
                List.of()
        );

        when(userService.updateUser(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Atualizada"));
    }

    @Test
    @WithMockUser
    void shouldDeleteUserAndReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldUpdateFcmTokenAndReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        FcmTokenRequest request = new FcmTokenRequest();
        request.setToken("fake-fcm-token");

        doNothing().when(userService).updateFcmToken(eq(id), any());

        mockMvc.perform(patch("/api/users/{id}/fcm-token", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
