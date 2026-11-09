package service;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import repository.Repository;

import java.util.Optional;

public class PrietenieService implements Service<Tuple<String, String>, Prietenie> {
    private final Repository<Tuple<String, String>, Prietenie> prietenieRepo;
    private final Repository<String, Utilizator> utilizatorRepo;

    public PrietenieService(Repository<Tuple<String, String>, Prietenie> prietenieRepo,
                            Repository<String, Utilizator> utilizatorRepo) {
        this.prietenieRepo = prietenieRepo;
        this.utilizatorRepo = utilizatorRepo;
    }

    public Prietenie save(String email1, String email2) {
        utilizatorRepo.findOne(email1)
                .orElseThrow(() -> new ServiceException("Utilizatorul " + email1 + " nu exista in sistem!"));
        utilizatorRepo.findOne(email2)
                .orElseThrow(() -> new ServiceException("Utilizatorul " + email2 + " nu exista in sistem!"));

        Prietenie friendship = new Prietenie(email1, email2);
        if (prietenieRepo.save(friendship).isPresent()) {
            throw new ServiceException("Relatia de prietenie exista deja!");
        }
        return friendship;
    }

    @Override
    public Prietenie delete(Tuple<String, String> id) {
        return prietenieRepo.delete(id)
                .orElseThrow(() -> new ServiceException("Relatia de prietenie nu exista!"));
    }

    public Prietenie delete(String email1, String email2) {
        String first = email1.compareTo(email2) <= 0 ? email1 : email2;
        String second = email1.compareTo(email2) <= 0 ? email2 : email1;
        return delete(new Tuple<>(first, second));
    }

    @Override
    public Iterable<Prietenie> findAll() {
        return prietenieRepo.findAll();
    }
}