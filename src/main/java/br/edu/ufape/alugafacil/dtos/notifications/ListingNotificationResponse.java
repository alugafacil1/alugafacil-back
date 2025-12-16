package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListingNotificationResponse extends NotificationResponse {
    private UUID propertyId;
    private String alertName;

    public ListingNotificationResponse() {
        this.setType("LISTING");
    }
}