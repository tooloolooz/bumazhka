package ru.tooloolooz.bumazhka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrnValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1990199999997",
            "5990199999993"
    })
    void isValidOgrn(String grn) {
        assertTrue(GrnValidator.isValid(grn, GrnValidator.GrnType.OGRN));
        assertThatCode(() -> GrnValidator.validate(grn, GrnValidator.GrnType.OGRN))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "0000000000000",
            "1110011111111",
            "1a11111111111",
            "11a1111111111",
            "11111a1111111",
            "111111a111111",
            "1111111a11111",
            "11111111a1111",
            "111111111a111",
            "1111111111a11",
            "11111111111a1",
            "111111111111a",
            "1990199999999"
    })
    void isInvalidOgrn(String grn) {
        assertFalse(GrnValidator.isValid(grn, GrnValidator.GrnType.OGRN));
        assertThatThrownBy(() -> GrnValidator.validate(grn, GrnValidator.GrnType.OGRN))
                .isInstanceOf(NotValidException.class)
                .hasMessageContaining("Invalid grn:");
    }

    @Test
    void isValidOgrnip() {
        String ogrnip = "333333333333337";
        assertTrue(GrnValidator.isValid(ogrnip, GrnValidator.GrnType.OGRNIP));
        assertThatCode(() -> GrnValidator.validate(ogrnip, GrnValidator.GrnType.OGRNIP))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3",
            "000000000000000",
            "330033333333333",
            "3a3333333333333",
            "33a333333333333",
            "33333a333333333",
            "333333a33333333",
            "3333333a3333333",
            "33333333a333333",
            "333333333a33333",
            "3333333333a3333",
            "33333333333a333",
            "333333333333a33",
            "3333333333333a3",
            "33333333333333a",
            "999019999999999"
    })
    void isInvalidOgrnip(String grn) {
        assertFalse(GrnValidator.isValid(grn, GrnValidator.GrnType.OGRNIP));
        assertThatThrownBy(() -> GrnValidator.validate(grn, GrnValidator.GrnType.OGRNIP))
                .isInstanceOf(NotValidException.class)
                .hasMessageContaining("Invalid grn:");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2220122222220",
            "6660166666660",
            "7770177777770",
            "8880188888880",
            "9990199999990"
    })
    void isValidGrnEgrul(String grn) {
        assertTrue(GrnValidator.isValid(grn, GrnValidator.GrnType.GRN_EGRUL));
        assertThatCode(() -> GrnValidator.validate(grn, GrnValidator.GrnType.GRN_EGRUL))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2",
            "0000000000000",
            "2220022222222",
            "2a22222222222",
            "22a2222222222",
            "22222a2222222",
            "222222a222222",
            "2222222a22222",
            "22222222a2222",
            "222222222a222",
            "2222222222a22",
            "22222222222a2",
            "222222222222a",
            "2990199999999"
    })
    void isInvalidGrnEgrul(String grn) {
        assertFalse(GrnValidator.isValid(grn, GrnValidator.GrnType.GRN_EGRUL));
        assertThatThrownBy(() -> GrnValidator.validate(grn, GrnValidator.GrnType.GRN_EGRUL))
                .isInstanceOf(NotValidException.class)
                .hasMessageContaining("Invalid grn:");
    }

    @Test
    void isValidGrnEgrip() {
        String egrip = "444014444444449";
        assertTrue(GrnValidator.isValid(egrip, GrnValidator.GrnType.GRN_EGRIP));
        assertThatCode(() -> GrnValidator.validate(egrip, GrnValidator.GrnType.GRN_EGRIP))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "4",
            "000000000000044",
            "444004444444444",
            "4a4444444444444",
            "44a444444444444",
            "44444a444444444",
            "444444a44444444",
            "4444444a4444444",
            "44444444a444444",
            "444444444a44444",
            "4444444444a4444",
            "44444444444a444",
            "444444444444a44",
            "4444444444444a4",
            "44444444444444a",
            "999019999999999"
    })
    void isInvalidGrnEgrip(String grn) {
        assertFalse(GrnValidator.isValid(grn, GrnValidator.GrnType.GRN_EGRIP));
        assertThatThrownBy(() -> GrnValidator.validate(grn, GrnValidator.GrnType.GRN_EGRIP))
                .isInstanceOf(NotValidException.class)
                .hasMessageContaining("Invalid grn:");
    }

    @Test
    void isValidWithoutTypeTestWithNullGrn() {
        assertThatThrownBy(() -> GrnValidator.isValid(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Grn must be not null");
    }

    @Test
    void isValidWithTypeTestWithNullGrn() {
        assertThatThrownBy(() -> GrnValidator.isValid(null, GrnValidator.GrnType.OGRN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Grn must be not null");
    }

    @Test
    void isValidWithTypeTestWithNullType() {
        assertThatThrownBy(() -> GrnValidator.isValid("", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Type must be not null");
    }
}
