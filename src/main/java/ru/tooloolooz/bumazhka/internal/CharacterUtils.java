package ru.tooloolooz.bumazhka.internal;

/**
 * A utility class providing convenient methods for working with characters.
 *
 * <p>This class contains static utility methods that extend the functionality
 * of the standard {@link Character} class.</p>
 */
public final class CharacterUtils {

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private CharacterUtils() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Checks if a character is a decimal digit (0-9).
     *
     * @param character the character to check for digit status
     * @return {@code true} if the character is a basic digit (0-9), {@code false} otherwise
     */
    public static boolean isDigit(final char character) {
        return '0' <= character && character <= '9';
    }

    /**
     * Checks if a character is a non-zero decimal digit (1-9).
     *
     * @param character the character to check for digit status
     * @return {@code true} if the character is a basic digit (1-9), {@code false} otherwise
     */
    public static boolean isNonZeroDigit(final char character) {
        return '1' <= character && character <= '9';
    }
}
