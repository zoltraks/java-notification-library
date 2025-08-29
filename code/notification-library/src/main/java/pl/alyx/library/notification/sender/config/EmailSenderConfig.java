package pl.alyx.library.notification.sender.config;

public class EmailSenderConfig {

    private final MailServerConfig server;
    private final String fromMail;
    private final String fromName;
    private final String replyTo;

    private EmailSenderConfig(MailServerConfig server, String fromMail, String fromName, String replyTo) {
        this.server = server;
        this.fromMail = fromMail;
        this.fromName = fromName;
        this.replyTo = replyTo;
    }

    public MailServerConfig getServer() {
        return server;
    }

    public String getFromMail() {
        return fromMail;
    }

    public String getFromName() {
        return fromName;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public static class Builder {

        private MailServerConfig server;
        private String fromMail;
        private String fromName;
        private String replyTo;

        public EmailSenderConfig build() {
            return new EmailSenderConfig(
                    server,
                    fromMail,
                    fromName,
                    replyTo
            );
        }

        public Builder server(MailServerConfig server) {
            this.server = server;
            return this;
        }

        public Builder fromMail(String fromMail) {
            this.fromMail = fromMail;
            return this;
        }

        public Builder fromName(String fromName) {
            this.fromName = fromName;
            return this;
        }

        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

    }

}
