package domain;

/**
 * Entity representing a user identified uniquely by a Gmail address
 */
public class Utilizator extends Entity<String> {
    private String firstName;
    private String lastName;

    /**
     * Constructs a Utilizator instance.
     *
     * @param gmail     unique Gmail address of the user
     * @param firstName first name
     * @param lastName  last name
     */
    public Utilizator(String gmail, String firstName, String lastName) {
        setId(gmail);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "gmail='" + getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}