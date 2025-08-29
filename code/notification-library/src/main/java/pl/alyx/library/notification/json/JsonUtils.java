package pl.alyx.library.notification.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import pl.alyx.library.notification.exception.JsonParseException;
import pl.alyx.library.notification.json.serializer.AttachmentSerializer;
import pl.alyx.library.notification.json.serializer.InstantSerializer;
import pl.alyx.library.notification.json.serializer.NotificationSerializer;
import pl.alyx.library.notification.model.Attachment;
import pl.alyx.library.notification.model.Notification;

import java.time.Instant;

public class JsonUtils {

    private static final GsonBuilder BASE_BUILDER = new GsonBuilder()
            .registerTypeAdapter(Notification.class, new NotificationSerializer())
            .registerTypeAdapter(Attachment.class, new AttachmentSerializer())
            .registerTypeAdapter(Instant.class, new InstantSerializer());

    private static final Gson GSON_COMPACT = BASE_BUILDER.create();
    private static final Gson GSON_PRETTY = BASE_BUILDER.setPrettyPrinting().create();

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return GSON_COMPACT.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException("Failed to parse JSON", e);
        }
    }

    public static String toJson(Object obj) {
        return GSON_COMPACT.toJson(obj);
    }

    public static String toJson(Object obj, boolean pretty) {
        return pretty ? GSON_PRETTY.toJson(obj) : GSON_COMPACT.toJson(obj);
    }

}
