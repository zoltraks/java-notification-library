package pl.alyx.library.notification.json.serializer;

import com.google.gson.*;
import pl.alyx.library.notification.model.Attachment;

import java.lang.reflect.Type;
import java.util.Base64;

public class AttachmentSerializer implements JsonSerializer<Attachment>, JsonDeserializer<Attachment> {

    @Override
    public JsonElement serialize(Attachment attachment, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (attachment.getFile() != null) {
            jsonObject.addProperty("file", attachment.getFile());
        }

        if (attachment.getMime() != null) {
            jsonObject.addProperty("mime", attachment.getMime());
        }

        if (attachment.getData() != null) {
            String base64Data = Base64.getEncoder().encodeToString(attachment.getData());
            jsonObject.addProperty("data", base64Data);
        }

        return jsonObject;
    }

    @Override
    public Attachment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Attachment.Builder builder = new Attachment.Builder();

        if (jsonObject.has("file") && !jsonObject.get("file").isJsonNull()) {
            builder.file(jsonObject.get("file").getAsString());
        }

        if (jsonObject.has("mime") && !jsonObject.get("mime").isJsonNull()) {
            builder.mime(jsonObject.get("mime").getAsString());
        }

        if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
            String base64Data = jsonObject.get("data").getAsString();
            byte[] data = Base64.getDecoder().decode(base64Data);
            builder.data(data);
        }

        return builder.build();
    }

}