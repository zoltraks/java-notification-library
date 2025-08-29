# Notification Java Library

This is Java 8 library for sending notifications through multiple channels (email, console) with support for attachments, multiple recipients, and flexible configuration.

## Features

- **Multiple notification channels**: Email (SMTP), Console output
- **Rich content support**: Plain text and HTML messages, file attachments
- **Flexible recipient management**: TO, CC, BCC recipients
- **Multiple output formats**: JSON and plain text console output
- **Robust error handling**: Distinguishes between retryable and permanent failures
- **Builder pattern**: Fluent API for easy configuration
- **Repository support**: File and console-based notification storage
- **JSON serialization**: Built-in support for notification serialization

## Dependencies

- Java 8+
- JavaMail API (javax.mail)
- Gson (JSON processing)
- JSoup (HTML processing)

## Quick start

### Basic e-mail notification

```java
import pl.alyx.library.notification.model.Notification;
import pl.alyx.library.notification.sender.impl.EmailSender;
import pl.alyx.library.notification.sender.config.EmailSenderConfig;
import pl.alyx.library.notification.sender.config.MailServerConfig;
import pl.alyx.library.notification.sender.config.MailServerSecurity;
import pl.alyx.library.notification.service.DefaultNotificationService;

// Configure email sender
NotificationSender emailSender = new EmailSender(new EmailSenderConfig.Builder()
    .server(new MailServerConfig.Builder()
        .host("smtp.example.com")
        .security(MailServerSecurity.TLS)
        .port(587)
        .user("username")
        .password("password")
        .build())
    .fromMail("noreply@example.com")
    .fromName("My Application")
    .build());

// Create notification service
NotificationService notificationService = new DefaultNotificationService.Builder()
    .addSender(emailSender)
    .build();

// Send notification
Notification notification = new Notification.Builder()
    .recipient("user@example.com")
    .subject("Hello World")
    .message("This is a test message")
    .build();

notificationService.send(notification);
```

### Complete example with multiple senders and attachments

```java
import pl.alyx.library.notification.model.Attachment;
import pl.alyx.library.notification.model.Notification;
import pl.alyx.library.notification.repository.impl.ConsoleRepository;
import pl.alyx.library.notification.repository.impl.FileRepository;
import pl.alyx.library.notification.repository.config.FileRepositoryConfig;
import pl.alyx.library.notification.sender.impl.ConsoleSender;
import pl.alyx.library.notification.sender.config.ConsoleSenderConfig;
import pl.alyx.library.notification.service.DefaultNotificationService;

import java.util.Arrays;

public class NotificationExample {
    public static void main(String[] args) {
        // Configure email sender
        NotificationSender emailSender = new EmailSender(new EmailSenderConfig.Builder()
            .server(new MailServerConfig.Builder()
                .host("localhost")
                .security(MailServerSecurity.NONE)
                .port(1025)
                .build())
            .fromMail("robot@localhost")
            .fromName("Robot")
            .build());

        // Create notification service with multiple senders and repositories
        NotificationSenderService notificationSenderService = new DefaultNotificationSenderService.Builder()
            .addRepository(new FileRepository(new FileRepositoryConfig.Builder()
                .path(".")
                .build()))
            .addSender(new ConsoleSender(new ConsoleSenderConfig.Builder()
                .format(ConsoleSenderConfig.Format.JSON)
                .build()))
            .addSender(emailSender)
            .build();

        // Create notification with attachments and multiple recipients
        Notification notification = new Notification.Builder()
            .recipient("filip@localhost")
            .cc(Arrays.asList("copy1@localhost", "copy2@localhost"))
            .subject("Test Message")
            .message("This is a test message with attachments")
            .attachments(Arrays.asList(
                new Attachment.Builder()
                    .data(new byte[]{1, 2, 3, 4, 5, 6, 7, 8})
                    .file("file.txt")
                    .build(),
                new Attachment.Builder()
                    .file("data.csv")
                    .mime("text/csv")
                    .data(new byte[]{1, 2, 3, 4})
                    .build()
            ))
            .build();

        // Send the notification
        notificationSenderService.send(notification);
    }
}
```

## Configuration

### Email sender configuration

Configure SMTP settings for e-mail delivery:

```java
EmailSenderConfig config = new EmailSenderConfig.Builder()
    .server(new MailServerConfig.Builder()
        .host("smtp.gmail.com")
        .port(587)
        .security(MailServerSecurity.fromString("TLS"))
        .user("your-email@gmail.com")
        .password("your-password")
        .timeout(30)
        .connectionTimeout(10)
        .build())
    .fromMail("sender@example.com")
    .fromName("Application Name")
    .replyTo("support@example.com")
    .build();
```

### Security options

- `MailServerSecurity.NONE`: No encryption (port 25)
- `MailServerSecurity.TLS`: STARTTLS encryption (port 587)
- `MailServerSecurity.SSL`: SSL/TLS encryption (port 465)

### Console sender configuration

Configure console output format:

```java
ConsoleSenderConfig config = new ConsoleSenderConfig.Builder()
    .format(ConsoleSenderConfig.Format.JSON) // or Format.TEXT, Format.NONE
    .build();
```

## Notification model

### Core properties

- `id`: Unique identifier (auto-generated UUID if not specified)
- `time`: Timestamp (defaults to current time)
- `type`: Notification type (e.g., "EMAIL", "SMS")
- `recipient`: Primary recipient address
- `subject`: Message subject
- `message`: Message content (supports HTML)
- `attachments`: List of file attachments
- `cc`: Carbon copy recipients
- `bcc`: Blind carbon copy recipients
- `format`: Message format specification
- `priority`: Message priority level

### Creating notifications

```java
Notification notification = new Notification.Builder()
    .id("custom-id")
    .type("EMAIL")
    .recipient("user@example.com")
    .cc(Arrays.asList("manager@example.com"))
    .bcc(Arrays.asList("archive@example.com"))
    .subject("Important Update")
    .message("Please review the attached document.")
    .format("HTML")
    .priority("HIGH")
    .attachments(Arrays.asList(
        new Attachment.Builder()
            .file("/path/to/document.pdf")
            .mime("application/pdf")
            .build()
    ))
    .build();
```

## Attachments

### File attachments

```java
Attachment fileAttachment = new Attachment.Builder()
    .file("/path/to/file.pdf")
    .mime("application/pdf")
    .build();
```

### Data attachments

```java
byte[] data = "file content".getBytes();
Attachment dataAttachment = new Attachment.Builder()
    .data(data)
    .file("filename.txt")
    .mime("text/plain")
    .build();
```

## Error handling

The library distinguishes between two types of errors:

### Permanent errors (`PermanentSendException`)
- Invalid email addresses
- Authentication failures
- File not found errors
- Configuration errors

### Retryable errors (`RetryableSendException`)
- Network connectivity issues
- Temporary server failures
- Timeout errors

```java
try {
    notificationService.send(notification);
} catch (PermanentSendException e) {
    // Log error, don't retry
    logger.error("Permanent failure: " + e.getMessage());
} catch (RetryableSendException e) {
    // Schedule for retry
    retryQueue.add(notification);
}
```

## JSON serialization

The library provides built-in JSON serialization support:

```java
import pl.alyx.library.notification.json.JsonUtils;

// Serialize to JSON
String json = JsonUtils.toJson(notification);
String prettyJson = JsonUtils.toJson(notification, true);

// Deserialize from JSON
Notification notification = JsonUtils.fromJson(json, Notification.class);
```

## Repository support

Store notifications using different repository implementations:

### File repository

```java
FileRepository fileRepo = new FileRepository(new FileRepositoryConfig.Builder()
    .path("/path/to/storage")
    .build());
```

### Console repository

```java
ConsoleRepository consoleRepo = new ConsoleRepository();
```

## Architecture

### Core components

- **Notification**: Notication model
- **Attachment**: Attachment model
- **NotificationSenderService**: Main service interface for sending notifications
- **NotificationSender**: Interface for different delivery channels
- **NotificationRepository**: Interface for storing notifications

### Implementations

- **EmailSender**: SMTP email delivery
- **ConsoleSender**: Console output
- **FileRepository**: File-based storage
- **ConsoleRepository**: Console-based storage

## Building the project

```bash
mvn clean compile
mvn test
mvn package
```

## License

This project is licensed under the terms specified in the project configuration.
