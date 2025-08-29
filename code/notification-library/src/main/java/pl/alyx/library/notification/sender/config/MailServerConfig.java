package pl.alyx.library.notification.sender.config;

public class MailServerConfig {

    private final String host;
    private final int port;
    private final MailServerSecurity security;
    private final String user;
    private final String password;
    private final boolean insecure;
    private final String trust;
    private final int timeout;
    private final int connectionTimeout;

    private MailServerConfig(String host, int port, MailServerSecurity security, String user, String password, boolean insecure, String trust, int timeout, int connectionTimeout) {
        this.host = host;
        this.port = port;
        this.security = security;
        this.user = user;
        this.password = password;
        this.insecure = insecure;
        this.trust = trust;
        this.timeout = timeout;
        this.connectionTimeout = connectionTimeout;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public MailServerSecurity getSecurity() {
        return security;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean getInsecure() {
        return insecure;
    }

    public String getTrust() {
        return trust;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public static class Builder {

        private String host;
        private int port;
        private MailServerSecurity security;
        private String user;
        private String password;
        private boolean insecure;
        private String trust;
        private int timeout;
        private int connectionTimeout;

        public MailServerConfig build() {
            validate();
            return new MailServerConfig(
                    host,
                    port > 0 ? port : security.getDefaultPort(),
                    security,
                    user,
                    password,
                    insecure,
                    trust,
                    timeout,
                    connectionTimeout
            );
        }

        private void validate() {
            if (port > 0 && port > 65535) {
                throw new IllegalArgumentException("Port number must not exceed 65535");
            }
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder security(MailServerSecurity security) {
            this.security = security;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder insecure(boolean insecure) {
            this.insecure = insecure;
            return this;
        }

        public Builder trust(String trust) {
            this.trust = trust;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

    }

}
