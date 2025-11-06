package utils;

import com.google.gson.*;
import exception.ExceptionHandler;
import object.brick.Brick;
import object.movable.powerup.PowerUp;
import screen.pausescreen.PauseScreen;

import java.io.*;

/**
 * Utility class providing helper methods for saving, loading,
 * and validating JSON files using Gson serialization.
 * <p>
 * This class supports polymorphic serialization and deserialization
 * for {@link Brick}, {@link PowerUp}, and {@link PauseScreen} objects
 * through a custom {@link PolymorphicAdapter}.
 * </p>
 */
public class JsonLoaderUtils {

    /** Default path to the main game configuration JSON file. */
    public static String gameConfigPath = "assets/json/GameConfig.json";

    /** Shared Gson instance configured for polymorphic (type-safe) serialization. */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Brick.class, new PolymorphicAdapter<>())
            .registerTypeAdapter(PowerUp.class, new PolymorphicAdapter<>())
            .registerTypeAdapter(PauseScreen.class, new PolymorphicAdapter<>())
            .setPrettyPrinting()
            .create();

    /**
     * Saves a Java object to a JSON file.
     *
     * @param path the file path where JSON data should be written
     * @param data the object to serialize and save
     */
    public static void saveToJson(String path, Object data) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    /**
     * Loads a Java object from a JSON file.
     *
     * @param path  the path of the JSON file to read
     * @param clazz the target class type to deserialize into
     * @param <T>   the generic type parameter of the target class
     * @return the deserialized object, or {@code null} if an error occurs
     */
    public static <T> T loadFromJson(String path, Class<T> clazz) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }

    /**
     * Clears all content from a JSON file by overwriting it with an empty object "{}".
     *
     * @param path the path of the JSON file to clear
     */
    public static void clearJsonFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("{}");
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    /**
     * Checks if a given JSON file exists and contains valid non-empty JSON data.
     * <ul>
     *     <li>Returns {@code false} if the file does not exist or is empty.</li>
     *     <li>Returns {@code false} if the JSON structure is {@code null} or empty.</li>
     *     <li>Returns {@code true} otherwise.</li>
     * </ul>
     *
     * @param filePath the path to the JSON file
     * @return {@code true} if the JSON file contains valid data, otherwise {@code false}
     */
    public static boolean isJsonDataAvailable(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return false;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (element == null || element.isJsonNull()) {
                return false;
            }

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                return !obj.isEmpty();
            }

            if (element.isJsonArray()) {
                return !element.getAsJsonArray().isEmpty();
            }

            return true;

        } catch (Exception e) {
            ExceptionHandler.handle(e);
            return false;
        }
    }

}
