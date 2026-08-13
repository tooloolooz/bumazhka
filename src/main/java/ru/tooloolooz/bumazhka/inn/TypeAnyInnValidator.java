package ru.tooloolooz.bumazhka.inn;

/**
 * Validator for the Taxpayer Identification Number (INN) of any type.
 * 
 * <p>
 * This class validates whether a given string is a valid 10-digit legal entity INN
 * or a valid 12-digit individual INN by checking its length and verifying
 * its checksum according to the Russian tax regulations.
 */
final class TypeAnyInnValidator implements TypeInnValidator {
    /**
     * The thread-safe singleton instance of this validator.
     */
    public static final TypeAnyInnValidator INSTANCE = new TypeAnyInnValidator();

    /**
     * Private constructor to enforce non-instantiability.
     *
     * <p>
     * All functionality is provided through static methods and the singleton
     * instance {@link #INSTANCE}.
     */
    private TypeAnyInnValidator() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Validates if the given string is a valid taxpayer identification number (INN) of any type.
     *
     * <p>
     * The validation process includes:
     * <ol>
     *   <li>Checking against the individual INN format (12 digits with checksum).</li>
     *   <li>If the first check fails, checking against the organization INN format (10 digits with checksum).</li>
     * </ol>
     * The inn is considered valid if it satisfies at least one of the above checks.
     *
     * @param inn the taxpayer identification number (INN) string to validate, cannot
     *               be null
     * @return {@code true} if the INN is a valid individual or organization INN;
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(final String inn) {
        return TypeOrganizationInnValidator.INSTANCE.isValid(inn)
                || TypeIndividualInnValidator.INSTANCE.isValid(inn);
    }

}
