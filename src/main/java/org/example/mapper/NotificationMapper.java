package org.example.mapper;


import org.example.model.dto.NotificationRequest;
import org.example.model.dto.NotificationResponse;
import org.example.model.entities.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse fromEntityToDto(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getNotificationDate(),
                notification.getPayment().getAmount()
        );
    }

    public Notification fromDtoToEntity(NotificationRequest dto) {
        Notification notification = new Notification();
        notification.setMessage(dto.message());
        notification.setNotificationDate(dto.notificationDate());
        //Payment e adaugat in service
        return notification;
    }
}
