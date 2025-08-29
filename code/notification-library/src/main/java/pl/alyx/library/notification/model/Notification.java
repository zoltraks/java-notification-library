package pl.alyx.library.notification.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Notification {

    private final String id;
    private final Instant time;
    private final String type; // "EMAIL", "SMS", "VOICE"
    private final String recipient; // "filip@local.com", "+48 12343242", "fgolewski:DXX"
    private final String subject;
    private final String message;
    private final List<Attachment> attachments;
    private final List<String> cc;
    private final List<String> bcc;
    private final String format;
    private final String priority;

    private Notification(String id, Instant time, String type, String recipient, String subject, String message, List<Attachment> attachments, List<String> cc, List<String> bcc, String format, String priority) {
        this.id = Objects.requireNonNull(id);
        this.time = time == null ? null : time.truncatedTo(ChronoUnit.MILLIS);
        this.type = type == null || type.trim().isEmpty() ? null : type.trim().toUpperCase();
        this.recipient = recipient;
        this.subject = subject;
        this.message = Objects.requireNonNull(message);
        this.attachments = attachments == null || attachments.isEmpty() ? null : Collections.unmodifiableList(new ArrayList<>(attachments));
        this.cc = cc == null || cc.isEmpty() ? null : Collections.unmodifiableList(new ArrayList<>(cc));
        this.bcc = bcc == null || bcc.isEmpty() ? null : Collections.unmodifiableList(new ArrayList<>(bcc));
        this.format = format == null || format.trim().isEmpty() ? null : format.trim().toUpperCase();
        this.priority = priority == null || priority.trim().isEmpty() ? null : priority.trim().toUpperCase();
    }

    public String getId() {
        return id;
    }

    public Instant getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getFormat() {
        return format;
    }

    public String getPriority() {
        return priority;
    }

    public static class Builder {

        private String id;
        private Instant time;
        private String type;
        private String recipient;
        private String subject;
        private String message;
        private List<String> cc;
        private List<String> bcc;
        private List<Attachment> attachments;
        private String format;
        private String priority;

        public Builder() {
            this.id = UUID.randomUUID().toString();
            this.time = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        }

        public Notification build() {
            Notification notification = new Notification(
                    id,
                    time,
                    type,
                    recipient,
                    subject,
                    message,
                    attachments,
                    cc,
                    bcc,
                    format,
                    priority
            );
            return notification;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder time(Instant time) {
            this.time = time;
            return this;
        }

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder attachments(List<Attachment> attachments) {
            this.attachments = attachments == null || attachments.isEmpty() ? null : new ArrayList<>(attachments);
            return this;
        }

        public Builder cc(List<String> cc) {
            this.cc = cc == null || cc.isEmpty() ? null : new ArrayList<>(cc);
            return this;
        }

        public Builder bcc(List<String> bcc) {
            this.bcc = bcc == null || bcc.isEmpty() ? null : new ArrayList<>(bcc);
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

    }
    
}
