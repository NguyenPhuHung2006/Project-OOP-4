package utils;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * A custom Gson adapter that supports polymorphic (class-aware)
 * serialization and deserialization of objects.
 * <p>
 * During serialization, this adapter adds a special {@code className}
 * property to the JSON output, storing the fully-qualified class name
 * of the serialized object.
 * </p>
 * <p>
 * During deserialization, this class name is used to dynamically
 * reconstruct the correct object type.
 * </p>
 *
 * @param <T> the base type for polymorphic serialization
 */
public class PolymorphicAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * Serializes an object and includes its runtime class name
     * in the JSON under the field {@code className}.
     *
     * @param src        the source object to serialize
     * @param typeOfSrc  the declared type of the source object
     * @param context    the Gson serialization context
     * @return a {@link JsonElement} representing the serialized object,
     *         or {@link JsonNull#INSTANCE} if {@code src} is null
     */
    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
        obj.addProperty("className", src.getClass().getName());
        return obj;
    }

    /**
     * Deserializes a JSON element back into an object of the original class type.
     * <p>
     * The JSON must contain a {@code className} field specifying the fully-qualified
     * class name of the object to reconstruct.
     * </p>
     *
     * @param json     the JSON data to deserialize
     * @param typeOfT  the declared type of the destination
     * @param context  the Gson deserialization context
     * @return the reconstructed object, or {@code null} if the JSON is invalid
     * @throws JsonParseException if the class cannot be found or deserialization fails
     */
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
