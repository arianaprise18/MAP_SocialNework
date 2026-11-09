package repository;

import domain.Entity;
import domain.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * CRUD operations repository interface with Java 8 default filter operations.
 *
 * @param <ID> type of identifier
 * @param <E>  type of stored entity
 */
public interface Repository<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> delete(ID id);

    Optional<E> update(E entity);

    /**
     * Default method returning all entities matching a given predicate.
     *
     * @param filter predicate condition
     * @return list of matching entities
     */
    default List<E> findAll(Predicate<E> filter) {
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(filter)
                .collect(Collectors.toList());
    }
}