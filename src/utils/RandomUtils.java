package utils;

import java.util.Random;

public final class RandomUtils {
    private static final Random random = new Random();

    private RandomUtils() {
    }

    public static int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static float nextFloat(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + (max - min) * random.nextFloat();
    }

    public static boolean nextBoolean() {
        return random.nextBoolean();
    }
}
