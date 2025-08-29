package pl.alyx.library.notification.sender.impl;

import pl.alyx.library.notification.exception.PermanentSendException;
import pl.alyx.library.notification.exception.RetryableSendException;
import pl.alyx.library.notification.model.Attachment;
import pl.alyx.library.notification.model.Notification;
import pl.alyx.library.notification.sender.NotificationSender;
import pl.alyx.library.notification.sender.config.EmailSenderConfig;
import pl.alyx.library.notification.sender.config.MailServerConfig;
import pl.alyx.library.notification.sender.config.MailServerSecurity;
import pl.alyx.library.notification.utils.HtmlUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.mail.PasswordAuthentication;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Properties;

public class EmailSender implements NotificationSender {

    private final EmailSenderConfig config;

    public EmailSender(EmailSenderConfig config) {
        this.config = config;
    }

    @Override
    public void send(Notification notification) throws PermanentSendException, RetryableSendException {
        try {
            validate(notification);

            Properties properties = createProperties();
            Authenticator authenticator = createAuthenticator();
            Session session = Session.getInstance(properties, authenticator);

            // Create MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Set sender
            String fromMail = config.getFromMail();
            String fromName = config.getFromName();
            if (fromName != null && !fromName.trim().isEmpty()) {
                message.setFrom(new InternetAddress(fromMail, fromName));
            } else {
                message.setFrom(new InternetAddress(fromMail));
            }

            // Set recipients
            if (notification.getRecipient() != null && !notification.getRecipient().isEmpty()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(notification.getRecipient()));
            }

            if (notification.getCc() != null && !notification.getCc().isEmpty()) {
                for (String cc : notification.getCc()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
                }
            }

            if (notification.getBcc() != null && !notification.getBcc().isEmpty()) {
                for (String bcc : notification.getBcc()) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
                }
            }

            if (notification.getSubject() != null) {
                message.setSubject(notification.getSubject(), "UTF-8");
            }

            if (config.getReplyTo() != null) {
                message.setReplyTo(new Address[]{new InternetAddress(config.getReplyTo())});
            }

            Multipart content = createContent(notification);
            message.setContent(content);

            message.setSentDate(new java.util.Date());

            Transport.send(message);

        } catch (AddressException e) {
            throw new PermanentSendException("Invalid email address format", e);
        } catch (FileNotFoundException e) {
            throw new PermanentSendException("Attachment file not found: " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new PermanentSendException("Access denied to attachment file: " + e.getMessage(), e);
        } catch (MessagingException e) {
            handleMessagingException(e);
        } catch (IOException e) {
            handleIOException(e);
        } catch (IllegalArgumentException e) {
            throw new PermanentSendException("Invalid notification configuration: " + e.getMessage(), e);
        }
    }

    private void validate(Notification notification) throws PermanentSendException {
        if (notification.getRecipient() == null || notification.getRecipient().trim().isEmpty()) {
            throw new PermanentSendException("Recipient email address is required");
        }

        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new PermanentSendException("Message content is required");
        }

        try {
            new InternetAddress(notification.getRecipient(), true);
        } catch (AddressException e) {
            throw new PermanentSendException("Invalid recipient email address: " + notification.getRecipient(), e);
        }

        if (notification.getCc() != null) {
            for (String cc : notification.getCc()) {
                try {
                    new InternetAddress(cc, true);
                } catch (AddressException e) {
                    throw new PermanentSendException("Invalid CC email address: " + cc, e);
                }
            }
        }

        if (notification.getBcc() != null) {
            for (String bcc : notification.getBcc()) {
                try {
                    new InternetAddress(bcc, true);
                } catch (AddressException e) {
                    throw new PermanentSendException("Invalid BCC email address: " + bcc, e);
                }
            }
        }
    }

    private void handleMessagingException(MessagingException e) throws RetryableSendException, PermanentSendException {
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

        if (e instanceof AuthenticationFailedException) {
            throw new PermanentSendException("SMTP authentication failed", e);
        }

        if (message.contains("timeout") ||
                message.contains("connection refused") ||
                message.contains("connection reset") ||
                message.contains("network is unreachable") ||
                message.contains("temporary failure") ||
                message.contains("try again later") ||
                message.contains("service unavailable") ||
                message.contains("server busy")) {
            throw new RetryableSendException("Temporary server issue: " + e.getMessage(), e);
        }

        if (message.contains("invalid recipient") ||
                message.contains("user unknown") ||
                message.contains("mailbox unavailable") ||
                message.contains("permanent failure") ||
                message.contains("message too large") ||
                message.contains("quota exceeded") ||
                message.contains("relay access denied")) {
            throw new PermanentSendException("Permanent delivery failure: " + e.getMessage(), e);
        }

        throw new RetryableSendException("SMTP communication error: " + e.getMessage(), e);
    }

    private void handleIOException(IOException e) throws RetryableSendException, PermanentSendException {
        if (e instanceof ConnectException ||
                e instanceof SocketTimeoutException ||
                e instanceof UnknownHostException) {
            throw new RetryableSendException("Network connection error: " + e.getMessage(), e);
        }

        if (e instanceof FileNotFoundException) {
            throw new PermanentSendException("Attachment file not found: " + e.getMessage(), e);
        }

        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        if (message.contains("permission denied") ||
                message.contains("access denied") ||
                message.contains("file not found")) {
            throw new PermanentSendException("File access error: " + e.getMessage(), e);
        }

        throw new RetryableSendException("IO error during email sending: " + e.getMessage(), e);
    }

    private Authenticator createAuthenticator() {
        MailServerConfig server = config.getServer();
        if (server.getUser() == null || server.getPassword() == null) {
            return null;
        } else {
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            server.getUser(),
                            server.getPassword()
                    );
                }
            };
        }
    }

    private Properties createProperties() {
        MailServerConfig server = config.getServer();
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", server.getHost());
        properties.put("mail.smtp.port", server.getPort());
        if (server.getUser() != null) {
            properties.put("mail.smtp.user", server.getUser());
        }
        if (server.getUser() != null && server.getPassword() != null) {
            properties.put("mail.smtp.auth", "true");
        }
        if (server.getSecurity() == MailServerSecurity.SSL) {
            properties.put("mail.smtp.ssl.enable", "true");
            if (server.getInsecure() == false) {
                properties.put("mail.smtp.ssl.checkserveridentity", "true");
            }
            if (server.getTrust() == null) {
                if (server.getInsecure() == true) {
                    properties.put("mail.smtp.ssl.trust", "*");
                }
            } else {
                properties.put("mail.smtp.ssl.trust", server.getTrust());
            }
        }
        if (server.getSecurity() == MailServerSecurity.TLS) {
            properties.put("mail.smtp.starttls.enable", "true");
            if (server.getInsecure() == false) {
                properties.put("mail.smtp.starttls.required", "true");
            }
        }
        int timeout = server.getTimeout();
        if (timeout == 0) {
            timeout = 30;
        }
        int connectionTimeout = server.getConnectionTimeout();
        if (connectionTimeout == 0) {
            connectionTimeout = timeout;
        }
        if (connectionTimeout > 0) {
            properties.put("mail.smtp.connectiontimeout", 1000 * connectionTimeout + "");
        } else if (connectionTimeout < 0) {
            properties.put("mail.smtp.connectiontimeout", "-1");
        }
        if (timeout > 0) {
            properties.put("mail.smtp.timeout", 1000 * timeout + "");
        } else if (timeout < 0) {
            properties.put("mail.smtp.timeout", "-1");
        }
        return properties;
    }

    private Multipart createContent(Notification notification) throws MessagingException, IOException {
        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(HtmlUtils.makeText(notification.getMessage()), "text/plain; charset=utf-8");

        BodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(HtmlUtils.makeHtml(notification.getMessage()), "text/html; charset=utf-8");

        Multipart multipartAlternative = new MimeMultipart("alternative");
        multipartAlternative.addBodyPart(textBodyPart);
        multipartAlternative.addBodyPart(htmlBodyPart);

        BodyPart messagePart = new MimeBodyPart();
        messagePart.setContent(multipartAlternative);

        Multipart content = new MimeMultipart("related");
        content.addBodyPart(messagePart);

        if (notification.getAttachments() != null) {
            for (Attachment attachment : notification.getAttachments()) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setContentID(attachment.getContentID());
                if (attachment.getData() != null) {
                    attachmentBodyPart.setContent(attachment.getData(),
                            attachment.getMime() != null ? attachment.getMime() : "application/octet-stream");

                    if (attachment.getFile() != null && !attachment.getFile().trim().isEmpty()) {
                        attachmentBodyPart.setFileName(attachment.getFile());
                        attachmentBodyPart.setDisposition(Part.ATTACHMENT);
                    }
                } else if (attachment.getFile() != null && !attachment.getFile().isEmpty()) {
                    File file = new File(attachment.getFile());
                    attachmentBodyPart.attachFile(file);
                    attachmentBodyPart.setFileName(file.getName());
                } else {
                    continue;
                }
                content.addBodyPart(attachmentBodyPart);
            }
        }

        return content;
    }

}
