package repository;

import domain.Entity;
import domain.validation.ValidationException;
import domain.validation.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * File repository using Template Method and Stream API with NIO.2 for persistence.
 *
 * @param <ID> identifier type
 * @param <E>  entity type
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected final String fileName;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);

    private void loadData() {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            IntStream.range(0, lines.size())
                    .filter(idx -> !lines.get(idx).trim().isEmpty())
                    .forEach(idx -> {
                        String line = lines.get(idx);
                        int lineNum = idx + 1;
                        try {
                            E entity = extractEntity(line);
                            validator.validate(entity);
                            super.save(entity);
                        } catch (ValidationException | IllegalArgumentException e) {
                            System.out.println("[AVERTISMENT] Linie corupta la linia " + lineNum + " in " + fileName + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citirea din fisierul " + fileName, e);
        }
    }

    protected void writeToFile() {
        Path path = Paths.get(fileName);
        try {
            if (path.getParent() != null && !Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = StreamSupport.stream(super.findAll().spliterator(), false)
                    .map(this::createEntityAsString)
                    .collect(Collectors.toList());

            Files.write(path, lines);
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scrierea in fisierul " + fileName, e);
        }
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> res = super.save(entity);
        if (res.isEmpty()) {
            writeToFile();
        }
        return res;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> res = super.delete(id);
        res.ifPresent(e -> writeToFile());
        return res;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> res = super.update(entity);
        if (res.isEmpty()) {
            writeToFile();
        }
        return res;
    }
}