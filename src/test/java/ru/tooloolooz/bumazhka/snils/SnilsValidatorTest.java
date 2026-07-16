package ru.tooloolooz.bumazhka.snils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tooloolooz.bumazhka.NotValidException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnilsValidatorTest {

    private static Stream<Arguments> validSnils() {
        return Stream.of(
                Arguments.of("112-233-445 95"),
                Arguments.of("123-456-789 64"),
                Arguments.of("336-134-414 47"),
                Arguments.of("123-412-340 00"),
                Arguments.of("123-412-341 00"),
                Arguments.of("665-543-100 00"),
                Arguments.of("001-001-001 00"),
                Arguments.of("001-001-500 99"),
                Arguments.of("001-001-998 00")
        );
    }

    private static Stream<Arguments> invalidSnils() {
        return Stream.of(
                Arguments.of("000-000-000 00"),
                Arguments.of("920-000-003 00"),
                Arguments.of("000-000-001 00"),
                Arguments.of("112-233-445 96"),
                Arguments.of("123-456-789 00"),
                Arguments.of("010-020-030 12"),
                Arguments.of("12345678901"),
                Arguments.of("123_456_789 01"),
                Arguments.of("123-456-78A 01"),
                Arguments.of("123-456-789 0A"),
                Arguments.of("12-345-6789 01"),
                Arguments.of(""),
                Arguments.of("123-456-789 0")
        );
    }

    @ParameterizedTest
    @MethodSource("validSnils")
    void validateTestForValidSnils(String snils) {
        assertThatCode(() -> SnilsValidator.validate(snils))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidSnils")
    void validateTestForInvalidSnils(String snils) {
        assertThatThrownBy(() -> SnilsValidator.validate(snils))
                .isInstanceOf(NotValidException.class)
                .hasMessage("Invalid snils: " + snils);
    }

    @Test
    void validateTestForNullSnils() {
        assertThatThrownBy(() -> SnilsValidator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Snils must be not null");
    }
}