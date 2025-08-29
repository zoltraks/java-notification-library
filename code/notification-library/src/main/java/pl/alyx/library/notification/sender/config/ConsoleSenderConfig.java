package pl.alyx.library.notification.sender.config;

public class ConsoleSenderConfig {

    private final Format format;
    private final boolean pretty;

    private ConsoleSenderConfig(Format format, boolean pretty) {
        this.format = format;
        this.pretty = pretty;
    }

    public Format getFormat() {
        return format;
    }

    public boolean getPretty() {
        return pretty;
    }

    public enum Format {
        NONE,
        TEXT,
        JSON,
    }

    public static class Builder {

        private Format format = Format.NONE;
        private boolean pretty = false;

        public ConsoleSenderConfig build() {
            return new ConsoleSenderConfig(
                    format,
                    pretty
            );
        }

        public Builder format(Format format) {
            this.format = format;
            return this;
        }

        public Builder pretty(boolean pretty) {
            this.pretty = pretty;
            return this;
        }

    }

}
