package pl.alyx.library.notification.service;

import pl.alyx.library.notification.model.Notification;

public interface NotificationSenderService {

    void send(Notification notification);

}
