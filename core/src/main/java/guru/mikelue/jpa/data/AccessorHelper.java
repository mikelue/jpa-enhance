package guru.mikelue.jpa.data;

/**
 * This utility provides helper methods for implementing accessor methods.
 */
public class AccessorHelper {
    private AccessorHelper() {}

    /**
     * Convert the value of {@link Enum} to {@link String} value.
     *
     * <p>This method uses {@link Enum#name()} to generate String value.</p>
     *
     * @param <T> The generic type of Enum
     * @param enumValue The value of enum
     *
     * @return null if the value of enum is null
     */
    public static <T extends Enum<T>> String getStringFromEnum(T enumValue)
    {
        if (enumValue == null) {
            return null;
        }

        return enumValue.name();
    }

    /**
     * Convert the value of {@link String} to {@link Enum} value.
     *
     * <p>This method uses {@link Enum#valueOf} to generate {@link Enum} value.</p>
     *
     * @param <T> The generic type of Enum
     * @param enumType The class of Enum
     * @param value The value of String
     *
     * @return null if the value of String is null
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> enumType, String value)
    {
        if (value == null) {
            return null;
        }

        return Enum.valueOf(enumType, value);
    }
}
