package ru.tooloolooz.bumazhka.plate;

import ru.tooloolooz.bumazhka.VehiclePlateValidator;

/**
 * Composite validator that delegates validation to all available plate type validators.
 * <p>
 * This validator implements the {@link VehiclePlateValidator.PlateType#ANY} type validation
 * strategy, where a plate is considered valid if it passes validation by <b>any</b> of
 * the registered plate type validators.
 * <p>
 * <b>Validation Strategy:</b>
 * The validator uses logical OR semantics - a plate is valid if it matches
 * <b>ANY</b> of the supported formats.
 *
 * @see PlateValidator
 * @see VehiclePlateValidator.PlateType#ANY
 */
public final class TypeAnyPlateValidator implements PlateValidator {
    /**
     * Singleton instance of the Type Any vehicle plate validator.
     * <p>
     * Use this instance for all implemented validations to ensure consistency
     * and avoid unnecessary object instantiation.
     */
    public static final TypeAnyPlateValidator INSTANCE = new TypeAnyPlateValidator();

    /**
     * Private constructor to enforce non-instantiability.
     * <p>
     * All functionality is provided through static methods and the singleton instance {@link #INSTANCE}.
     */
    private TypeAnyPlateValidator() {
        // Private constructor to prevent instantiation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String plate) {
        return Type1PlateValidator.INSTANCE.isValid(plate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VehiclePlateValidator.PlateType getType() {
        return VehiclePlateValidator.PlateType.ANY;
    }
}
