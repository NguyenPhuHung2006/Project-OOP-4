package utils;

import com.google.gson.Gson;
import config.GameConfig;
import config.GameSave;
import config.LevelData;
import exception.ExceptionHandler;
import exception.ResourceLoadException;

import java.io.FileReader;
import java.io.IOException;

public class JsonLoaderUtils {

    private JsonLoaderUtils() {
    }

    private static final Gson gson = new Gson();

    public static GameConfig loadConfigFromJson(String path) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, GameConfig.class);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }

    public static LevelData loadLevelFromJson(String path) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, LevelData.class);
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

    public static GameSave loadGameProgress(String path) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, GameSave.class);
        } catch (IOException e) {
            ExceptionHandler.handle(new ResourceLoadException(path, e));
            return null;
        }
    }

}
