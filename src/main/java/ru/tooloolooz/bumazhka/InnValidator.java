package ru.tooloolooz.bumazhka;

/**
 * A utility class for validating Russian Individual Taxpayer Numbers (INN).
 * <p>
 * This class provides methods to validate INN strings for both legal entities (10-digit INN)
 * and individuals (12-digit INN). Validation includes checksum verification according to
 * Russian tax authority algorithms.
 * </p>
 *
 * <p>The validation algorithm for legal entities (10-digit INN):</p>
 * <ul>
 *   <li>First 9 digits are multiplied by corresponding coefficients</li>
 *   <li>Products are summed and divided by 11</li>
 *   <li>The remainder is divided by 10</li>
 *   <li>Result must match the 10th digit</li>
 * </ul>
 *
 * <p>The validation algorithm for individuals (12-digit INN):</p>
 * <ul>
 *   <li>First 10 digits are used to calculate the 11th checksum digit</li>
 *   <li>First 11 digits are used to calculate the 12th checksum digit</li>
 *   <li>Both checksums must match the corresponding digits</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Validate without specifying type (auto-detects by length)
 * boolean isValid1 = InnValidator.isValid("123456789012"); // 12-digit individual INN
 *
 * // Validate with explicit type
 * boolean isValid2 = InnValidator.isValid("1234567890", InnType.LEGAL_ENTITIES);
 *
 * // Validate any type explicitly
 * boolean isValid3 = InnValidator.isValid("123456789012", InnType.ANY);
 * }</pre>
 *
 * @author Dinoel Nirgihc
 * @version 1.0
 * @see InnType
 */
public final class InnValidator {

    private static final int[] LE_CHECKSUM_COEFFICIENTS_FOR_TENTH_NUMBER =
            {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] INDIVIDUAL_CHECKSUM_COEFFICIENTS_FOR_ELEVENTH_NUMBER =
            {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] INDIVIDUAL_CHECKSUM_COEFFICIENTS_FOR_TWELFTH_NUMBER =
            {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int FIRST_DIVIDER = 11;
    private static final int SECOND_DIVIDER = 10;
    private static final int LE_INN_LENGTH = 10;
    private static final int INDIVIDUAL_INN_LENGTH = 12;


    /**
     * Validates an INN string, automatically detecting the INN type based on its length.
     * <p>
     * The method determines the INN type as follows:
     * <ul>
     *   <li>10-digit strings are validated as legal entities INN</li>
     *   <li>12-digit strings are validated as individual INN</li>
     *   <li>Any other length returns {@code false}</li>
     * </ul>
     * </p>
     *
     * @param inn the INN string to validate, must not be {@code null}
     * @return {@code true} if the INN has correct length, contains only digits,
     *         is not all zeros, and passes checksum validation; {@code false} otherwise
     * @throws IllegalArgumentException if {@code inn} is {@code null}
     */
    public static boolean isValid(final String inn) {
        Assert.notNull(inn, "Inn must be not null");

        final int length = inn.length();
        return switch (length) {
            case LE_INN_LENGTH -> isValidLegalEntities(inn);
            case INDIVIDUAL_INN_LENGTH -> isValidIndividual(inn);
            default -> false;
        };
    }

    /**
     * Validates an INN string against a specified INN type.
     * <p>
     * This method allows explicit control over which validation algorithm to use,
     * regardless of the actual string length.
     * </p>
     *
     * @param inn the INN string to validate, must not be {@code null}
     * @param innType the type of INN validation to perform:
     *                <ul>
     *                  <li>{@code LEGAL_ENTITIES} - validates as 10-digit legal entity INN</li>
     *                  <li>{@code INDIVIDUAL} - validates as 12-digit individual INN</li>
     *                  <li>{@code ANY} - auto-detects based on length (same as {@link #isValid(String)})</li>
     *                </ul>
     * @return {@code true} if the INN passes validation for the specified type;
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code inn} is {@code null}
     * @throws NullPointerException if {@code innType} is {@code null}
     */
    public static boolean isValid(final String inn, final InnType innType) {
        Assert.notNull(inn, "Inn must be not null");

        return switch (innType) {
            case LEGAL_ENTITIES -> isValidLegalEntities(inn);
            case INDIVIDUAL -> isValidIndividual(inn);
            case ANY -> isValid(inn);
        };
    }

    private static boolean isValidIndividual(final String inn) {
        final int length = inn.length();
        final boolean hasCorrectStructure = hasCorrectStructure(inn);
        if (length != INDIVIDUAL_INN_LENGTH || !hasCorrectStructure) {
            return false;
        }
        final int eleventhNum = Character.getNumericValue(inn.charAt(10));
        final int twelfthNum = Character.getNumericValue(inn.charAt(11));
        final int eleventhControlSum = calculateEleventhControlSumIndividual(inn);
        final int twelfthNumControlsSum = calculateTwelfthControlSumIndividual(inn);

        return eleventhNum == eleventhControlSum && twelfthNum == twelfthNumControlsSum;
    }

    private static boolean isValidLegalEntities(final String inn) {
        final int length = inn.length();
        final boolean hasCorrectStructure = hasCorrectStructure(inn);
        if (length != LE_INN_LENGTH || !hasCorrectStructure) {
            return false;
        }
        final int tenthNum = Character.getNumericValue(inn.charAt(9));
        final int tenthControlSum = calculateTenthControlSumLe(inn);
        return tenthNum == tenthControlSum;
    }

    private static int calculateTwelfthControlSumIndividual(final String inn) {
        final int lengthWithoutTwelfthNum = inn.length() - 1;
        int twelfthControlSum = 0;
        for (int i = 0; i < lengthWithoutTwelfthNum; i++) {
            final int number = Character.getNumericValue(inn.charAt(i));
            final int coefficient = INDIVIDUAL_CHECKSUM_COEFFICIENTS_FOR_TWELFTH_NUMBER[i];
            twelfthControlSum += number * coefficient;
        }
        twelfthControlSum = twelfthControlSum % FIRST_DIVIDER;
        twelfthControlSum = twelfthControlSum % SECOND_DIVIDER;
        return twelfthControlSum;
    }

    private static int calculateEleventhControlSumIndividual(final String inn) {
        final int lengthWithoutEleventhNum = inn.length() - 2;
        int eleventhControlSum = 0;
        for (int i = 0; i < lengthWithoutEleventhNum; i++) {
            final int number = Character.getNumericValue(inn.charAt(i));
            final int coefficient = INDIVIDUAL_CHECKSUM_COEFFICIENTS_FOR_ELEVENTH_NUMBER[i];
            eleventhControlSum += number * coefficient;
        }
        eleventhControlSum = eleventhControlSum % FIRST_DIVIDER;
        eleventhControlSum = eleventhControlSum % SECOND_DIVIDER;
        return eleventhControlSum;
    }

    private static int calculateTenthControlSumLe(final String inn) {
        final int lengthWithoutTenthNum = inn.length() - 1;
        int tenthControlSum = 0;
        for (int i = 0; i < lengthWithoutTenthNum; i++) {
            final int number = Character.getNumericValue(inn.charAt(i));
            final int coefficient = LE_CHECKSUM_COEFFICIENTS_FOR_TENTH_NUMBER[i];
            tenthControlSum += number * coefficient;
        }
        tenthControlSum = tenthControlSum % FIRST_DIVIDER;
        tenthControlSum = tenthControlSum % SECOND_DIVIDER;
        return tenthControlSum;
    }

    private static boolean hasCorrectStructure(final String inn) {
        return inn.chars().allMatch(Character::isDigit) && isNotAllZero(inn);
    }

    private static boolean isNotAllZero(final String inn) {
        boolean allZeros = inn.chars().allMatch(c -> c == '0');
        return !allZeros;
    }

    /**
     * Enumeration of INN types for explicit validation.
     * <p>
     * Used with {@link InnValidator#isValid(String, InnType)} to specify
     * which validation algorithm should be applied.
     * </p>
     */
    public enum InnType {
        LEGAL_ENTITIES, INDIVIDUAL, ANY
    }
}
