package service;

import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import repository.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ComunitatiService {
    private final Repository<String, Utilizator> utilizatorRepo;
    private final Repository<Tuple<String, String>, Prietenie> prietenieRepo;

    public ComunitatiService(Repository<String, Utilizator> utilizatorRepo,
                             Repository<Tuple<String, String>, Prietenie> prietenieRepo) {
        this.utilizatorRepo = utilizatorRepo;
        this.prietenieRepo = prietenieRepo;
    }

    private Map<String, List<String>> buildAdjList() {
        Map<String, List<String>> adj = StreamSupport.stream(utilizatorRepo.findAll().spliterator(), false)
                .collect(Collectors.toMap(Utilizator::getId, u -> new ArrayList<>()));

        StreamSupport.stream(prietenieRepo.findAll().spliterator(), false)
                .forEach(p -> {
                    String u1 = p.getIdUser1();
                    String u2 = p.getIdUser2();
                    if (adj.containsKey(u1) && adj.containsKey(u2)) {
                        adj.get(u1).add(u2);
                        adj.get(u2).add(u1);
                    }
                });

        return adj;
    }

    private void dfsComponent(String node, Map<String, List<String>> adj, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);

        adj.getOrDefault(node, Collections.emptyList()).stream()
                .filter(neighbor -> !visited.contains(neighbor))
                .forEach(neighbor -> dfsComponent(neighbor, adj, visited, component));
    }

    public List<List<String>> getComunitati() {
        Map<String, List<String>> adj = buildAdjList();
        Set<String> visited = new HashSet<>();
        List<List<String>> comunitati = new ArrayList<>();

        adj.keySet().stream()
                .filter(email -> !visited.contains(email))
                .forEach(email -> {
                    List<String> component = new ArrayList<>();
                    dfsComponent(email, adj, visited, component);
                    comunitati.add(component);
                });

        return comunitati;
    }

    public int getNrComunitati() {
        return getComunitati().size();
    }

    private int dfsLongestPath(String current, Map<String, List<String>> adj, Set<String> localVisited) {
        localVisited.add(current);

        int maxLen = adj.getOrDefault(current, Collections.emptyList()).stream()
                .filter(neighbor -> !localVisited.contains(neighbor))
                .mapToInt(neighbor -> 1 + dfsLongestPath(neighbor, adj, localVisited))
                .max()
                .orElse(0);

        localVisited.remove(current);
        return maxLen;
    }

    private int getMaxPathInComponent(List<String> component, Map<String, List<String>> adj) {
        return component.stream()
                .mapToInt(node -> dfsLongestPath(node, adj, new HashSet<>()))
                .max()
                .orElse(0);
    }

    public List<Utilizator> getCeaMaiSociabilaComunitate() {
        List<List<String>> comunitati = getComunitati();
        if (comunitati.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<String>> adj = buildAdjList();

        return comunitati.stream()
                .max(Comparator.comparingInt(comp -> getMaxPathInComponent(comp, adj)))
                .map(comp -> comp.stream()
                        .map(utilizatorRepo::findOne)
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}