package utils;

import com.google.gson.Gson;
import config.GameSave;
import exception.ExceptionHandler;
import exception.ResourceLoadException;

import java.io.FileReader;
import java.io.IOException;

public class JsonLoaderUtils {

    private JsonLoaderUtils() {
    }

    private static final Gson gson = new Gson();

    public static <T> T loadFromJson(String path, Class<T> type) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }

    public static void saveGameProgress(String path, GameSave saveData) {
        try (java.io.FileWriter writer = new java.io.FileWriter(path)) {
            gson.toJson(saveData, writer);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
        }
    }

}
