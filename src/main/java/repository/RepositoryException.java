package repository;

/**
 * Unchecked exception thrown for repository and persistence errors.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}