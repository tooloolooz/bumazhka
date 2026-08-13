package ru.tooloolooz.bumazhka.inn;

import ru.tooloolooz.bumazhka.CharacterUtils;

/**
 * Validator for the Taxpayer Identification Number (INN) of an Individual.
 *
 * <p>
 * This class validates whether a given string is a valid 12-digit individualINN
 * by checking its length and verifying its checksum according to the Russian tax regulations.
 */
final class TypeIndividualInnValidator implements TypeInnValidator {
    /**
     * The thread-safe singleton instance of this validator.
     */
    public static final TypeIndividualInnValidator INSTANCE = new TypeIndividualInnValidator();

    /**
     * The required length of an individual taxpayer identification number (12 digits).
     */
    private static final int INDIVIDUAL_INN_SIZE = 12;

    /**
     * The coefficients used in the weighted sum calculation for the first control digit (11th character).
     */
    private static final int[] FIRST_COEFFICIENT = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * The coefficients used in the weighted sum calculation for the second control digit (12th character).
     */
    private static final int[] SECOND_COEFFICIENT = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * Position of the first control number.
     */
    private static final int FIRST_CONTROL_NUMBER_POSITION = 10;

    /**
     * Position of the second control number.
     */
    private static final int SECOND_CONTROL_NUMBER_POSITION = 11;

    /**
     * Divider used for calculating the control number.
     */
    private static final int CONTROL_DIVIDER = 11;

    /**
     * Private constructor to enforce non-instantiability.
     *
     * <p>
     * All functionality is provided through static methods and the singleton
     * instance {@link #INSTANCE}.
     */
    private TypeIndividualInnValidator() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Validates if the given string is a valid individual taxpayer identification number (INN).
     *
     * <p>
     * The validation process includes:
     * <ol>
     *   <li>Checking that the string length is exactly {@value INDIVIDUAL_INN_SIZE} characters.</li>
     *   <li>Verifying that the region code (first 2 digits) is not "00".</li>
     *   <li>Verifying that if the number portion of the INN consists only of decimal digits (0-9).</li>
     *   <li>Verifying the control digit (checksum) at the end of the number.</li>
     * </ol>
     *
     * @param inn the taxpayer identification number (INN) string to validate, cannot be null
     * @return {@code true} if the inn is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(final String inn) {
        if (inn.length() != INDIVIDUAL_INN_SIZE) {
            return false;
        }

        return InnValidatorUtils.isValidRegionCode(inn)
                && InnValidatorUtils.isValidInnNumber(inn)
                && isValidChecksum(inn);
    }

    /**
     * Internal method that computes and verifies the dual checksums of the given individual INN.
     *
     * <p>
     * The algorithm calculates two separate weighted sums:
     * <ul>
     * <li>The first sum uses the first 10 digits and {@link #FIRST_COEFFICIENT} to verify the 11th digit.</li>
     * <li>The second sum uses the first 11 digits and {@link #SECOND_COEFFICIENT} to verify the 12th digit.</li>
     * </ul>
     * Both calculations take the remainder of the sum divided by 11 to match against their respective control digits.
     *
     * @param inn the 12-digit taxpayer identification number (INN) string to check
     * @return {@code true} if both calculated checksums match their control digits;
     *         {@code false} otherwise
     */
    private boolean isValidChecksum(final String inn) {
        int firstResult = 0;
        int secondResult = 0;

        for (int i = 0; i < SECOND_CONTROL_NUMBER_POSITION; i++) {
            final int digit = CharacterUtils.toInt(inn.charAt(i));
        
            if (i < FIRST_COEFFICIENT.length) {
                firstResult += digit * FIRST_COEFFICIENT[i];
            }
            if (i < SECOND_COEFFICIENT.length) {
                secondResult += digit * SECOND_COEFFICIENT[i];
            }
        }

        final int firstControlNumber = CharacterUtils.toInt(inn.charAt(FIRST_CONTROL_NUMBER_POSITION));
        final int secondControlNumber = CharacterUtils.toInt(inn.charAt(SECOND_CONTROL_NUMBER_POSITION));

        return firstResult % CONTROL_DIVIDER == firstControlNumber
                && secondResult % CONTROL_DIVIDER == secondControlNumber;
    }
}
