package ru.tooloolooz.bumazhka.inn;

/**
 * Strategy interface for validating taxpayer identification numbers (INN).
 *
 * <p>
 * This is a sealed interface that restricts implementation to specific, recognized
 * taxpayer types within the Russian tax system: Individuals and Organizations.
 *
 * @see TypeIndividualInnValidator
 * @see TypeOrganizationInnValidator
 */
sealed interface TypeInnValidator permits TypeOrganizationInnValidator,
        TypeIndividualInnValidator, TypeAnyInnValidator {
    /**
     * Validates the format and checksum of the given taxpayer identification number (INN).
     *
     * @param inn the taxpayer identification number (INN) string to validate, cannot be null
     * @return {@code true} if the inn conforms to the validation rules of the
     *         specific taxpayer type;
     *         {@code false} otherwise
     */
    boolean isValid(String inn);
}
