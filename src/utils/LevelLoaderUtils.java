package utils;

import com.google.gson.Gson;
import object.LevelData;

import java.io.FileReader;
import java.io.IOException;

public class LevelLoaderUtils {
    public static LevelData load(String path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, LevelData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
