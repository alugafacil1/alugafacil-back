package br.edu.ufape.alugafacil.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationResponse;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private INotificationService notificationService;

    @Test
    void shouldReturn201WhenCreatingListingNotification() throws Exception {
        // Arrange
        ListingNotificationRequest request = new ListingNotificationRequest();
        request.setPropertyId(UUID.randomUUID());
        request.setAlertName("Teste Alerta");
        request.setTargetToken("token-xyz");

        ListingNotificationResponse response = new ListingNotificationResponse();
        response.setAlertName("Teste Alerta");
        response.setType("LISTING");

        when(notificationService.createListingNotification(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/notifications/listing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alertName").value("Teste Alerta"))
                .andExpect(jsonPath("$.type").value("LISTING"));
    }
    
    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        // Arrange: Request vazio (sem alertName que é @NotBlank)
        ListingNotificationRequest request = new ListingNotificationRequest();

        // Act & Assert
        mockMvc.perform(post("/api/notifications/listing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(422)); // Espera erro de validação
    }
}