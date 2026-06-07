package ru.tooloolooz.bumazhka;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InnValidatorTest {

    private static Stream<Arguments> anyInn() {
        return Stream.of(
                Arguments.of("7707083893", true),
                Arguments.of("7702070139", true),
                Arguments.of("7728168971", true),
                Arguments.of("7728168971", true),
                Arguments.of("1234567890", false),
                Arguments.of("612205661128", true),
                Arguments.of("611105269814", true),
                Arguments.of("611104329669", true),
                Arguments.of("614111131136", true),
                Arguments.of("614133331136", false),
                Arguments.of("611105269819", false),
                Arguments.of("61110526981912", false)
        );
    }

    private static Stream<Arguments> innWithType() {
        return Stream.of(
                Arguments.of("7707083893", InnValidator.InnType.LEGAL_ENTITIES, true),
                Arguments.of("7702070139", InnValidator.InnType.LEGAL_ENTITIES, true),
                Arguments.of("7728168971", InnValidator.InnType.LEGAL_ENTITIES, true),
                Arguments.of("7728168973", InnValidator.InnType.LEGAL_ENTITIES, false),
                Arguments.of("772816897321", InnValidator.InnType.LEGAL_ENTITIES, false),
                Arguments.of("612205661128", InnValidator.InnType.INDIVIDUAL, true),
                Arguments.of("611105269814", InnValidator.InnType.INDIVIDUAL, true),
                Arguments.of("611104329669", InnValidator.InnType.INDIVIDUAL, true),
                Arguments.of("611104329666", InnValidator.InnType.INDIVIDUAL, false),
                Arguments.of("6111043296", InnValidator.InnType.INDIVIDUAL, false),
                Arguments.of("7728168971", InnValidator.InnType.ANY, true),
                Arguments.of("1234567890", InnValidator.InnType.ANY, false),
                Arguments.of("614111131136", InnValidator.InnType.ANY, true),
                Arguments.of("614133331136", InnValidator.InnType.ANY, false),
                Arguments.of("611105269819", InnValidator.InnType.ANY, false)
        );
    }

    @ParameterizedTest
    @MethodSource("anyInn")
    void isValidTest(String inn, boolean result) {
        assertThat(InnValidator.isValid(inn))
                .isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("innWithType")
    void isValidTest(String inn, InnValidator.InnType innType, boolean result) {
        assertThat(InnValidator.isValid(inn, innType))
                .isEqualTo(result);
    }
}
