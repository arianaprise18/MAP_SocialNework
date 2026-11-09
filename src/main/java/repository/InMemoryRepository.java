package repository;

import domain.Entity;
import domain.validation.ValidationException;
import domain.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Generic in-memory repository implementation using a hash map and Optional return types.
 *
 * @param <ID> type of identifier
 * @param <E>  type of entity
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected final Validator<E> validator;
    protected final Map<ID, E> entities = new HashMap<>();

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }
        validator.validate(entity);
        if (entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}