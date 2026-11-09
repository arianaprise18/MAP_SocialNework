package domain;

import java.time.LocalDateTime;

/**
 * Entity representing a friendship relationship between two users.
 */
public class Prietenie extends Entity<Tuple<String, String>> {
    private final LocalDateTime date;

    /**
     * Constructs a Prietenie with the current date and time.
     *
     * @param email1 Gmail of first user
     * @param email2 Gmail of second user
     */
    public Prietenie(String email1, String email2) {
        this(email1, email2, LocalDateTime.now());
    }

    public Prietenie(String email1, String email2, LocalDateTime date) {
        String first = email1.compareTo(email2) <= 0 ? email1 : email2;
        String second = email1.compareTo(email2) <= 0 ? email2 : email1;
        setId(new Tuple<>(first, second));
        this.date = date;
    }

    public String getIdUser1() {
        return getId().getLeft();
    }

    public String getIdUser2() {
        return getId().getRight();
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "u1='" + getIdUser1() + '\'' +
                ", u2='" + getIdUser2() + '\'' +
                ", date=" + date +
                '}';
    }
}