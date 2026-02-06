package ru.tooloolooz.bumazhka;

import java.util.Set;

/**
 * Validator for state registration numbers (GRN, OGRN, OGRNIP).
 * This utility class provides methods for validating various types of Russian state registration
 * numbers used for legal entities and individual entrepreneurs. The validation includes format
 * checks, region code verification, and checksum validation.
 */
public final class GrnValidator {

    /**
     * Radix (base) divisor used in checksum calculations.
     */
    private static final int RADIX_DIVISOR = 10;

    /**
     * Divisor used for checksum calculation of EGRUL numbers.
     */
    private static final int EGRUL_DIVISOR = 11;

    /**
     * Divisor used for checksum calculation of EGRIP numbers.
     */
    private static final int EGRIP_DIVISOR = 13;

    /**
     * Index of the first character.
     * Contains the type identifier of the GRN record.
     */
    private static final int INDEX_0 = 0;

    /**
     * Index of the second character.
     * First digit of the year when the record was made.
     */
    private static final int INDEX_1 = 1;

    /**
     * Index of the third character.
     * Second digit of the year when the record was made.
     */
    private static final int INDEX_2 = 2;

    /**
     * Index of the fourth character.
     * Start of the region code.
     */
    private static final int INDEX_3 = 3;

    /**
     * Index of the sixth character.
     * Start of the sequential record number.
     */
    private static final int INDEX_5 = 5;

    /**
     * Index of the seventh character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_6 = 6;

    /**
     * Index of the eighth character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_7 = 7;

    /**
     * Index of the ninth character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_8 = 8;

    /**
     * Index of the tenth character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_9 = 9;

    /**
     * Index of the eleventh character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_10 = 10;

    /**
     * Index of the twelfth character.
     * Continuation of the sequential record number.
     */
    private static final int INDEX_11 = 11;

    /**
     * Index of the thirteenth character.
     * Continuation of the sequential record number (for EGRIP)
     * or checksum digit (for EGRUL).
     */
    private static final int INDEX_12 = 12;

    /**
     * Index of the fourteenth character.
     * Continuation of the sequential record number for EGRIP.
     */
    private static final int INDEX_13 = 13;

    /**
     * Index of the fifteenth character.
     * Checksum digit for EGRUL.
     */
    private static final int INDEX_14 = 14;

    /**
     * Length of EGRUL numbers.
     * Consists of 13 digits.
     */
    private static final int EGRUL_LENGTH = 13;

    /**
     * Length of EGRIP numbers.
     * Consists of 15 digits.
     */
    private static final int EGRIP_LENGTH = 15;

    /**
     * Valid vehicle region codes.
     * The codes represent subjects of the Russian Federation.
     */
    private static final Set<String> REGION_CODES = Set.of(
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
            "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72",
            "73", "74", "75", "76", "77", "78", "79", "83", "86", "87", "89", "90", "91", "92", "93", "94", "95", "99"
    );

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private GrnValidator() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Checks if a string is a valid GRN of any type.
     * Automatically determines the type by its length and first character.
     *
     * @param grn the string with the number to validate
     * @return {@code true} if the number complies with formal rules
     * for any of the supported types, {@code false} otherwise
     * @throws IllegalArgumentException if {@code grn} is {@code null}
     */
    public static boolean isValid(final String grn) {
        Assert.notNull(grn, "Grn must be not null");

        return isValidOgrn(grn)
                || isValidGrnEgrul(grn)
                || isValidOgrnip(grn)
                || isValidGrnEgrip(grn);
    }

    /**
     * Checks if a string is a valid GRN of the specified type.
     *
     * @param grn  the string with the number to validate
     * @param type the type of number to validate against
     * @return {@code true} if the number complies with formal rules
     * for the specified type, {@code false} otherwise
     * @throws IllegalArgumentException if {@code grn} or {@code type} is {@code null}
     */
    public static boolean isValid(final String grn, final GrnType type) {
        Assert.notNull(grn, "Grn must be not null");
        Assert.notNull(type, "Type must be not null");

        return switch (type) {
            case OGRN -> isValidOgrn(grn);
            case OGRNIP -> isValidOgrnip(grn);
            case GRN_EGRUL -> isValidGrnEgrul(grn);
            case GRN_EGRIP -> isValidGrnEgrip(grn);
            case ANY -> isValid(grn);
        };
    }

    /**
     * Checks if a string is a valid OGRN.
     * <p>
     * OGRN must comply with the following rules:
     * <ul>
     *     <li>Length of 13 characters</li>
     *     <li>First character: '1' or '5'</li>
     *     <li>All characters from position 2 to 13 are digits</li>
     *     <li>Contains a valid region code (positions 4-5)</li>
     *     <li>Check digit validation at position 13</li>
     * </ul>
     *
     * @param ogrn the string with OGRN to validate
     * @return {@code true} if the number matches the OGRN format,
     * {@code false} otherwise
     */
    private static boolean isValidOgrn(final String ogrn) {
        if (ogrn.length() != EGRUL_LENGTH) {
            return false;
        }

        final char firstChar = ogrn.charAt(INDEX_0);
        return (firstChar == '1' || firstChar == '5')
                && REGION_CODES.contains(ogrn.substring(INDEX_3, INDEX_5))
                && isDigit(ogrn.charAt(INDEX_1))
                && isDigit(ogrn.charAt(INDEX_2))
                && isDigit(ogrn.charAt(INDEX_5))
                && isDigit(ogrn.charAt(INDEX_6))
                && isDigit(ogrn.charAt(INDEX_7))
                && isDigit(ogrn.charAt(INDEX_8))
                && isDigit(ogrn.charAt(INDEX_9))
                && isDigit(ogrn.charAt(INDEX_10))
                && isDigit(ogrn.charAt(INDEX_11))
                && isDigit(ogrn.charAt(INDEX_12))
                && validateEgrulChecksum(ogrn);
    }

    /**
     * Checks if a string is a valid OGRNIP.
     * <p>
     * OGRNIP must comply with the following rules:
     * <ul>
     *     <li>Length of 15 characters</li>
     *     <li>First character: '3'</li>
     *     <li>All characters from position 2 to 15 are digits</li>
     *     <li>Contains a valid region code (positions 4-5)</li>
     *     <li>Check digit validation at position 15</li>
     * </ul>
     *
     * @param ogrnip the string with OGRNIP to validate
     * @return {@code true} if the number matches the OGRNIP format,
     * {@code false} otherwise
     */
    private static boolean isValidOgrnip(final String ogrnip) {
        if (ogrnip.length() != EGRIP_LENGTH) {
            return false;
        }

        return ogrnip.charAt(INDEX_0) == '3'
                && REGION_CODES.contains(ogrnip.substring(INDEX_3, INDEX_5))
                && isDigit(ogrnip.charAt(INDEX_1))
                && isDigit(ogrnip.charAt(INDEX_2))
                && isDigit(ogrnip.charAt(INDEX_5))
                && isDigit(ogrnip.charAt(INDEX_6))
                && isDigit(ogrnip.charAt(INDEX_7))
                && isDigit(ogrnip.charAt(INDEX_8))
                && isDigit(ogrnip.charAt(INDEX_9))
                && isDigit(ogrnip.charAt(INDEX_10))
                && isDigit(ogrnip.charAt(INDEX_11))
                && isDigit(ogrnip.charAt(INDEX_12))
                && isDigit(ogrnip.charAt(INDEX_13))
                && isDigit(ogrnip.charAt(INDEX_14))
                && validateEgripChecksum(ogrnip);
    }

    /**
     * Checks if a string is a valid GRN EGRUL.
     * <p>
     * GRN EGRUL must comply with the following rules:
     * <ul>
     *     <li>Length of 13 characters</li>
     *     <li>First character: '2', '6', '7', '8', or '9'</li>
     *     <li>All characters from position 2 to 13 are digits</li>
     *     <li>Contains a valid region code (positions 4-5)</li>
     *     <li>Check digit validation at position 13</li>
     * </ul>
     *
     * @param grnEgrul the string with GRN EGRUL to validate
     * @return {@code true} if the number matches the GRN EGRUL format,
     * {@code false} otherwise
     */
    private static boolean isValidGrnEgrul(final String grnEgrul) {
        if (grnEgrul.length() != EGRUL_LENGTH) {
            return false;
        }

        final char firstChar = grnEgrul.charAt(INDEX_0);
        return (firstChar == '2' || ('6' <= firstChar && firstChar <= '9'))
                && REGION_CODES.contains(grnEgrul.substring(INDEX_3, INDEX_5))
                && isDigit(grnEgrul.charAt(INDEX_1))
                && isDigit(grnEgrul.charAt(INDEX_2))
                && isDigit(grnEgrul.charAt(INDEX_5))
                && isDigit(grnEgrul.charAt(INDEX_6))
                && isDigit(grnEgrul.charAt(INDEX_7))
                && isDigit(grnEgrul.charAt(INDEX_8))
                && isDigit(grnEgrul.charAt(INDEX_9))
                && isDigit(grnEgrul.charAt(INDEX_10))
                && isDigit(grnEgrul.charAt(INDEX_11))
                && isDigit(grnEgrul.charAt(INDEX_12))
                && validateEgrulChecksum(grnEgrul);
    }

    /**
     * Checks if a string is a valid GRN EGRIP.
     * <p>
     * GRN EGRIP must comply with the following rules:
     * <ul>
     *     <li>Length of 15 characters</li>
     *     <li>First character: '4'</li>
     *     <li>All characters from position 2 to 14 are digits</li>
     *     <li>Contains a valid region code (positions 4-5)</li>
     *     <li>Check digit validation at position 15</li>
     * </ul>
     *
     * @param grnEgrip the string with GRN EGRIP to validate
     * @return {@code true} if the number matches the GRN EGRIP format,
     * {@code false} otherwise
     */
    private static boolean isValidGrnEgrip(final String grnEgrip) {
        if (grnEgrip.length() != EGRIP_LENGTH) {
            return false;
        }

        return grnEgrip.charAt(INDEX_0) == '4'
                && REGION_CODES.contains(grnEgrip.substring(INDEX_3, INDEX_5))
                && isDigit(grnEgrip.charAt(INDEX_1))
                && isDigit(grnEgrip.charAt(INDEX_2))
                && isDigit(grnEgrip.charAt(INDEX_5))
                && isDigit(grnEgrip.charAt(INDEX_6))
                && isDigit(grnEgrip.charAt(INDEX_7))
                && isDigit(grnEgrip.charAt(INDEX_8))
                && isDigit(grnEgrip.charAt(INDEX_9))
                && isDigit(grnEgrip.charAt(INDEX_10))
                && isDigit(grnEgrip.charAt(INDEX_11))
                && isDigit(grnEgrip.charAt(INDEX_12))
                && isDigit(grnEgrip.charAt(INDEX_13))
                && isDigit(grnEgrip.charAt(INDEX_14))
                && validateEgripChecksum(grnEgrip);
    }


    /**
     * Validates the checksum for EGRUL numbers (OGRN and GRN EGRUL).
     * <p>
     * The checksum is calculated as follows:
     * (number without last digit ÷ 11) mod 10 = last digit
     *
     * @param grn the GRN string to validate
     * @return {@code true} if the checksum is valid, {@code false} otherwise
     */
    private static boolean validateEgrulChecksum(final String grn) {
        return Long.parseLong(grn.substring(INDEX_0, INDEX_12)) / EGRUL_DIVISOR
                % RADIX_DIVISOR == grn.charAt(INDEX_12) - '0';
    }

    /**
     * Validates the checksum for EGRIP numbers (OGRNIP and GRN EGRIP).
     * <p>
     * The checksum is calculated as follows:
     * (number without last digit ÷ 13) mod 10 = last digit
     *
     * @param grn the GRN string to validate
     * @return {@code true} if the checksum is valid, {@code false} otherwise
     */
    private static boolean validateEgripChecksum(final String grn) {
        return Long.parseLong(grn.substring(INDEX_0, INDEX_14)) / EGRIP_DIVISOR
                % RADIX_DIVISOR == grn.charAt(INDEX_14) - '0';
    }

    /**
     * Checks if a character is a digit ('0' to '9').
     * This method provides a more readable alternative to direct character range comparison.
     * It's equivalent to {@code Character.isDigit(char)} but only recognizes ASCII digits
     * ('0'-'9') and not other Unicode digit characters.
     *
     * @param character the character to check
     * @return {@code true} if the character is an ASCII digit ('0' through '9'),
     * {@code false} otherwise
     */
    private static boolean isDigit(final char character) {
        return '0' <= character && character <= '9';
    }

    /**
     * Types of State Registration Numbers.
     */
    public enum GrnType {
        /**
         * Primary State Registration Number of a legal entity.
         * Format: 13 digits, starts with 1 or 5.
         */
        OGRN,

        /**
         * Primary State Registration Number of an individual entrepreneur.
         * Format: 15 digits, starts with 3.
         */
        OGRNIP,

        /**
         * State Registration Number of a record in EGRUL.
         * Format: 13 digits, starts with 2, 6, 7, 8, or 9.
         */
        GRN_EGRUL,

        /**
         * State Registration Number of a record in EGRIP.
         * Format: 15 digits, starts with 4.
         */
        GRN_EGRIP,

        /**
         * Any of the supported types.
         * Automatic detection based on length and first character.
         */
        ANY
    }
}
