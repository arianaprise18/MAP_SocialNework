package repository;

import domain.Utilizator;
import domain.validation.Validator;

import java.util.Optional;

public class UtilizatorDbRepository implements Repository<String, Utilizator> {
    private final Validator<Utilizator> validator;
    private final String url;
    private final String user;
    private final String password;

    public UtilizatorDbRepository(Validator<Utilizator> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Utilizator> findOne(String id) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Iterable<Utilizator> findAll() {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Utilizator> delete(String id) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }
}