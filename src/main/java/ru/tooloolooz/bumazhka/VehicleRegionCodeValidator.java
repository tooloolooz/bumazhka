package ru.tooloolooz.bumazhka;

import java.util.Set;

/**
 * Validator for vehicle region codes.
 * This utility class provides validation for Russian Federation region codes used by vehicle registration plates.
 * It supports both 2-digit region codes and 3-digit vehicle codes.
 *
 * @see <a href="http://publication.pravo.gov.ru/document/0001201801100004"/>
 */
public final class VehicleRegionCodeValidator {

    /**
     * Valid vehicle region codes.
     * The codes represent subjects of the Russian Federation.
     */
    private static final Set<String> REGION_CODES = Set.of(
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "95", "21", "82", "22", "59", "81", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
            "36", "37", "38", "85", "39", "40", "42", "43", "44", "45", "46", "47", "48", "49", "50", "90", "51", "75",
            "80", "41", "23", "93", "24", "84", "88", "57", "58", "60", "61", "62", "63", "64", "65", "66", "96", "67",
            "68", "69", "70", "71", "72", "73", "74", "76", "52", "53", "54", "55", "56", "77", "97", "99", "78", "98",
            "92", "79", "83", "86", "87", "89", "94"
    );

    /**
     * Length of 2-digit vehicle codes.
     */
    private static final int TWO_DIGIT_CODE_LENGTH = 2;

    /**
     * Length of 3-digit vehicle codes.
     */
    private static final int THREE_DIGIT_CODE_LENGTH = 3;

    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private VehicleRegionCodeValidator() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Validates either a 2-digit region code or a 3-digit vehicle region codes.
     * Automatically detects the code type based on length and validates accordingly.
     *
     * @param code the code to validate.
     * @return {@code true} if the code is valid, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code code} is {@code null}.
     */
    public static boolean isValid(final String code) {
        Assert.notNull(code, "Code must be not null");

        final int length = code.length();
        return switch (length) {
            case TWO_DIGIT_CODE_LENGTH -> isValidTwoDigit(code);
            case THREE_DIGIT_CODE_LENGTH -> isValidThreeDigit(code);
            default -> false;
        };
    }

    /**
     * Validates a vehicle region code with a specified expected length.
     * Allows explicit control over which type of code is being validated.
     *
     * @param code   the code to validate.
     * @param length the expected length of the code.
     * @return {@code true} if the code is valid, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code code} or {@code length} is {@code null}.
     */
    public static boolean isValid(final String code, final RegionCodeLength length) {
        Assert.notNull(code, "Code must be not null");
        Assert.notNull(length, "Length must be not null");

        return switch (length) {
            case TWO_DIGIT -> isValidTwoDigit(code);
            case THREE_DIGIT -> isValidThreeDigit(code);
            case ANY -> isValid(code);
        };
    }

    /**
     * Validates 2-digit vehicle region codes.
     * Checks if the provided code exists in the official list of Russian region codes.
     *
     * @param code the code to validate.
     * @return {@code true} if the code is valid, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code code} is {@code null}.
     */
    private static boolean isValidTwoDigit(final String code) {
        final int length = code.length();
        if (length != TWO_DIGIT_CODE_LENGTH) {
            return false;
        }
        return REGION_CODES.contains(code);
    }

    /**
     * Validates a 3-digit vehicle region codes.
     * Checks if the first digit is in allowable range and the last two digits is a valid vehicle region codes.
     *
     * @param code the code to validate.
     * @return {@code true} if the code format is valid and region part exists, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code code} is {@code null}.
     */
    private static boolean isValidThreeDigit(final String code) {
        final int length = code.length();
        if (length != THREE_DIGIT_CODE_LENGTH) {
            return false;
        }
        final char firstChar = code.charAt(0);
        final String regionCode = code.substring(1, length);
        return '1' <= firstChar && firstChar <= '9' && REGION_CODES.contains(regionCode);
    }

    /**
     * Enumeration representing the supported lengths for vehicle region codes.
     * Used to specify which type of code should be validated.
     */
    public enum RegionCodeLength {
        /**
         * Represents 2-digit region codes.
         */
        TWO_DIGIT,

        /**
         * Represents 3-digit vehicle codes.
         */
        THREE_DIGIT,

        /**
         * Accepts either 2-digit or 3-digit codes, auto-detected by length.
         */
        ANY
    }
}
