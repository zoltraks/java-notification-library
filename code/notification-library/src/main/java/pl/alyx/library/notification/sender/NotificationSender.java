package pl.alyx.library.notification.sender;

import pl.alyx.library.notification.exception.PermanentSendException;
import pl.alyx.library.notification.exception.RetryableSendException;
import pl.alyx.library.notification.model.Notification;

public interface NotificationSender {

    void send(Notification notification) throws PermanentSendException, RetryableSendException;

}
