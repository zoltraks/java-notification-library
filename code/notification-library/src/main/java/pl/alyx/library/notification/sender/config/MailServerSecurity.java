package pl.alyx.library.notification.sender.config;

public enum MailServerSecurity {

    NONE("", 25),
    TLS("TLS", 587),
    SSL("SSL", 465);

    private final String value;
    private final int defaultPort;

    MailServerSecurity(String value, int defaultPort) {
        this.value = value;
        this.defaultPort = defaultPort;
    }

    public String getValue() {
        return value;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public static MailServerSecurity fromString(String value) {
        if (value == null) {
            return NONE;
        }

        String normalizedValue = value.trim().toUpperCase();

        switch (normalizedValue) {
            case "":
            case "NONE":
            case "NULL":
                return NONE;
            case "TLS":
            case "STARTTLS":
                return TLS;
            case "SSL":
            case "SMTPS":
                return SSL;
            default:
                throw new IllegalArgumentException("Unknown security type: " + value);
        }
    }

}
