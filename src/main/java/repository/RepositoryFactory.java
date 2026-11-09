package repository;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import domain.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

/**
 * Factory implementing strategy mappings via Functional Interfaces without switch statements.
 * Validates all required properties strictly to prevent incomplete configurations.
 */
public class RepositoryFactory {
    private final RepositoryType repoType;
    private final Properties properties = new Properties();

    private final Map<RepositoryType, Function<Validator<Utilizator>, Repository<String, Utilizator>>> userCreators = new EnumMap<>(RepositoryType.class);
    private final Map<RepositoryType, Function<Validator<Prietenie>, Repository<Tuple<String, String>, Prietenie>>> friendshipCreators = new EnumMap<>(RepositoryType.class);

    public RepositoryFactory() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RepositoryException("Fisierul config.properties nu a fost gasit in classpath!");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RepositoryException("Eroare la incarcarea fisierului config.properties", e);
        }

        String typeStr = getRequiredProperty("repository.type").toUpperCase();

        try {
            this.repoType = RepositoryType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Tip de repository necunoscut: " + typeStr, e);
        }

        initCreators();
    }

    /**
     * Extracts a configuration property and validates that it is neither missing nor blank.
     *
     * @param key the property key to look up
     * @return the trimmed property value
     * @throws RepositoryException if the property is missing or empty
     */
    private String getRequiredProperty(String key) {
        return Optional.ofNullable(properties.getProperty(key))
                .map(String::trim)
                .filter(val -> !val.isEmpty())
                .orElseThrow(() -> new RepositoryException("Proprietatea obligatorie lipseste sau este vida: " + key));
    }

    private void initCreators() {

        userCreators.put(RepositoryType.MEMORY, InMemoryRepository::new);
        userCreators.put(RepositoryType.FILE, val -> {
            String filePath = getRequiredProperty("file.utilizatori");
            return new UtilizatorFileRepository(val, filePath);
        });
        userCreators.put(RepositoryType.DATABASE, val -> {
            String url = getRequiredProperty("db.url");
            String user = getRequiredProperty("db.user");
            String password = getRequiredProperty("db.password");
            return new UtilizatorDbRepository(val, url, user, password);
        });

        // Creatori pentru Prietenie
        friendshipCreators.put(RepositoryType.MEMORY, InMemoryRepository::new);
        friendshipCreators.put(RepositoryType.FILE, val -> {
            String filePath = getRequiredProperty("file.prietenii");
            return new PrietenieFileRepository(val, filePath);
        });
        friendshipCreators.put(RepositoryType.DATABASE, val -> {
            String url = getRequiredProperty("db.url");
            String user = getRequiredProperty("db.user");
            String password = getRequiredProperty("db.password");
            return new PrietenieDbRepository(val, url, user, password);
        });
    }

    public Repository<String, Utilizator> createUtilizatorRepository(Validator<Utilizator> validator) {
        return Optional.ofNullable(userCreators.get(repoType))
                .map(creator -> creator.apply(validator))
                .orElseThrow(() -> new RepositoryException("Tip de persistenta nesuportat: " + repoType));
    }

    public Repository<Tuple<String, String>, Prietenie> createPrietenieRepository(Validator<Prietenie> validator) {
        return Optional.ofNullable(friendshipCreators.get(repoType))
                .map(creator -> creator.apply(validator))
                .orElseThrow(() -> new RepositoryException("Tip de persistenta nesuportat: " + repoType));
    }
}