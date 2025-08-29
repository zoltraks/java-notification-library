package pl.alyx.library.notification.exception;

public class RetryableSendException extends SendException {

    public RetryableSendException(String message) {
        super(message);
    }

    public RetryableSendException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public boolean isRetryable() {
        return true;
    }

}
