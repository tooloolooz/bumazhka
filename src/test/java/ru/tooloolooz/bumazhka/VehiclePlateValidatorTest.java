package ru.tooloolooz.bumazhka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehiclePlateValidatorTest {
    private static Stream<Arguments> validPlates() {
        return Stream.of(
                Arguments.of("А019АА61"),
                Arguments.of("А019АА161")
        );
    }

    private static Stream<Arguments> invalidPlates() {
        return Stream.of(
                Arguments.of("а019АА161"),
                Arguments.of("Аа19АА161"),
                Arguments.of("А0а9АА161"),
                Arguments.of("А01аАА161"),
                Arguments.of("А019аА161"),
                Arguments.of("А019Аа161"),
                Arguments.of("А019ААа61"),
                Arguments.of("А019АА1а1"),
                Arguments.of("А019АА16а"),
                Arguments.of("AА019АА161"),
                Arguments.of("А019АА161A"),
                Arguments.of("А019АА1"),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("validPlates")
    void validateTestForValidLicensePlates(String code) {
        assertThatCode(() -> VehiclePlateValidator.validate(code))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("validPlates")
    void validateTestForValidLicensePlatesType1(String code) {
        assertThatCode(() -> VehiclePlateValidator.validate(code, VehiclePlateValidator.PlateType.TYPE_1))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("validPlates")
    void validateTestForValidLicensePlatesType1A(String code) {
        assertThatCode(() -> VehiclePlateValidator.validate(code, VehiclePlateValidator.PlateType.TYPE_1A))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidPlates")
    void validateTestForInvalidLicensePlates(String code) {
        assertThatThrownBy(() -> VehiclePlateValidator.validate(code))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid vehicle state registration plate: " + code);
    }

    @ParameterizedTest
    @MethodSource("invalidPlates")
    void validateTestForInvalidLicensePlatesForType1(String code) {
        assertThatThrownBy(() -> VehiclePlateValidator.validate(code, VehiclePlateValidator.PlateType.TYPE_1))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid vehicle state registration plate: " + code);
    }

    @Test
    void validateTestForNullCode() {
        assertThatThrownBy(() -> VehiclePlateValidator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Plate must be not null");
    }

    @Test
    void validateTestForNullCodeType1() {
        assertThatThrownBy(() -> VehiclePlateValidator.validate(null, VehiclePlateValidator.PlateType.TYPE_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Plate must be not null");
    }

    @Test
    void validateTestForNullType() {
        assertThatThrownBy(() -> VehiclePlateValidator.validate("", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Type must be not null");
    }
}
