
README - LABORATOR 7

 INTERFETE FUNCTIONALE UTILIZATE
- Validator<T>: adnotat cu @FunctionalInterface; contine metoda default andThen() pentru inlantuirea regulilor.
- Predicate<E>: utilizat in Repository.findAll(filter) si in CriteriiUtilizator (and, or, negate).
- Supplier / Function: utilizate in RepositoryFactory (Map cu EnumMap) pentru instantierea repository-urilor fara switch.
- Consumer<T>: utilizat in afisari (System.out::println) si in prelucrari terminale forEach.

 REFERINTE LA METODE PREZENTE IN PROIECT
- Metoda statica: CriteriiUtilizator::prenumeIncepeCu
- Metoda de instanta a unui obiect arbitrar: Utilizator::getId, Utilizator::getLastName
- Metoda de instanta a unui obiect anume: System.out::println
- Constructor: InMemoryRepository::new, ArrayList::new

 DECIZII PRIVIND OPTIONAL SI TEMPLATE METHOD
- Optional in Repository: S-a adoptat Optional<E> in semnatura interfetei Repository (findOne, save, delete, update).
  Aceasta decizie previne NullPointerException si obliga nivelul de service sa trateze explicit lipsa valorilor prin orElseThrow / ifPresent.
- Template Method in FileRepository: S-a pastrat AbstractFileRepository cu subclase deoarece imparte curat responsabilitatea de I/O generic
  de conversia de domeniu si pastreaza compatibilitatea simpla cu arhitectura existenta.

 GESTIUNEA VERSIONARII (GIT)
- Proiectul respecta bunele practici Git (commit-uri atomice, mesaje descriptive in limba engleza, excluderea folderelor build/out prin .gitignore).
