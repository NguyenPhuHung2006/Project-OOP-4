package utils;

import com.google.gson.*;
import java.lang.reflect.Type;

public class PolymorphicAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
        obj.addProperty("className", src.getClass().getName());
        return obj;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        JsonObject obj = json.getAsJsonObject();
        JsonElement classNameEl = obj.get("className");
        if (classNameEl == null) {
            return null;
        }

        try {
            Class<?> clazz = Class.forName(classNameEl.getAsString());
            return context.deserialize(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown class: " + classNameEl.getAsString(), e);
        }
    }
}
