package com.tms.aula9.password;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    public boolean isValid(String password) {
        return validate(password).isValid();
    }

    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.length() < 8) {
            errors.add("Senha deve ter pelo menos 8 caracteres");
        }
        if (password != null && password.matches(".*\\s.*")) {
            errors.add("Senha nao pode conter espacos");
        }
        if (password == null || !password.matches(".*[A-Z].*")) {
            errors.add("Senha deve conter letra maiuscula");
        }
        if (password == null || !password.matches(".*[a-z].*")) {
            errors.add("Senha deve conter letra minuscula");
        }
        if (password == null || !password.matches(".*[0-9].*")) {
            errors.add("Senha deve conter numero");
        }
        if (password == null || !password.matches(".*[@#$%^&+=!].*")) {
            errors.add("Senha deve conter caractere especial");
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }
}
