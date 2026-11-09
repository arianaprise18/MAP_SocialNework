package domain.validation;

/**
 * Unchecked exception thrown when domain validation rules are violated
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}