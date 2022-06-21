package code;

import code.modellingclasses.Visit;
import code.modellingclasses.Place;
import code.modellingclasses.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactTracingManager {
    private final List<Person> people;
    private final List<Place> places;
    private final List<Visit> visits;

    public ContactTracingManager(List<Person> people, List<Place> places, List<Visit> visits) {
        this.people = people;
        this.places = places;
        this.visits = visits;
    }

    /**
     * This is the central method of the ContactTracingManager. It takes the arguments given to the main method,
     * parses those and processes them to return one String with all Information in it according to the exercise specification.
     * @param args Arguments given to main
     * @return String containing information according to exercise specification
     */
    public String processInput(String[] args) {
        List<String> parsedArgs;
        if (args.length != 1) {
            throw new RuntimeException("Invalid amount of arguments. Please enter one argument of:\n" +
                    "\"--personensuche=\", \"--ortssuche=\", \"--kontaktperson=\", \"--besucher=\"");
        } else {
            parsedArgs = parseArgument(args[0]);
        }

        if (args[0].startsWith("--personensuche=")) {
            return findPerson(parsedArgs);
        } else if (args[0].startsWith("--ortssuche=")) {
            return findPlace(parsedArgs);
        } else if (args[0].startsWith("--kontaktperson")) {
            return castToOutputFormat(contactPerson(parsedArgs));
        } else if (args[0].startsWith("--besucher=")) {
            return castToOutputFormat(visitorAndContacts(parsedArgs));
        } else {
            throw new RuntimeException("Unknown argument. Please enter one argument of:\n" +
                    "\"--personensuche=\", \"--ortssuche=\", \"--kontaktperson=\", \"--besucher=\"");
        }
    }

    /**
     * Finds a certain Person by looking through every single Person and checking whether their
     * name contains the given key in the parsedArgs at index 0. After that it builds a String with the overwritten
     * .toString() method of Person.
     */
    private String findPerson(List<String> parsedArgs) {
        StringBuilder sb = new StringBuilder();
        people.stream()
                .filter(person -> person.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                .forEach(person -> sb.append(person).append("\n"));
        return sb.toString();
    }

    private String findPlace(List<String> parsedArgs) {
        StringBuilder sb = new StringBuilder();
        places.stream()
                .filter(place -> place.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                .forEach(place -> sb.append(place).append("\n"));
        return sb.toString();
    }

    private List<String> contactPerson(List<String> parsedArgs) {
        List<Visit> visitsByPerson = visits.stream()
                .filter(visit -> visit.getVisitor().getId() == Integer.parseInt(parsedArgs.get(0)))
                .toList();
        return visits.stream()
                .filter(generalVisit -> {
                    List<Boolean> visitsByPersonMatching = visitsByPerson.stream().map(personVisit ->
                            doVisitsOverlap(personVisit, generalVisit) &&
                                    areVisitsInSamePlace(personVisit, generalVisit) &&
                                    isNotSamePerson(personVisit, generalVisit) &&
                                    !personVisit.getPlace().isOutside()).toList();
                    return visitsByPersonMatching.contains(true);
                })
                .map(visit -> visit.getVisitor().getName())
                .distinct().sorted().collect(Collectors.toList());
    }

    private List<String> visitorAndContacts(List<String> parsedArgs) {
        Place place = getPlaceByID(Integer.parseInt(parsedArgs.get(0)));
        LocalDateTime timeStamp = LocalDateTime.parse(parsedArgs.get(1));
        List<Person> peopleAtPlace = visits.stream()
                .filter(visit -> visit.getPlace().getId() == place.getId() && isDuringVisit(timeStamp, visit))
                .map(Visit::getVisitor)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        List<String> contactPeople = new ArrayList<>();
        if (!place.isOutside()) {
            peopleAtPlace.forEach(person -> contactPeople.addAll(contactPerson(List.of("" + person.getId()))));
        }
        return Stream.concat(peopleAtPlace.stream().map(Person::getName), contactPeople.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> parseArgument(String arg) {
        if (arg.split("=").length < 2) return new ArrayList<>(List.of(arg));
        return Arrays.stream(arg.split("=")[1].split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
    }

    private boolean doVisitsOverlap(Visit visit1, Visit visit2) {
        return visit1.getStart().isBefore(visit2.getEnd()) && visit1.getEnd().isAfter(visit2.getStart());
    }

    private boolean areVisitsInSamePlace(Visit visit1, Visit visit2) {
        return visit1.getPlace().equals(visit2.getPlace());
    }

    private boolean isNotSamePerson(Visit visit1, Visit visit2) {
        return !(visit1.getVisitor().getId() == visit2.getVisitor().getId());
    }

    private boolean isDuringVisit(LocalDateTime timeStamp, Visit visit) {
        return visit.getStart().isEqual(timeStamp) ||
                visit.getEnd().isEqual(timeStamp) ||
                (visit.getStart().isBefore(timeStamp) && visit.getEnd().isAfter(timeStamp));
    }

    private Place getPlaceByID(int id) {
        return places.stream().filter(place -> place.getId() == id).toList().get(0);
    }

    private String castToOutputFormat(List<String> input) {
        StringBuilder sb = new StringBuilder();
        input.forEach(string -> sb.append(string).append(", "));
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}