package ru.tooloolooz.bumazhka.plate;

import ru.tooloolooz.bumazhka.VehiclePlateValidator;

/**
 * Base interface for all vehicle registration plate validators.
 * <p>
 * Every plate validator must implement this interface. Each concrete validator is
 * responsible for validating a specific {@link VehiclePlateValidator.PlateType}
 * of Russian Federation vehicle registration plates.
 *
 * @see VehiclePlateValidator.PlateType
 */
public sealed interface PlateValidator permits TypeAnyPlateValidator, Type1PlateValidator {
    /**
     * Validates a vehicle registration plate according to the specific format rules.
     * <p>
     * This method checks if the plate conforms to the specified format type only.
     *
     * @param plate the registration plate string to validate.
     * @return {@code true} if the {@code plate} is valid, {@code false} otherwise.
     */
    boolean isValid(String plate);

    /**
     * Returns the vehicle plate type which this validator is designed to validate.
     * <p>
     * This method allows clients to determine which specific plate format this
     * validator supports. It enables type-safe validation and proper routing
     * in validation pipelines.
     *
     * @return {@link VehiclePlateValidator.PlateType} which this validator instance supports.
     */
    VehiclePlateValidator.PlateType getType();
}
