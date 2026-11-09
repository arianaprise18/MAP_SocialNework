package domain.validation;

import domain.Utilizator;

/**
 * Validator implementation for Utilizator entities.
 */
public class UtilizatorValidator implements Validator<Utilizator> {

    @Override
    public void validate(Utilizator entity) {
        if (entity == null) {
            throw new ValidationException("Utilizatorul nu poate fi null!");
        }

        StringBuilder errors = new StringBuilder();

        String email = entity.getId();
        if (email == null || email.trim().isEmpty()) {
            errors.append("Adresa de Gmail nu poate fi vida! ");
        } else {
            String trimmedEmail = email.trim();
            if (!trimmedEmail.matches("^[a-zA-Z0-9.]+@gmail\\.com$")) {
                errors.append("Adresa de email trebuie sa aiba domeniul @gmail.com! ");
            }
        }

        if (entity.getFirstName() == null || entity.getFirstName().trim().isEmpty()) {
            errors.append("Prenumele nu poate fi vid! ");
        } else {
            String firstName = entity.getFirstName().trim();
            if (!Character.isUpperCase(firstName.charAt(0))) {
                errors.append("Prenumele trebuie sa inceapa cu litera mare! ");
            }
            if (!firstName.matches("[a-zA-Z]+")) {
                errors.append("Prenumele trebuie sa contina doar litere! ");
            }
        }

        if (entity.getLastName() == null || entity.getLastName().trim().isEmpty()) {
            errors.append("Numele nu poate fi vid! ");
        } else {
            String lastName = entity.getLastName().trim();
            if (!Character.isUpperCase(lastName.charAt(0))) {
                errors.append("Numele trebuie sa inceapa cu litera mare! ");
            }
            if (!lastName.matches("[a-zA-Z]+")) {
                errors.append("Numele trebuie sa contina doar litere! ");
            }
        }

        if (errors.length() > 0) {
            throw new ValidationException(errors.toString().trim());
        }
    }
}