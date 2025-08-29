package pl.alyx.library.notification.repository.config;

public class FileRepositoryConfig {

    private final String path;

    private FileRepositoryConfig(String path) {
        this.path = path;
    }

    public static class Builder {

        private String path;

        public FileRepositoryConfig build() {
            return new FileRepositoryConfig(path);
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

    }

}
