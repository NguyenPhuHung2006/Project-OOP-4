package utils;

import com.google.gson.Gson;
import exception.ExceptionHandler;
import exception.ResourceLoadException;
import main.GameContext;
import object.*;

import java.io.FileReader;
import java.io.IOException;

public class LevelLoaderUtils {

    private LevelLoaderUtils() {
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
