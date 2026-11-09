import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import domain.validation.PrietenieValidator;
import domain.validation.UtilizatorValidator;
import domain.validation.ValidationException;
import domain.validation.Validator;
import repository.Repository;
import repository.RepositoryFactory;
import service.ComunitatiService;
import service.PrietenieService;
import service.UtilizatorService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Validator<Utilizator> customRule = u -> {
            if (u.getFirstName().length() < 2) {
                throw new ValidationException("Prenumele trebuie sa aiba cel putin 2 caractere!");
            }
        };

        Validator<Utilizator> userValidator = new UtilizatorValidator().andThen(customRule);
        Validator<Prietenie> friendshipValidator = new PrietenieValidator();

        RepositoryFactory factory = new RepositoryFactory();
        Repository<String, Utilizator> userRepo = factory.createUtilizatorRepository(userValidator);
        Repository<Tuple<String, String>, Prietenie> friendshipRepo = factory.createPrietenieRepository(friendshipValidator);

        UtilizatorService utilizatorService = new UtilizatorService(userRepo, friendshipRepo);
        PrietenieService prietenieService = new PrietenieService(friendshipRepo, userRepo);
        ComunitatiService comunitatiService = new ComunitatiService(userRepo, friendshipRepo);

        ConsoleUI consoleUI = new ConsoleUI(utilizatorService, prietenieService, comunitatiService);
        consoleUI.start();
    }
}