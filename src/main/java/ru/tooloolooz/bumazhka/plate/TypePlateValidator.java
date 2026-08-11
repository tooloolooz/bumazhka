package ru.tooloolooz.bumazhka.plate;

/**
 * Base interface for all vehicle registration plate validators.
 *
 * <p>
 * Every plate validator must implement this interface. Each concrete validator is
 * responsible for validating a specific {@link VehiclePlateValidator.PlateType}
 * of Russian Federation vehicle registration plates.
 *
 * @see VehiclePlateValidator.PlateType
 */
sealed interface TypePlateValidator permits TypeAnyPlateValidator, Type1PlateValidator {
    /**
     * Validates a vehicle registration plate according to the specific format rules.
     *
     * <p>
     * This method checks if the plate conforms to the specified format type only.
     *
     * @param plate the registration plate string to validate.
     * @return {@code true} if the {@code plate} is valid, {@code false} otherwise.
     */
    boolean isValid(String plate);
}
