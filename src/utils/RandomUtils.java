package utils;

import java.util.Random;

/**
 * The {@code RandomUtils} class provides a set of utility methods for generating
 * random values, including integers, floats, booleans, and random enum constants.
 * <p>
 * This class wraps a single instance of {@link Random} to ensure efficiency and
 * consistency across random number generations throughout the application.
 * </p>
 *
 * <p>
 * It is declared as {@code final} with a private constructor to prevent instantiation
 * and subclassing.
 * </p>
 */
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

    /**
     * Returns a random constant from the specified {@link Enum} type.
     *
     * @param <T>        the type of the enum
     * @param enumClass  the class object of the enum type
     * @return a randomly selected constant from the specified enum
     * @throws IllegalArgumentException if the enum class has no constants
     */
    public static <T extends Enum<?>> T nextEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        if (constants == null || constants.length == 0) {
            throw new IllegalArgumentException("Enum class has no constants: " + enumClass);
        }
        int index = random.nextInt(constants.length);
        return constants[index];
    }
}
