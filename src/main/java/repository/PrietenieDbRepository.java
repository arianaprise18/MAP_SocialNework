package repository;

import domain.Prietenie;
import domain.Tuple;
import domain.validation.Validator;

import java.util.Optional;

public class PrietenieDbRepository implements Repository<Tuple<String, String>, Prietenie> {
    private final Validator<Prietenie> validator;
    private final String url;
    private final String user;
    private final String password;

    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<String, String> id) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Iterable<Prietenie> findAll() {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Prietenie> delete(Tuple<String, String> id) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        throw new UnsupportedOperationException("Not implemented yet for Database persistence.");
    }
}