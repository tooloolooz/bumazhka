package ru.tooloolooz.bumazhka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehicleRegionCodeValidatorTest {

    /**
     * Test data provider for region code validation tests.
     * Provides region codes with their expected validation results.
     *
     * @return Stream of Arguments where:
     * - First argument (String): region code to test
     * - Second argument (Boolean): expected validation result (true = valid, false = invalid)
     */
    private static Stream<Arguments> regionCodesWithoutLength() {
        return Stream.of(
                Arguments.of("01", true),
                Arguments.of("101", true),
                Arguments.of("901", true),
                Arguments.of("1", false),
                Arguments.of("1111", false),
                Arguments.of("a", false),
                Arguments.of("001", false),
                Arguments.of("a01", false),
                Arguments.of("00", false),
                Arguments.of("100", false),
                Arguments.of("900", false)
        );
    }

    /**
     * Test data provider for region code validation tests.
     * Provides region codes with their expected validation results.
     *
     * @return Stream of Arguments where:
     * - First argument (String): region code to test
     * - Second argument (Enum): length to test
     * - Third argument (Boolean): expected validation result (true = valid, false = invalid)
     */
    private static Stream<Arguments> regionCodesWithLength() {
        return Stream.of(
                Arguments.of("01", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, true),
                Arguments.of("101", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("901", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("1", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("1111", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("a", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("001", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("a01", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("00", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("100", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),
                Arguments.of("900", VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT, false),

                Arguments.of("01", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("101", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, true),
                Arguments.of("901", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, true),
                Arguments.of("1", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("1111", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("a", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("001", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("a01", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("00", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("100", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),
                Arguments.of("900", VehicleRegionCodeValidator.RegionCodeLength.THREE_DIGIT, false),

                Arguments.of("01", VehicleRegionCodeValidator.RegionCodeLength.ANY, true),
                Arguments.of("101", VehicleRegionCodeValidator.RegionCodeLength.ANY, true),
                Arguments.of("901", VehicleRegionCodeValidator.RegionCodeLength.ANY, true),
                Arguments.of("1", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("1111", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("a", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("001", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("a01", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("00", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("100", VehicleRegionCodeValidator.RegionCodeLength.ANY, false),
                Arguments.of("900", VehicleRegionCodeValidator.RegionCodeLength.ANY, false)
        );
    }

    @Test
    void constructorTest() throws NoSuchMethodException {
        Constructor<VehicleRegionCodeValidator> constructor = VehicleRegionCodeValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThat(Modifier.isPrivate(constructor.getModifiers()))
                .isTrue();
        assertThat(constructor.getParameterCount())
                .isZero();
        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .extracting(Throwable::getCause)
                .isInstanceOf(UnsupportedOperationException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("Utility class should not be instantiated");
    }

    @ParameterizedTest
    @MethodSource("regionCodesWithoutLength")
    void isValidTest(String code, boolean result) {
        assertThat(VehicleRegionCodeValidator.isValid(code))
                .isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("regionCodesWithLength")
    void isValidTest(String code, VehicleRegionCodeValidator.RegionCodeLength length, boolean result) {
        assertThat(VehicleRegionCodeValidator.isValid(code, length))
                .isEqualTo(result);
    }

    @Test
    @SuppressWarnings("NullAway")
    void isValidWithoutLengthTestWithNullCode() {
        assertThatThrownBy(() -> VehicleRegionCodeValidator.isValid(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Code must be not null");
    }

    @Test
    @SuppressWarnings("NullAway")
    void isValidWithLengthTestWithNullCode() {
        assertThatThrownBy(() -> VehicleRegionCodeValidator.isValid(null, VehicleRegionCodeValidator.RegionCodeLength.TWO_DIGIT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Code must be not null");
    }

    @Test
    @SuppressWarnings("NullAway")
    void isValidWithLengthTestWithNullLength() {
        assertThatThrownBy(() -> VehicleRegionCodeValidator.isValid("01", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length must be not null");
    }
}
