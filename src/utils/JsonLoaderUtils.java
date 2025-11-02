package utils;

import com.google.gson.*;
import exception.ExceptionHandler;
import object.brick.Brick;
import object.movable.powerup.PowerUp;

import java.io.*;

public class JsonLoaderUtils {

    public static String gameConfigPath = "assets/json/GameConfig.json";
    public static String playerStatusDataPath = "assets/json/PlayerStatus.json";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Brick.class, new PolymorphicAdapter<>())
            .registerTypeAdapter(PowerUp.class, new PolymorphicAdapter<>())
            .setPrettyPrinting()
            .create();

    public static void saveToJson(String path, Object data) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public static <T> T loadFromJson(String path, Class<T> clazz) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }

    public static void clearJsonFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("{}");
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

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
