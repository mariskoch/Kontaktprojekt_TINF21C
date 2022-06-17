package code;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;

import java.util.*;

public class ContactTracingManager {
    private final List<Person> people;
    private final List<Ort> places;
    private final List<Besuch> visits;

    public ContactTracingManager(List<Person> people, List<Ort> places, List<Besuch> visits) {
        this.people = people;
        this.places = places;
        this.visits = visits;
    }

    private List<String> parseArgument(String arg) {
        return Arrays.stream(arg.split("=")[1].split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
    }

    public String findPerson(String arg) {
        StringBuilder sb = new StringBuilder();
        List<String> parsedArgs = parseArgument(arg);
        people.stream()
                .filter(person -> person.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                .forEach(person -> sb.append(person).append("\n"));
        return sb.toString();
    }

    public String findPlace(String arg) {
        StringBuilder sb = new StringBuilder();
        List<String> parsedArgs = parseArgument(arg);
        places.stream()
                .filter(place -> place.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                .forEach(place -> sb.append(place).append("\n"));
        return sb.toString();
    }

    public String contactPerson(String arg) {
        StringBuilder sb = new StringBuilder();
        List<String> parsedArgs = parseArgument(arg);
        List<Besuch> visitsByPerson = visits.stream()
                .filter(visit -> visit.getVisitor().getId() == Integer.parseInt(parsedArgs.get(0)))
                .toList();
        List<String> peopleMet = new ArrayList<>(visits.stream()
                .filter(generalVisit -> {
                    List<Boolean> visitsByPersonMatching = visitsByPerson.stream().map(personVisit ->
                            doVisitsOverlap(personVisit, generalVisit) &&
                                    areVisitsInSamePlace(personVisit, generalVisit) &&
                                    isNotSameVisit(personVisit, generalVisit) &&
                                    !personVisit.getPlace().isOutside()).toList();
                    return visitsByPersonMatching.contains(true);
                })
                .map(visit -> visit.getVisitor().getName())
                .toList());
        Collections.sort(peopleMet);
        peopleMet.forEach(name -> sb.append(name).append(", "));
        sb.setLength(sb.length()-2);
        return sb.toString();
    }

    public void visitor(String arg) {

    }

    private boolean doVisitsOverlap(Besuch visit1, Besuch visit2) {
        return visit1.getStart().isBefore(visit2.getEnd()) && visit1.getEnd().isAfter(visit2.getStart());
    }

    private boolean areVisitsInSamePlace(Besuch visit1, Besuch visit2) {
        return visit1.getPlace().equals(visit2.getPlace());
    }

    private boolean isNotSameVisit(Besuch visit1, Besuch visit2) {
        return !visit1.equals(visit2);
    }

}
