package pl.alyx.library.notification.service;

import pl.alyx.library.notification.exception.PermanentSendException;
import pl.alyx.library.notification.exception.RetryableSendException;
import pl.alyx.library.notification.model.Notification;
import pl.alyx.library.notification.repository.NotificationRepository;
import pl.alyx.library.notification.sender.NotificationSender;

import java.util.ArrayList;
import java.util.List;

public class DefaultNotificationSenderService implements NotificationSenderService, AutoCloseable {

    private final List<NotificationSender> senders;

    public DefaultNotificationSenderService(List<NotificationSender> senders) {
        this.senders = senders;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void send(Notification notification) {
        for (NotificationSender sender : this.senders) {
            try {
                sender.send(notification);
            } catch (PermanentSendException e) {
                throw new RuntimeException(e);
            } catch (RetryableSendException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Builder {

        private List<NotificationSender> senders = new ArrayList<>();

        public DefaultNotificationSenderService build() {
            return new DefaultNotificationSenderService(senders);
        }

        public Builder addRepository(NotificationRepository repository) {
            return this;
        }

        public Builder addSender(NotificationSender sender) {
            this.senders.add(sender);
            return this;
        }

    }

}
