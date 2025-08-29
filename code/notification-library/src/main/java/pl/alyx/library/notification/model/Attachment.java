package pl.alyx.library.notification.model;

import java.util.UUID;

public class Attachment {

    private final String file;
    private final String mime;
    private final byte[] data;

    private Attachment(String file, String mime, byte[] data) {
        this.file = file;
        this.mime = mime;
        this.data = data == null || data.length == 0 ? null : data.clone();
    }

    public String getFile() {
        return file;
    }

    public String getMime() {
        return mime;
    }

    public byte[] getData() {
        return data == null ? null : data.clone();
    }

    public String getContentID() {
        String contentID = file;
        if (contentID == null || contentID.trim().isEmpty()) {
            contentID = UUID.randomUUID().toString();
        } else {
            contentID = contentID.replaceAll("[\\\\/\\s]", "_");
        }
        return contentID;
    }

    public static class Builder {

        private String file;
        private String mime;
        private byte[] data;

        public Attachment build() {
            return new Attachment(file, mime, data);
        }

        public Builder file(String file) {
            this.file = file;
            return this;
        }

        public Builder mime(String mime) {
            this.mime = mime;
            return this;
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

    }

}
