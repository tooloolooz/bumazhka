package ru.tooloolooz.bumazhka.snils;

import ru.tooloolooz.bumazhka.Assert;
import ru.tooloolooz.bumazhka.NotValidException;

/**
 * Utility class for validating Russian Insurance Number of Individual Ledger Account (SNILS).
 * <p>
 * This class provides methods to validate SNILS by checking its format,
 * ensuring it does not consist of all zeros, verifying the absence of three
 * consecutive identical digits in the account number, and performing mathematical
 * checksum validation according to the Russian Pension Fund (PFR/SFR) regulations.
 * </p>
 */
public final class SnilsValidator {

    /**
     * Index of the 1st character of the SNILS string.
     */
    private static final int INDEX_0 = 0;

    /**
     * Index of the 2nd character of the SNILS string.
     */
    private static final int INDEX_1 = 1;

    /**
     * Index of the 3rd character of the SNILS string.
     */
    private static final int INDEX_2 = 2;

    /**
     * Index of the 1st delimiter (dash) between digit groups.
     */
    private static final int INDEX_3 = 3;

    /**
     * Index of the 4th character of the SNILS string.
     */
    private static final int INDEX_4 = 4;

    /**
     * Index of the 5th character of the SNILS string.
     */
    private static final int INDEX_5 = 5;

    /**
     * Index of the 6th character of the SNILS string.
     */
    private static final int INDEX_6 = 6;

    /**
     * Index of the 2nd delimiter (dash) between digit groups.
     */
    private static final int INDEX_7 = 7;

    /**
     * Index of the 7th character of the SNILS string.
     */
    private static final int INDEX_8 = 8;

    /**
     * Index of the 8th character of the SNILS string.
     */
    private static final int INDEX_9 = 9;

    /**
     * Index of the 9th character of the SNILS string.
     */
    private static final int INDEX_10 = 10;

    /**
     * Index of the 3rd delimiter (dash or space) before the checksum.
     */
    private static final int INDEX_11 = 11;

    /**
     * Index of the 1st digit of the checksum.
     */
    private static final int INDEX_12 = 12;

    /**
     * Index of the 2nd digit of the checksum.
     */
    private static final int INDEX_13 = 13;

    /**
     * Divisor used for checksum calculation according to PFR/SFR regulations.
     */
    private static final int DIVISOR = 101;

    /**
     * The required length of a SNILS string in format {@code XXX-XXX-XXX YY} or {@code XXX-XXX-XXX-YY}.
     */
    private static final int STRING_LENGTH = 14;

    /**
     * Dash separator allowed between digit groups and before the checksum.
     */
    private static final char DASH_SEPARATOR = '-';

    /**
     *  Space separator allowed only before the checksum.
     */
    private static final char SPACE_SEPARATOR = ' ';

    /**
     * The length of the account number part of the SNILS (without delimiters and checksum).
     */
    private static final int ACCOUNT_NUMBER_LENGTH = 9;

    /**
     * The size of a digit group in the SNILS format (e.g., XXX).
     */
    private static final int DIGIT_GROUP_SIZE = 3;

    /**
     * The threshold value for the account number. Checksum validation is only performed
     * for numbers strictly greater than this value.
     */
    private static final int CHECKSUM_THRESHOLD = 1_001_998;

    /**
     * The multiplier for converting the account number string to an integer.
     */
    private static final int DIGIT_WEIGHT_MULTIPLIER = 100_000_000;

    /**
     * The divisor used for decimal digit conversion and multiplier reduction.
     */
    private static final int DIGIT_WEIGHT_MULTIPLIER_DIVISOR = 10;

    /**
     * The place value multiplier for digit positions.
     * Each digit's weight is determined by its position multiplied by this base.
     */
    private static final int PLACE_VALUE_MULTIPLIER = 10;

    /**
     * The modulo value used to extract the final two-digit checksum from the calculated sum.
     */
    private static final int TWO_DIGIT_MODULUS = 100;

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private SnilsValidator() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Validates the given SNILS string against formal rules and checksum requirements.
     *
     * @param snils the SNILS string to validate
     * @throws NotValidException if the SNILS is invalid
     * @throws IllegalArgumentException if the SNILS is {@code null}
     */
    public static void validate(final String snils) {
        if (!isValid(snils)) {
            throw new NotValidException("Invalid snils: " + snils);
        }
    }

    /**
     * Checks whether the given SNILS string is valid.
     * <p>
     * Validation includes checking for null values, ensuring the correct length,
     * verifying format (digits and valid delimiters), ensuring the number is not
     * {@code 000-000-000}, checking for consecutive triple digits, and validating
     * the checksum for numbers greater than {@code 001-001-998}.
     * </p>
     *
     * @param snils the SNILS string to check
     * @return {@code true} if the SNILS complies with all formal and mathematical rules; {@code false} otherwise
     * @throws IllegalArgumentException if the SNILS is {@code null}
     */
    private static boolean isValid(final String snils) {
        Assert.notNull(snils, "Snils must be not null");

        return snils.length() == STRING_LENGTH
                && isValidRegexp(snils)
                && isNotContainAllZeros(snils)
                && hasNoConsecutiveTripleDigits(snils)
                && isChecksumValid(snils);
    }

    /**
     * Checks if the string contains only digits at required positions and valid delimiters.
     *
     * @param snils the SNILS string to check
     * @return {@code true} if the format is correct; {@code false} otherwise
     */
    private static boolean isValidRegexp(final String snils) {
        return containsOnlyDigits(snils)
                && containsValidDelimiters(snils);
    }

    /**
     * Checks that the individual ledger account number is not {@code 000-000-000}.
     * <p>
     * According to PFR/SFR assignment practices, numbering starts from {@code 001-001-001}.
     * A number consisting entirely of zeros does not exist in reality.
     * </p>
     *
     * @param snils the SNILS string to check
     * @return {@code true} if at least one digit in the account number is not '0'; {@code false} otherwise
     */
    private static boolean isNotContainAllZeros(final String snils) {
        return snils.charAt(INDEX_0) != '0'
                || snils.charAt(INDEX_1) != '0'
                || snils.charAt(INDEX_2) != '0'
                || snils.charAt(INDEX_4) != '0'
                || snils.charAt(INDEX_5) != '0'
                || snils.charAt(INDEX_6) != '0'
                || snils.charAt(INDEX_8) != '0'
                || snils.charAt(INDEX_9) != '0'
                || snils.charAt(INDEX_10) != '0';
    }

    /**
     * Checks for the absence of three consecutive identical digits in the account number.
     * <p>
     * According to PFR/SFR rules, the first 9 digits of the SNILS cannot contain
     * the same digit three times in a row. The checksum digits (positions 12-13)
     * are not included in this check.
     * </p>
     *
     * @param snils the SNILS string to check
     * @return {@code true} if no three consecutive identical digits are found; {@code false} otherwise
     */
    private static boolean hasNoConsecutiveTripleDigits(final String snils) {
        char[] digits = getAccountNumberDigits(snils);

        for (int i = 0; i < digits.length - 2; i++) {
            if (digits[i] == digits[i + 1] && digits[i] == digits[i + 2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Internal method that computes and verifies the checksum of the given SNILS.
     * <p>
     * The algorithm calculates a weighted sum of the first 9 digits (weights from 9 down to 1).
     * The checksum is then derived using the formula {@code (sum % 101) % 100}, which correctly
     * handles the special PFR/SFR rule where a sum of 100 or 101 results in a checksum of {@code 00}.
     * This calculated value is compared against the last two digits of the SNILS.
     * </p>
     *
     * @param snils the SNILS string to check
     * @return {@code true} if the calculated checksum matches the control digits; {@code false} otherwise
     */
    private static boolean isChecksumValid(final String snils) {
        char[] digits = getAccountNumberDigits(snils);

        if (isUncheckableNumber(digits)) {
            return true;
        }

        int digitSum = 0;
        for (int i = 0; i < digits.length; i++) {
            digitSum += (digits[i] - '0') * (digits.length - i);
        }

        int calculatedChecksum = (digitSum % DIVISOR) % TWO_DIGIT_MODULUS;

        return calculatedChecksum
                == ((snils.charAt(INDEX_12) - '0') * PLACE_VALUE_MULTIPLIER + (snils.charAt(INDEX_13) - '0'));
    }

    /**
     * Determines if checksum validation should be skipped for the given account number.
     * <p>
     * According to official regulations, checksum validation is only performed for
     * account numbers strictly greater than {@code 001-001-998} (numeric value 1,001,998).
     * </p>
     *
     * @param digits the array of 9 account number digits
     * @return {@code true} if the number is {@code <= 1_001_998}; {@code false} otherwise
     */
    private static boolean isUncheckableNumber(final char[] digits) {
        int digitWeightMultiplier = DIGIT_WEIGHT_MULTIPLIER;
        int accountNumber = 0;

        for (char digit : digits) {
            accountNumber += (digit - '0') * digitWeightMultiplier;
            digitWeightMultiplier /= DIGIT_WEIGHT_MULTIPLIER_DIVISOR;
        }

        return accountNumber <= CHECKSUM_THRESHOLD;
    }

    /**
     * Checks that all required character positions in the string contain only digits.
     *
     * @param snils the SNILS string to check
     * @return {@code true} if all specified positions are digits; {@code false} otherwise
     */
    private static boolean containsOnlyDigits(final String snils) {
        return isDigit(snils.charAt(INDEX_0))
                && isDigit(snils.charAt(INDEX_1))
                && isDigit(snils.charAt(INDEX_2))
                && isDigit(snils.charAt(INDEX_4))
                && isDigit(snils.charAt(INDEX_5))
                && isDigit(snils.charAt(INDEX_6))
                && isDigit(snils.charAt(INDEX_8))
                && isDigit(snils.charAt(INDEX_9))
                && isDigit(snils.charAt(INDEX_10))
                && isDigit(snils.charAt(INDEX_12))
                && isDigit(snils.charAt(INDEX_13));
    }

    /**
     * Checks that the delimiters are present at the correct positions and are of valid types.
     *
     * @param snils the SNILS string to check
     * @return {@code true} if delimiters match the {@code XXX-XXX-XXX YY} or {@code XXX-XXX-XXX-YY} pattern;
     * {@code false} otherwise
     */
    private static boolean containsValidDelimiters(final String snils) {
        return snils.charAt(INDEX_3) == DASH_SEPARATOR
                && snils.charAt(INDEX_7) == DASH_SEPARATOR
                && (snils.charAt(INDEX_11) == DASH_SEPARATOR || snils.charAt(INDEX_11) == SPACE_SEPARATOR);
    }

    /**
     * Checks if a character is a decimal digit.
     *
     * @param character the character to check
     * @return {@code true} if the character is between '0' and '9'; {@code false} otherwise
     */
    private static boolean isDigit(final char character) {
        return '0' <= character && character <= '9';
    }

    /**
     * Extracts the 9 digits of the individual ledger account number from the SNILS string,
     * skipping the delimiter characters.
     *
     * @param snils the SNILS string
     * @return a char array of length 9 containing only the account number digits
     */
    private static char[] getAccountNumberDigits(final String snils) {
        char[] result = new char[ACCOUNT_NUMBER_LENGTH];

        for (int i = 0; i < result.length; i++) {
            result[i] = snils.charAt(i + (i / DIGIT_GROUP_SIZE));
        }

        return result;
    }
}
