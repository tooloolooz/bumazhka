package ru.tooloolooz.bumazhka.inn;

import ru.tooloolooz.bumazhka.Assert;
import ru.tooloolooz.bumazhka.NotValidException;

/**
 * Utility class for validating Russian Taxpayer Identification Numbers (INN).
 *
 * <p>
 * This class provides methods to validate both 10-digit organizational (legal entities)
 * and 12-digit individual (including sole proprietors) taxpayer identification numbers (INN)
 * by checking their format, regional codes, and mathematical checksums.
 *
 * @see <a href="https://www.consultant.ru/document/cons_doc_LAW_516257/8797ea564fafba110dde741869c230c3273e82c6/">
 *     Order of the Federal Tax Service of Russia dated June 26, 2025 No. ED-7-14/559</a>
 */
public final class InnValidator {
    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private InnValidator() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Validates the given taxpayer identification number (INN) against any supported type
     * (either Individual or Organization).
     *
     * @param inn the taxpayer identification number (INN) string to validate
     * @throws NotValidException        if the inn is not a valid Individual or Organization INN
     * @throws IllegalArgumentException if the inn is null
     */
    public static void validate(final String inn) {
        validate(inn, InnType.ANY);
    }

    /**
     * Validates the given taxpayer identification number (INN) against a specific INN type.
     *
     * @param inn the taxpayer identification number (INN) string to validate
     * @param type   the expected type of the taxpayer (INDIVIDUAL or ORGANIZATION or ANY)
     * @throws NotValidException        if the inn is invalid for the specified type
     * @throws IllegalArgumentException if the inn or type is null
     */
    public static void validate(final String inn, final InnType type) {
        if (!isValid(inn, type)) {
            throw new NotValidException("Invalid INN: " + inn);
        }
    }

    /**
     * Checks whether the given taxpayer identification number (INN) is valid for any supported INN type.
     *
     * <p>
     * Validation includes checking for null values, ensuring the string contains only digits,
     * and verifying if it passes checksum validation for either individual or organizational INN.
     *
     * @param inn the taxpayer identification number (INN) string to check
     * @return {@code true} if the inn is a valid Individual or Organization INN;
     *         {@code false} otherwise
     * @throws IllegalArgumentException if the inn is null
     */
    public static boolean isValid(final String inn) {
        return isValid(inn, InnType.ANY);
    }

    /**
     * Checks whether the given taxpayer identification number (INN) is valid for a specific INN type.
     *
     * <p>
     * Validation includes checking for null values, ensuring the string contains only digits,
     * verifying regional format requirements, and performing mathematical checksum validation.
     *
     * @param inn the taxpayer identification number (INN) string to check
     * @param type   the expected type of the taxpayer (INDIVIDUAL or ORGANIZATION or ANY)
     * @return {@code true} if the inn is valid for the specified type;
     *         {@code false} otherwise
     * @throws IllegalArgumentException if the inn or type is null
     */
    public static boolean isValid(final String inn, final InnType type) {
        Assert.notNull(inn, "INN must be not null");
        Assert.notNull(type, "Type must be not null");

        return getValidator(type).isValid(inn);
    }

    /**
     * Resolves and returns the appropriate validator instance based on the INN type.
     *
     * @param type the taxpayer type enum
     * @return the corresponding {@link TypeInnValidator} implementation instance
     */
    private static TypeInnValidator getValidator(final InnType type) {
        return switch (type) {
            case INDIVIDUAL -> TypeIndividualInnValidator.INSTANCE;
            case ORGANIZATION -> TypeOrganizationInnValidator.INSTANCE;
            case ANY -> TypeAnyInnValidator.INSTANCE;
        };
    }

    /**
     * Supported types of taxpayer identification numbers (INN).
     */
    public enum InnType {
        /**
         * Individual Taxpayer Number (INN) for individuals.
         *
         * <p>
         * Format: {@code 000000000000}
         * Where:
         * <ul>
         *   <li>0 - digit indicating the number.</li>
         * </ul>
         *
         * <p>
         * <b>Structure (12 digits total):</b>
         * <ul>
         *   <li>Digits 1-2: Code of the region of the Russian Federation.</li>
         *   <li>Digits 3-4: Code of the local tax inspectorate.</li>
         *   <li>Digits 5-10: Sequential number of the record in the territorial section
         *   of the Unified State Register of Taxpayers.</li>
         *   <li>Digit 11: First check digit calculated from the first 10 digits.</li>
         *   <li>Digit 12: Second check digit calculated from the first 11 digits.</li>
         * </ul>
         */
        INDIVIDUAL,

        /**
         * Individual Taxpayer Number (INN) for organizations.
         *
         * <p>
         * Format: {@code 0000000000}
         * Where:
         * <ul>
         *   <li>0 - digit indicating the number.</li>
         * </ul>
         *
         * <p>
         * <b>Structure (10 digits total):</b>
         * <ul>
         *   <li>Digits 1-2: Code of the region of the Russian Federation.</li>
         *   <li>Digits 3-4: Code of the local tax inspectorate.</li>
         *   <li>Digits 5-9: Sequential number of the record in the territorial section
         *   of the Unified State Register of Legal Entities.</li>
         *   <li>Digit 10: Check digit calculated from the first 9 digits.</li>
         * </ul>
         */
        ORGANIZATION,

        /**
         * Any type of taxpayer identification number (INN) individuals or organizations.
         */
        ANY
    }
}
