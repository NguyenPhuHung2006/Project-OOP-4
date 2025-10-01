package utils;

import com.google.gson.Gson;
import exception.ResourceLoadException;
import object.LevelData;

import java.io.FileReader;
import java.io.IOException;

public class LevelLoaderUtils {
    public static LevelData load(String path) throws ResourceLoadException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, LevelData.class);
        } catch (IOException e) {
            throw new ResourceLoadException(path, e);
        }
    }
}
