package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ExceptionHandler;
import exception.ResourceLoadException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonLoaderUtils {

    private JsonLoaderUtils() {
    }

    public static String gameConfigPath = "assets/json/GameConfig.json";
    public static String playerStatusDataPath = "assets/json/PlayerStatus.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public static <T> T loadFromJson(String path, Class<T> type) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }

    public static <T> void saveToJson(String path, T data) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
        }
    }

}
