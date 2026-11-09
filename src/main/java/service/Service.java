package service;

/**
 * Common standard operations for business services.
 *
 * @param <ID> identifier type
 * @param <E>  entity type
 */
public interface Service<ID, E> {

    E delete(ID id);
    Iterable<E> findAll();
}