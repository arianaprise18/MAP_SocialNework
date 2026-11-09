package domain.validation;

import domain.Utilizator;

import java.util.function.Predicate;

/**
 * Utility predicates for filtering users according to criteria
 */
public final class CriteriiUtilizator {

    private CriteriiUtilizator() {
    }

    /**
     * Filter predicate matching users with a given last name.
     *
     * @param lastName the last name to match
     * @return predicate
     */
    public static Predicate<Utilizator> areNumele(String lastName) {
        return user -> user.getLastName().equalsIgnoreCase(lastName);
    }

    /**
     * Filter predicate matching users whose first name starts with a specific prefix.
     *
     * @param prefix the string prefix
     * @return predicate
     */
    public static Predicate<Utilizator> prenumeIncepeCu(String prefix) {
        return user -> user.getFirstName().startsWith(prefix);
    }

    /**
     * Filter predicate matching users whose email identifier contains a keyword.
     *
     * @param keyword domain or name keyword
     * @return predicate
     */
    public static Predicate<Utilizator> emailContine(String keyword) {
        return user -> user.getId().toLowerCase().contains(keyword.toLowerCase());
    }
}