package ru.tooloolooz.bumazhka.inn;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.tooloolooz.bumazhka.NotValidException;
import ru.tooloolooz.bumazhka.inn.InnValidator.InnType;

public class InnValidatorTest {
    private static Stream<Arguments> validIndividualInn() {
        return Stream.of(
                Arguments.of("773379906731"),
                Arguments.of("103379906723"),
                Arguments.of("013379906780"),
                Arguments.of("776491168166")
            );
    }

    private static Stream<Arguments> invalidIndividualInn() {
        return Stream.of(
                Arguments.of("773379906741"),
                Arguments.of("776491168266"),
                Arguments.of("003379906741"),
                Arguments.of("")
            );
    }

    private static Stream<Arguments> validOrganizationInn() {
        return Stream.of(
                Arguments.of("7707083893"),
                Arguments.of("1007083897"),
                Arguments.of("0107083899"),
                Arguments.of("2309085638")
            );
    }

    private static Stream<Arguments> invalidOrganizationInn() {
        return Stream.of(
                Arguments.of("7707083883"),
                Arguments.of("7708040143"),
                Arguments.of("0008040142"),
                Arguments.of("")
            );
    }

    @ParameterizedTest
    @MethodSource("validIndividualInn")
    void validateTestForValidIndividualInn(String inn) {
        assertThatCode(() -> InnValidator.validate(inn,
                InnType.INDIVIDUAL))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidIndividualInn")
    void invalidTestForValidIndividualInn(String inn) {
        assertThatThrownBy(() -> InnValidator.validate(inn,
                InnType.INDIVIDUAL))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid INN: " + inn);
    }

    @ParameterizedTest
    @MethodSource("validOrganizationInn")
    void validateTestForValidOrganizationInn(String inn) {
        assertThatCode(() -> InnValidator.validate(inn,
                InnType.ORGANIZATION))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidOrganizationInn")
    void invalidTestForValidOrganizationInn(String inn) {
        assertThatThrownBy(() -> InnValidator.validate(inn,
                InnType.ORGANIZATION))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid INN: " + inn);
    }

    @ParameterizedTest
    @MethodSource("invalidOrganizationInn")
    void invalidTestForValidAnyInn(String inn) {
        assertThatThrownBy(() -> InnValidator.validate(inn,
                InnType.ANY))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid INN: " + inn);
    }

    @ParameterizedTest
    @MethodSource("validOrganizationInn")
    void validTestForValidAnyWithOrganizationInn(String inn) {
        assertThatCode(() -> InnValidator.validate(inn))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("validIndividualInn")
    void validTestForValidAnyWithIndividualInn(String inn) {
        assertThatCode(() -> InnValidator.validate(inn))
                .doesNotThrowAnyException();
    }

    @Test
    void validateTestWithNullinn() {
        assertThatThrownBy(
                () -> InnValidator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("INN must be not null");
    }

    @Test
    void validateTestWithNullType() {
        assertThatThrownBy(() -> InnValidator.validate("", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Type must be not null");
    }

    @ParameterizedTest
    @MethodSource("validOrganizationInn")
    void validateIsValidTestForValidOrganizationInn(String inn) {
        assertThatCode(() -> InnValidator.isValid(inn,
                InnType.ORGANIZATION))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("validOrganizationInn")
    void validateIsValidTestForValidAnyInn(String inn) {
        assertThatCode(() -> InnValidator.isValid(inn))
                .doesNotThrowAnyException();
    }
}
