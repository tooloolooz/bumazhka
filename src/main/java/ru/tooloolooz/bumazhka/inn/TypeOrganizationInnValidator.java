package ru.tooloolooz.bumazhka.inn;

import ru.tooloolooz.bumazhka.CharacterUtils;

/**
 * Validator for the Taxpayer Identification Number (INN) of an Organization.
 *
 * <p>
 * This class validates whether a given string is a valid 10-digit organization INN
 * by checking its length and verifying its checksum according to the Russian tax regulations.
 */
final class TypeOrganizationInnValidator implements TypeInnValidator {
    /**
     * The thread-safe singleton instance of this validator.
     */
    public static final TypeOrganizationInnValidator INSTANCE = new TypeOrganizationInnValidator();

    /**
     * The required length of an organization taxpayer identification number (INN) (10 digits).
     */
    private static final int ORGANIZATION_INN_SIZE = 10;

    /**
     * The coefficients used in the weighted sum calculation for the checksum verification.
     */
    private static final int[] COEFFICIENT = {2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * Position of the control number.
     */
    private static final int CONTROL_NUMBER_POSITION = 9;

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
    private TypeOrganizationInnValidator() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Validates if the given string is a valid organization taxpayer identification number (INN).
     *
     * <p>
     * The validation process includes:
     * <ol>
     *   <li>Checking that the string length is exactly {@value ORGANIZATION_INN_SIZE} characters.</li>
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
        if (inn.length() != ORGANIZATION_INN_SIZE) {
            return false;
        }

        return InnValidatorUtils.isValidRegionCode(inn)
                && InnValidatorUtils.isValidInnNumber(inn)
                && isValidChecksum(inn);
    }

    /**
     * Internal method that computes and verifies the checksum of the given organization INN.
     *
     * <p>
     * The algorithm calculates a weighted sum of the first 9 digits using the defined {@link #COEFFICIENT} array.
     * The remainder of the sum divided by 11 is then compared to the 10th (last) digit of the inn.
     *
     * @param inn the 10-digit taxpayer identification number (INN) string to check
     * @return {@code true} if the calculated checksum matches the last digit;
     *         {@code false} otherwise
     */
    private boolean isValidChecksum(final String inn) {
        int result = 0;

        for (int i = 0; i < CONTROL_NUMBER_POSITION; i++) {
            result += CharacterUtils.toInt(inn.charAt(i)) * COEFFICIENT[i];
        }

        return result % CONTROL_DIVIDER == CharacterUtils.toInt(inn.charAt(CONTROL_NUMBER_POSITION));
    }
}
