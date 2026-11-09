package repository;

import domain.Utilizator;
import domain.validation.Validator;

/**
 * File repository implementation for Utilizator.
 */
public class UtilizatorFileRepository extends AbstractFileRepository<String, Utilizator> {

    public UtilizatorFileRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Utilizator extractEntity(String line) {
        String[] tokens = line.split(";", -1);
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Campuri insuficiente pe linie");
        }
        String gmail = tokens[0].trim();
        String firstName = tokens[1].trim();
        String lastName = tokens[2].trim();
        return new Utilizator(gmail, firstName, lastName);
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}