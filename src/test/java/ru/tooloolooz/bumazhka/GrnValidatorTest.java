package ru.tooloolooz.bumazhka;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GrnValidatorTest {

    /**
     * Provides strings with invalid EGRIP numbers
     * containing a non-digit character at a specific position.
     * Generated string format: 15 characters where:
     * First character: '3' or '4' (valid values for EGRIP)
     * Characters 4-5: always "99" (valid region code)
     * Characters 2-3 and 6-15: digits '0', except for one position
     */
    @Provide
    Arbitrary<String> egripGrnWithNonDigit() {
        return Combinators.combine(
                Arbitraries.of('3', '4'),
                Arbitraries.integers()
                        .between(1, 14)
                        .filter(integer -> integer != 0 && integer != 3 && integer != 4)
        ).as((firstChar, index) -> {
            char[] chars = new char[]{
                    firstChar,
                    '0', '0',
                    '9', '9',
                    '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
            };
            chars[index] = 'a';
            return new String(chars);
        });
    }

    /**
     * Provides strings with invalid EGRUL numbers
     * containing a non-digit character at a specific position.
     * Generated string format: 15 characters where:
     * First character: '1', '2', '5', '6', '7', '8', '9' (valid values for EGRUL)
     * Characters 4-5: always "99" (valid region code)
     * Characters 2-3 and 6-13: digits '0', except for one position
     */
    @Provide
    Arbitrary<String> egrulGrnWithNonDigit() {
        return Combinators.combine(
                Arbitraries.of('1', '2', '5', '6', '7', '8', '9'),
                Arbitraries.integers()
                        .between(0, 12)
                        .filter(integer -> integer != 0 && integer != 3 && integer != 4)
        ).as((firstChar, index) -> {
            char[] chars = new char[]{
                    firstChar,
                    '0', '0',
                    '9', '9',
                    '0', '0', '0', '0', '0', '0', '0', '0'
            };
            chars[index] = 'a';
            return new String(chars);
        });
    }

    @Provide
    Arbitrary<String> grnWithInvalidLength() {
        return Arbitraries.strings()
                .numeric()
                .ofMinLength(0)
                .ofMaxLength(20)
                .filter(str -> str.length() != 13 && str.length() != 15);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1009900000009",
            "5009900000009",
            "2009900000009",
            "6009900000009",
            "7009900000009",
            "8009900000009",
            "9009900000009",
            "300990000000000",
            "400990000000000"
    })
    void grnWithInvalidChecksum(String grn) {
        assertThat(GrnValidator.isValid(grn))
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1000000000000",
            "5000000000000",
            "2000000000000",
            "6000000000000",
            "7000000000000",
            "8000000000000",
            "9000000000000",
            "300000000000000",
            "400000000000000"
    })
    void grnWithInvalidRegion(String grn) {
        assertThat(GrnValidator.isValid(grn))
                .isFalse();
    }

    @Property
    void grnWithInvalidLength(@ForAll("grnWithInvalidLength") String grn) {
        assertThat(GrnValidator.isValid(grn))
                .isFalse();
    }

    @Property
    void egripGrnWithNonDigit(@ForAll("egripGrnWithNonDigit") String grn) {
        assertThat(GrnValidator.isValid(grn))
                .isFalse();
    }

    @Property
    void egrulGrnWithNonDigit(@ForAll("egrulGrnWithNonDigit") String grn) {
        assertThat(GrnValidator.isValid(grn))
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1009900000000",
            "5009900000004",
            "2009900000001",
            "6009900000005",
            "7009900000006",
            "8009900000007",
            "9009900000008",
            "300990000000007",
            "400990000000008"
    })
    void validGrnAnyType(String grn) {
        assertThat(GrnValidator.isValid(grn, GrnValidator.GrnType.ANY))
                .isTrue();
    }

    @ValueSource(strings = {
            "1009900000000",
            "5009900000004"
    })
    void validOgrn(String ogrn) {
        assertThat(GrnValidator.isValid(ogrn, GrnValidator.GrnType.OGRN))
                .isTrue();
    }

    void validOgrnip() {
        assertThat(GrnValidator.isValid("300990000000007", GrnValidator.GrnType.OGRNIP))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2009900000001",
            "6009900000005",
            "7009900000006",
            "8009900000007",
            "9009900000008",
    })
    void validGrnEgrul(String grnEgrul) {
        assertThat(GrnValidator.isValid(grnEgrul, GrnValidator.GrnType.GRN_EGRUL))
                .isTrue();
    }

    void validGrnEgrip() {
        assertThat(GrnValidator.isValid("400990000000008", GrnValidator.GrnType.GRN_EGRIP))
                .isTrue();
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
