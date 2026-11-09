package service;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorService implements Service<String, Utilizator> {
    private final Repository<String, Utilizator> utilizatorRepo;
    private final Repository<Tuple<String, String>, Prietenie> prietenieRepo;

    public UtilizatorService(Repository<String, Utilizator> utilizatorRepo,
                             Repository<Tuple<String, String>, Prietenie> prietenieRepo) {
        this.utilizatorRepo = utilizatorRepo;
        this.prietenieRepo = prietenieRepo;
    }

    public Utilizator save(String gmail, String firstName, String lastName) {
        Utilizator user = new Utilizator(gmail, firstName, lastName);
        if (utilizatorRepo.save(user).isPresent()) {
            throw new ServiceException("Exista deja un utilizator inregistrat cu adresa de Gmail: " + gmail);
        }
        return user;
    }

    @Override
    public Utilizator delete(String gmail) {
        Utilizator user = utilizatorRepo.findOne(gmail)
                .orElseThrow(() -> new ServiceException("Utilizatorul cu adresa de Gmail specificata nu exista!"));

        List<Tuple<String, String>> friendTuples = StreamSupport.stream(prietenieRepo.findAll().spliterator(), false)
                .filter(p -> p.getIdUser1().equalsIgnoreCase(gmail) || p.getIdUser2().equalsIgnoreCase(gmail))
                .map(Prietenie::getId)
                .collect(Collectors.toList());

        friendTuples.forEach(prietenieRepo::delete);

        return utilizatorRepo.delete(gmail).orElse(user);
    }

    @Override
    public Iterable<Utilizator> findAll() {
        return utilizatorRepo.findAll();
    }

    public List<Utilizator> filterUsers(Predicate<Utilizator> predicate) {
        return utilizatorRepo.findAll(predicate);
    }
}