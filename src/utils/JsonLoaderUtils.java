package utils;

import com.google.gson.Gson;
import config.GameConfig;
import config.LevelData;
import exception.ExceptionHandler;
import exception.ResourceLoadException;

import java.io.FileReader;
import java.io.IOException;

public class JsonLoaderUtils {

    private JsonLoaderUtils() {
    }

    public static GameConfig loadConfigFromJson(String path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, GameConfig.class);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }

    public static LevelData loadLevelFromJson(String path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, LevelData.class);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }
}
