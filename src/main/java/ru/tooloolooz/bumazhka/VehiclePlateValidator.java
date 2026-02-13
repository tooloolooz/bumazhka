package ru.tooloolooz.bumazhka;

import ru.tooloolooz.bumazhka.plate.PlateValidator;
import ru.tooloolooz.bumazhka.plate.Type1PlateValidator;
import ru.tooloolooz.bumazhka.plate.TypeAnyPlateValidator;

/**
 * Utility class for validating Russian Federation vehicle registration plates (license plates).
 * <p>
 * This class provides validation for state registration plates according to Russian GOST R 50577-2018 standard.
 * <p>
 * <b>It supports next formats:</b>
 * <ul>
 *   <li><b>Type 1/1A</b></li>
 * </ul>
 * The class provides both automatic plate type detection and explicit type-based validation.
 * <p>
 * Use {@link #validate(String)} or {@link #validate(String, PlateType)} in case of exception-throwing
 * validation. Use {@link #isValid(String)} or {@link #isValid(String, PlateType)} in case of
 * default true/false validation.
 * <p>
 * Implementation uses separate validator instances for each plate type,
 * ensuring compliance with formal GOST requirements.
 * <p>
 * <b>Thread Safety:</b>
 * This validator is thread-safe as it contains no mutable state and all
 * validation logic uses only method parameters and immutable constants.
 *
 * @see PlateType
 * @see NotValidException
 * @see PlateValidator
 * @see <a href="https://docs.cntd.ru/document/1200160380">GOST R 50577-2018 State Registration Plates for Vehicles</a>
 */
public final class VehiclePlateValidator {
    /**
     * This class is a utility class and should not be instantiated.
     *
     * @throws UnsupportedOperationException always.
     */
    private VehiclePlateValidator() {
        Assert.unsupported("Utility class should not be instantiated");
    }

    /**
     * Validates any vehicle registration plate.
     * <p>
     * This is the exception-throwing variant of {@link #isValid(String)}.
     *
     * @param plate the registration plate string to validate.
     * @throws IllegalArgumentException if {@code plate} is {@code null}.
     * @throws NotValidException        if {@code plate} is invalid.
     * @see #isValid(String)
     */
    public static void validate(final String plate) {
        if (!isValid(plate)) {
            throw new NotValidException("Invalid vehicle state registration plate: " + plate);
        }
    }

    /**
     * Validates a vehicle registration plate against a specific type.
     * <p>
     * This is the exception-throwing variant of {@link #isValid(String, PlateType)}.
     *
     * @param plate the registration plate string to validate.
     * @param type  the required {@link PlateType} format.
     * @throws IllegalArgumentException if {@code plate} or {@code type} is {@code null}.
     * @throws NotValidException        if {@code plate} is invalid.
     * @see #isValid(String, PlateType)
     */
    public static void validate(final String plate, final PlateType type) {
        if (!isValid(plate, type)) {
            throw new NotValidException("Invalid vehicle state registration plate: " + plate);
        }
    }

    /**
     * Validates any vehicle registration plate.
     * <p>
     * This method checks the plate against all supported formats.
     * <p>
     * The validation includes: length check and character pattern validation.
     *
     * @param plate the registration plate string to validate
     * @return {@code true} if {@code plate} is valid, {@code false} otherwise
     * @throws IllegalArgumentException if {@code plate} is {@code null}.
     */
    public static boolean isValid(final String plate) {
        Assert.notNull(plate, "Plate must be not null");

        return getValidator(PlateType.ANY).isValid(plate);
    }

    /**
     * Validates a vehicle registration plate against a specific type.
     * <p>
     * This method checks if the plate conforms to the specified format type only.
     * Use this method when you know the expected plate type.
     * <p>
     * The validation includes: length check and character pattern validation.
     *
     * @param plate the registration plate string to validate.
     * @param type  vehicle state registration plate {@link PlateType type}.
     * @return {@code true} if {@code plate} is valid, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code plate} or {@code type} is {@code null}.
     */
    public static boolean isValid(final String plate, final PlateType type) {
        Assert.notNull(plate, "Plate must be not null");
        Assert.notNull(type, "Type must be not null");

        return getValidator(type).isValid(plate);
    }

    /**
     * Returns the appropriate validator instance for the specified plate type.
     * <p>
     * This internal method provides the strategy pattern implementation for plate validation.
     *
     * @param type the vehicle plate type
     * @return the validator instance for the specified type
     */
    private static PlateValidator getValidator(final PlateType type) {
        return switch (type) {
            case TYPE_1, TYPE_1A -> Type1PlateValidator.INSTANCE;
            case ANY -> TypeAnyPlateValidator.INSTANCE;
        };
    }

    /**
     * Enumeration representing different types of Russian Federation vehicle registration plates.
     * <p>
     * Each type corresponds to a specific format and use case defined by GOST R 50577-2018.
     * The types differ in their structural format and allowed characters or digits.
     *
     * @see <a href="https://docs.cntd.ru/document/1200160380">GOST R 50577-2018 State Registration Plates
     * for Vehicles</a>
     */
    public enum PlateType {
        /**
         * 1 type of vehicle registration plate.
         * <p>
         * Format: {@code М000ММ55} or {@code М000ММ555}
         * Where:
         * <ul>
         *   <li>0 - digit indicating the number.</li>
         *   <li>M - letter indicating the series.</li>
         *   <li>5 - digit of the region code.</li>
         * </ul>
         *
         * <b>Allowed Cyrillic letters (12 total) for series:</b>
         * 'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'
         */
        TYPE_1,

        /**
         * 1A type of vehicle registration plate. The type format has no differences from the {@link #TYPE_1}.
         * <p>
         * Is used by vehicles with a non-standard attachment point.
         */
        TYPE_1A,

        /**
         * Any type of vehicle registration plate. The type format includes all checks from all types.
         */
        ANY
    }
}
