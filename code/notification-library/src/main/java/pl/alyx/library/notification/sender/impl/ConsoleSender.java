package pl.alyx.library.notification.sender.impl;

import pl.alyx.library.notification.json.JsonUtils;
import pl.alyx.library.notification.sender.NotificationSender;
import pl.alyx.library.notification.sender.config.ConsoleSenderConfig;
import pl.alyx.library.notification.model.Notification;

public class ConsoleSender implements NotificationSender {

    private final ConsoleSenderConfig config;

    public ConsoleSender(ConsoleSenderConfig config) {
        this.config = config;
    }

    @Override
    public void send(Notification notification) {
        switch (config.getFormat()) {
            default:
                System.out.println(notification.toString());
                break;
            case JSON:
                System.out.println(JsonUtils.toJson(notification, config.getPretty()));
                break;
        }
    }

}
