package pl.alyx.library.notification.exception;

public abstract class SendException extends Exception {

    protected SendException(String message) {
        super(message);
    }

    protected SendException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract boolean isRetryable();

}
