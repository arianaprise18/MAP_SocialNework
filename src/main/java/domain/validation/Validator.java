package domain.validation;

import java.util.Objects;

/**
 * Functional interface for validating domain entities.
 *
 * @param <T> the type of entity to validate
 */
@FunctionalInterface
public interface Validator<T> {

    void validate(T entity);

    /**
     * Returns a composed validator that performs, in sequence, this operation followed by the other.
     *
     * @param other the next validator to apply
     * @return a composed validator
     */
    default Validator<T> andThen(Validator<T> other) {
        Objects.requireNonNull(other);
        return entity -> {
            this.validate(entity);
            other.validate(entity);
        };
    }
}