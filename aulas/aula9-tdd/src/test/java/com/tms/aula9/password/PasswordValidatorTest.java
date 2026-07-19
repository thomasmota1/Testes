package com.tms.aula9.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {
    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "Senha@123, true",
            "Ab@1, false",
            "senha@123, false",
            "SENHA@123, false",
            "Senha@abc, false",
            "Senha1234, false",
            "'Senha @123', false"
    })
    void shouldValidatePasswordRules(String password, boolean expected) {
        if (expected) {
            assertTrue(validator.isValid(password));
        } else {
            assertFalse(validator.isValid(password));
        }
    }

    @org.junit.jupiter.api.Test
    void shouldExplainAllRulesForNullPassword() {
        ValidationResult result = validator.validate(null);

        assertAll(
                () -> assertFalse(result.isValid()),
                () -> assertEquals(5, result.getErrors().size())
        );
    }
}
