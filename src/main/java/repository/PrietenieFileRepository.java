package repository;

import domain.Prietenie;
import domain.Tuple;
import domain.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * File repository implementation for Prietenie.
 */
public class PrietenieFileRepository extends AbstractFileRepository<Tuple<String, String>, Prietenie> {

    public PrietenieFileRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Prietenie extractEntity(String line) {
        String[] tokens = line.split(";", -1);
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Campuri insuficiente pentru prietenie");
        }
        String email1 = tokens[0].trim();
        String email2 = tokens[1].trim();
        try {
            LocalDateTime date = LocalDateTime.parse(tokens[2].trim());
            return new Prietenie(email1, email2, date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data prieteniei este invalida", e);
        }
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getIdUser1() + ";" + entity.getIdUser2() + ";" + entity.getDate().toString();
    }
}