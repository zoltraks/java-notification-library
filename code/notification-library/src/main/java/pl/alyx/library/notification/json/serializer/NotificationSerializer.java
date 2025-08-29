package pl.alyx.library.notification.json.serializer;

import com.google.gson.*;
import pl.alyx.library.notification.model.Attachment;
import pl.alyx.library.notification.model.Notification;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NotificationSerializer implements JsonSerializer<Notification>, JsonDeserializer<Notification> {

    @Override
    public JsonElement serialize(Notification notification, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", notification.getId());

        if (notification.getTime() != null) {
            jsonObject.add("time", context.serialize(notification.getTime()));
        }

        if (notification.getType() != null) {
            jsonObject.addProperty("type", notification.getType());
        }

        if (notification.getRecipient() != null) {
            jsonObject.addProperty("recipient", notification.getRecipient());
        }

        if (notification.getSubject() != null) {
            jsonObject.addProperty("subject", notification.getSubject());
        }

        jsonObject.addProperty("message", notification.getMessage());

        if (notification.getAttachments() != null) {
            JsonArray attachmentsArray = new JsonArray();
            for (Attachment attachment : notification.getAttachments()) {
                attachmentsArray.add(context.serialize(attachment));
            }
            jsonObject.add("attachments", attachmentsArray);
        }

        if (notification.getCc() != null) {
            JsonArray ccArray = new JsonArray();
            for (String cc : notification.getCc()) {
                ccArray.add(cc);
            }
            jsonObject.add("cc", ccArray);
        }

        if (notification.getBcc() != null) {
            JsonArray bccArray = new JsonArray();
            for (String bcc : notification.getBcc()) {
                bccArray.add(bcc);
            }
            jsonObject.add("bcc", bccArray);
        }

        if (notification.getFormat() != null) {
            jsonObject.addProperty("format", notification.getFormat());
        }

        if (notification.getPriority() != null) {
            jsonObject.addProperty("priority", notification.getPriority());
        }

        return jsonObject;
    }

    @Override
    public Notification deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Notification.Builder builder = new Notification.Builder();

        if (jsonObject.has("id")) {
            builder.id(jsonObject.get("id").getAsString());
        }

        if (jsonObject.has("time") && !jsonObject.get("time").isJsonNull()) {
            builder.time(context.deserialize(jsonObject.get("time"), Instant.class));
        }

        if (jsonObject.has("type") && !jsonObject.get("type").isJsonNull()) {
            builder.type(jsonObject.get("type").getAsString());
        }

        if (jsonObject.has("recipient") && !jsonObject.get("recipient").isJsonNull()) {
            builder.recipient(jsonObject.get("recipient").getAsString());
        }

        if (jsonObject.has("subject") && !jsonObject.get("subject").isJsonNull()) {
            builder.subject(jsonObject.get("subject").getAsString());
        }

        if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
            builder.message(jsonObject.get("message").getAsString());
        }

        if (jsonObject.has("attachments") && !jsonObject.get("attachments").isJsonNull()) {
            JsonArray attachmentsArray = jsonObject.getAsJsonArray("attachments");
            List<Attachment> attachments = new ArrayList<>();
            for (JsonElement element : attachmentsArray) {
                attachments.add(context.deserialize(element, Attachment.class));
            }
            builder.attachments(attachments);
        }

        if (jsonObject.has("cc") && !jsonObject.get("cc").isJsonNull()) {
            JsonArray ccArray = jsonObject.getAsJsonArray("cc");
            List<String> ccList = new ArrayList<>();
            for (JsonElement element : ccArray) {
                ccList.add(element.getAsString());
            }
            builder.cc(ccList);
        }

        if (jsonObject.has("bcc") && !jsonObject.get("bcc").isJsonNull()) {
            JsonArray bccArray = jsonObject.getAsJsonArray("bcc");
            List<String> bccList = new ArrayList<>();
            for (JsonElement element : bccArray) {
                bccList.add(element.getAsString());
            }
            builder.bcc(bccList);
        }

        if (jsonObject.has("format") && !jsonObject.get("format").isJsonNull()) {
            builder.format(jsonObject.get("format").getAsString());
        }

        if (jsonObject.has("priority") && !jsonObject.get("priority").isJsonNull()) {
            builder.priority(jsonObject.get("priority").getAsString());
        }

        return builder.build();
    }

}