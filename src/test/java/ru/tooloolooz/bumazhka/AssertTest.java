package ru.tooloolooz.bumazhka;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.*;

class AssertTest {

    @Test
    void constructorTest() throws NoSuchMethodException {
        Constructor<Assert> constructor = Assert.class.getDeclaredConstructor();
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
