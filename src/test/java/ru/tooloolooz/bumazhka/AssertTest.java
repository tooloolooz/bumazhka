package ru.tooloolooz.bumazhka;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertTest {

    @Test
    void unsupportedTest() {
        String message = "message";
        assertThatThrownBy(() -> Assert.unsupported(message))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(message);
    }

    @Test
    void notNullTestWithNull() {
        String message = "message";
        assertThatThrownBy(() -> Assert.notNull(null, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    void notNullTestWithoutNull() {
        String message = "message";
        assertThatCode(() -> Assert.notNull(new Object(), message))
                .doesNotThrowAnyException();
    }
}
