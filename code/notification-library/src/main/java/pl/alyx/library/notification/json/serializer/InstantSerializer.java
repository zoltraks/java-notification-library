package pl.alyx.library.notification.json.serializer;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(instant));
    }

    @Override
    public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Instant.from(FORMATTER.parse(json.getAsString()));
    }

}