package br.edu.ufape.alugafacil.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListingNotificationResponse extends NotificationResponse {
    private String propertyId;
    private String alertName;

    public ListingNotificationResponse() {
        this.setType("LISTING");
    }
}