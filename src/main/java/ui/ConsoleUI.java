package ui;

import domain.Prietenie;
import domain.Utilizator;
import domain.validation.CriteriiUtilizator;
import domain.validation.ValidationException;
import repository.RepositoryException;
import service.ComunitatiService;
import service.PrietenieService;
import service.ServiceException;
import service.UtilizatorService;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class ConsoleUI {
    private final UtilizatorService utilizatorService;
    private final PrietenieService prietenieService;
    private final ComunitatiService comunitatiService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(UtilizatorService utilizatorService,
                     PrietenieService prietenieService,
                     ComunitatiService comunitatiService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.comunitatiService = comunitatiService;
    }

    public void start() {
        while (true) {
            printMenu();
            System.out.print("Alegeti optiunea: ");
            String opt = scanner.nextLine().trim();

            try {
                if ("1".equals(opt)) uiAddUtilizator();
                else if ("2".equals(opt)) uiRemoveUtilizator();
                else if ("3".equals(opt)) uiAddPrietenie();
                else if ("4".equals(opt)) uiRemovePrietenie();
                else if ("5".equals(opt)) uiShowUtilizatori();
                else if ("6".equals(opt)) uiShowPrietenii();
                else if ("7".equals(opt)) uiNrComunitati();
                else if ("8".equals(opt)) uiCeaMaiSociabilaComunitate();
                else if ("9".equals(opt)) uiFiltrareUtilizatori();
                else if ("0".equals(opt)) {
                    System.out.println("Final");
                    return;
                } else System.out.println("Optiune invalida! Incercati din nou.");
            } catch (ValidationException | ServiceException | RepositoryException | IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\nMENIU");
        System.out.println("1. Adauga utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println("3. Adauga prieten");
        System.out.println("4. Sterge prieten");
        System.out.println("5. Afiseaza utilizatori");
        System.out.println("6. Afiseaza prietenii");
        System.out.println("7. Numar de comunitati");
        System.out.println("8. Cea mai sociabila comunitate");
        System.out.println("9. Filtreaza utilizatori (Demo Lambda & Predicate)");
        System.out.println("0. Iesire");
    }

    private void uiAddUtilizator() {
        System.out.print("Introduceti adresa de Gmail: ");
        String gmail = scanner.nextLine().trim();
        System.out.print("Introduceti prenumele: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Introduceti numele: ");
        String lastName = scanner.nextLine().trim();

        Utilizator saved = utilizatorService.save(gmail, firstName, lastName);
        System.out.println("Utilizator salvat cu succes: " + saved);
    }

    private void uiRemoveUtilizator() {
        System.out.print("Introduceti adresa de Gmail a utilizatorului de sters: ");
        String gmail = scanner.nextLine().trim();
        Utilizator deleted = utilizatorService.delete(gmail);
        System.out.println("Utilizatorul a fost sters: " + deleted);
    }

    private void uiAddPrietenie() {
        System.out.print("Introduceti Gmail-ul primului utilizator: ");
        String email1 = scanner.nextLine().trim();
        System.out.print("Introduceti Gmail-ul celui de-al doilea utilizator: ");
        String email2 = scanner.nextLine().trim();

        prietenieService.save(email1, email2);
        System.out.println("Prietenie adaugata cu succes!");
    }

    private void uiRemovePrietenie() {
        System.out.print("Introduceti Gmail-ul primului utilizator: ");
        String email1 = scanner.nextLine().trim();
        System.out.print("Introduceti Gmail-ul celui de-al doilea utilizator: ");
        String email2 = scanner.nextLine().trim();

        prietenieService.delete(email1, email2);
        System.out.println("Prietenie stearsa cu succes!");
    }

    private void uiShowUtilizatori() {
        System.out.println("Lista utilizatorilor:");
        utilizatorService.findAll().forEach(System.out::println);
    }

    private void uiShowPrietenii() {
        System.out.println("Lista prieteniilor:");
        prietenieService.findAll().forEach(System.out::println);
    }

    private void uiNrComunitati() {
        System.out.println("Numarul de comunitati din retea: " + comunitatiService.getNrComunitati());
    }

    private void uiCeaMaiSociabilaComunitate() {
        List<Utilizator> membrii = comunitatiService.getCeaMaiSociabilaComunitate();
        if (membrii.isEmpty()) {
            System.out.println("Nu exista nicio comunitate in retea!");
            return;
        }
        System.out.println("Cea mai sociabila comunitate (membri):");
        membrii.forEach(System.out::println);
    }

    private void uiFiltrareUtilizatori() {
        System.out.print("Introduceti prefixul prenumelui pentru filtrare: ");
        String prefix = scanner.nextLine().trim();

        Predicate<Utilizator> filtru = CriteriiUtilizator.prenumeIncepeCu(prefix);
        List<Utilizator> filtrati = utilizatorService.filterUsers(filtru);

        System.out.println("Rezultate filtrate:");
        filtrati.forEach(System.out::println);
    }
}