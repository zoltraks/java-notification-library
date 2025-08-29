package pl.alyx.library.notification.exception;

public class PermanentSendException extends SendException {

    public PermanentSendException(String message) {
        super(message);
    }

    public PermanentSendException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public boolean isRetryable() {
        return false;
    }

}
