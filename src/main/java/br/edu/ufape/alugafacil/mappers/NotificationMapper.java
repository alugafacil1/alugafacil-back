package br.edu.ufape.alugafacil.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.NotificationResponse;
import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "title", constant = "Novo Imóvel!") 
    @Mapping(target = "user", ignore = true)     
    @Mapping(target = "imageUrl", ignore = true) 
    @Mapping(target = "message", expression = "java(\"Um imóvel compatível com seu alerta '\" + request.getAlertName() + \"' foi listado.\")")
    ListingNotification toEntity(ListingNotificationRequest request);

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "title", constant = "Nova Mensagem")
    @Mapping(target = "user", ignore = true)      
    @Mapping(target = "imageUrl", ignore = true) 
    @Mapping(target = "message", expression = "java(\"Você recebeu uma mensagem de \" + request.getSenderName())")
    MessageNotification toEntity(MessageNotificationRequest request);




    @Mapping(source = "notificationId", target = "id")          
    @Mapping(source = "message", target = "body")   
    @Mapping(target = "type", constant = "LISTING") 
    ListingNotificationResponse toResponse(ListingNotification entity);

    @Mapping(source = "notificationId", target = "id")
    @Mapping(source = "message", target = "body")
    @Mapping(target = "type", constant = "MESSAGE")
    MessageNotificationResponse toResponse(MessageNotification entity);

    
    default NotificationResponse toResponse(Notification entity) {
        if (entity instanceof ListingNotification) {
            return toResponse((ListingNotification) entity);
        } else if (entity instanceof MessageNotification) {
            return toResponse((MessageNotification) entity);
        }
        return null; 
    }
}