# Social Network - Laborator 4 MAP

Aplicație pentru administrarea unei rețele sociale, refactorizată conform principiilor de programare funcțională introduse în Java 8. Modificările vizează exclusiv arhitectura internă a aplicației (înlocuirea structurilor imperative cu fluxuri de date declarative, eliminarea posibilității de NullPointerException și modularizarea prin interfețe funcționale), păstrând neatins meniul și comportamentul din consola Laboratorului 3.

Modificări Implementate în Cod

* **Interfața Funcțională de Validare (`domain.validation.Validator`)**
  Interfața generică `Validator<T>` a primit adnotarea `@FunctionalInterface`, având ca unică metodă abstractă `void validate(T entity)`. Pentru a asigura principiul Open/Closed (extinderea regulilor fără modificarea claselor existente), am adăugat metoda default `andThen(Validator<T> other)`. Aceasta permite înlănțuirea secvențială a validărilor, astfel încât o regulă nouă (de exemplu, validarea lungimii numelui sau a formatului de email) poate fi furnizată direct ca expresie lambda și compusă peste validatorul `UtilizatorValidator`.

* **Tratarea Absenței Datelor prin `Optional` (`repository.Repository`)**
  Am refactorizat contractul interfeței generice `Repository<ID, E>` și al tuturor claselor care o implementează (`InMemoryRepository`, `AbstractFileRepository`, `UtilizatorDbRepository`). Metodele `findOne`, `save`, `delete` și `update` returnează acum instanțe de `Optional<E>`. Prin această abordare, valorile `null` au fost eliminate din fluxul de execuție, iar serviciile superioare sunt obligate să trateze explicit cazul în care o entitate nu există, folosind metode funcționale precum `.orElseThrow()` sau `.ifPresent()`.

* **Filtrare Declarativă și Compunere de Predicate (`repository.Repository`)**
  Am introdus în interfața `Repository` metoda default `findAll(Predicate<E> filter)`. Aceasta transformă colecția internă de entități într-un stream secvențial prin `StreamSupport.stream()`, aplică condiția logică prin operația intermediară `.filter(filter)` și colectează rezultatele într-o listă prin `.collect(Collectors.toList())`. Toate tipurile de depozite moștenesc această metodă fără duplicare de cod, iar interogările se pot compune nativ în stil funcțional prin metodele default ale interfeței `Predicate` (`.and()`, `.or()`, `.negate()`).

* **Eliminarea Instrucțiunilor de Decizie din Factory (`repository.RepositoryFactory`)**
  Am eliminat blocul clasic `switch (repoType)` din `RepositoryFactory`, înlocuindu-l cu un `EnumMap` ce asociază fiecare `RepositoryType` cu o instanță a interfeței funcționale `Function<Validator, Repository>`. Pentru depozitul în memorie (`MEMORY`) instanțierea se face printr-o referință la constructor (`InMemoryRepository::new`), iar pentru fișiere și baze de date prin expresii lambda. Citirea și validarea cheilor din fișierul `config.properties` a fost rescrisă cu `Optional`, verificând prezența valorilor și eliminând spațiile inutile înainte de inițializare.

* **I/O Modern și Procesare cu Stream API (`repository.AbstractFileRepository`)**
  Am rescris operațiile de citire și scriere pe disc renunțând la clasele tradiționale `BufferedReader`/`BufferedWriter`. Încărcarea datelor utilizează `Files.readAllLines()` și `IntStream.range()`. Iterarea peste indicii liniilor permite raportarea numărului liniei la apariția unei excepții `ValidationException` fără a încălca restricția de variabilă `effectively final` impusă de lambdas. Scrierea pe disc folosește `.map(this::createEntityAsString)` și `Files.write()`, asigurând o scriere atomică și compactă.

* **Algoritm pe Grafuri Rescris Declarativ (`service.ComunitatiService`)**
  Determinarea componentelor conexe și a celei mai sociabile comunități a fost transformată într-un flux de operații funcționale. Construirea grafului de adiacență folosește `Collectors.toMap()` pentru alocarea listelor de vecini. Identificarea comunității optime se bazează pe `stream().max(Comparator.comparingInt(...))`, eliminând variabilele auxiliare de maxim. Transformarea identificatorilor în obiecte de domeniu se realizează prin `utilizatorRepo::findOne` și despachetare cu `flatMap(Optional::stream)`.

* **Utilizarea Formelor de Method References**
  Am integrat cele patru forme de referințe la metode:
  1. Metodă a unui obiect specific: `System.out::println` la afișarea colecțiilor în consolă.
  2. Metodă a unui obiect arbitrar: `Utilizator::getId` la maparea cheilor în servicii.
  3. Referință la constructor: `InMemoryRepository::new` în factory și `ArrayList::new` la instanțierea listelor.
  4. Metodă statică: `CriteriiUtilizator::prenumeIncepeCu` pentru generarea predicatelor de căutare.

## Rulare și Configurare

Aplicația își extrage parametrii de execuție din fișierul `config.properties`, unde se specifică mecanismul de persistență dorit (`repository.type=MEMORY|FILE|DATABASE`) și căile sau credențialele aferente. Punctul de lansare rămâne metoda statică `main` din fișierul `Main.java`.