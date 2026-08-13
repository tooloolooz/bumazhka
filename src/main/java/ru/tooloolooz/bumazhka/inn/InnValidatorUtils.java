package ru.tooloolooz.bumazhka.inn;

import ru.tooloolooz.bumazhka.Assert;
import ru.tooloolooz.bumazhka.CharacterUtils;

/**
 * A utility class providing for working with INN validator classes.
 */
final class InnValidatorUtils {

    /**
     * Position constant for the first character.
     */
    private static final int POSITION_1 = 0;

    /**
     * Position constant for the second character.
     */
    private static final int POSITION_2 = 1;

    /**
     * Position constant for the third character.
     */
    private static final int POSITION_3 = 2;

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private InnValidatorUtils() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Checks if the number portion of the INN consists only of decimal digits (0-9).
     *
     * <p>
     * The number starts at position {@value POSITION_3} (zero-based).
     * All characters from this position to the end of the string are validated.
     *
     * @param inn the taxpayer identification number (INN) string to check
     * @return {@code true} if all characters in the number portion are digits;
     *         {@code false} otherwise
     */
    public static boolean isValidInnNumber(final String inn) {
        return inn.substring(POSITION_3).chars().allMatch(character -> CharacterUtils.isDigit((char) character));
    }

    /**
     * Validates the basic structural format of the region code.
     * 
     * <p>
     * Ensures that the first two digits (representing the region code) are not both zeros.
     *
     * @param inn the taxpayer identification number (INN) string to check
     * @return {@code true} if the region code is valid (region code is not "00"); {@code false} otherwise
     */
    public static boolean isValidRegionCode(final String inn) {
        return inn.charAt(POSITION_1) != '0' || inn.charAt(POSITION_2) != '0';
    }
}
