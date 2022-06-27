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
     * name contains the given key in the parsedArgs at index 0.
     * @param parsedArgs list of parsed arguments
     * @return String with all people and their id fulfilling the specification
     */
    private String findPerson(List<String> parsedArgs) {
        StringBuilder sb = new StringBuilder();
        people.stream()
                //filtering all people by name
                .filter(person -> person.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                //append .toString() of people to StringBuilder
                .forEach(person -> sb.append(person).append("\n"));
        return sb.toString();
    }

    /**
     * Finds a certain Place by its name.
     * @param parsedArgs list of parsed arguments
     * @return String with all Places and their id and whether the place is in- or outdoor fulfilling the specification
     */
    private String findPlace(List<String> parsedArgs) {
        StringBuilder sb = new StringBuilder();
        places.stream()
                //filter all places by id
                .filter(place -> place.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                //append .toString() to StringBuilder
                .forEach(place -> sb.append(place).append("\n"));
        return sb.toString();
    }

    /**
     * Filters out list of all people that met the given person at any time in the registered visits
     * @param parsedArgs list of parsed arguments
     * @return list of names, which fulfill specifications
     */
    private List<String> contactPerson(List<String> parsedArgs) {
        List<Visit> visitsByPerson = visits.stream()
                //filter all visits done by the given person
                .filter(visit -> visit.getVisitor().getId() == Integer.parseInt(parsedArgs.get(0)))
                .toList();
        return visits.stream()
                .filter(generalVisit -> {
                    //filter all registered visits if they are at the same time, in the same place, and outside
                    //map visitsByPerson to booleans to check if the generalVisit's visitor is a contact person
                    List<Boolean> visitsByPersonMatching = visitsByPerson.stream().map(personVisit ->
                            doVisitsOverlap(personVisit, generalVisit) &&
                                    areVisitsInSamePlace(personVisit, generalVisit) &&
                                    !isSamePerson(personVisit, generalVisit) &&
                                    !personVisit.getPlace().isOutside()).toList();
                    //if any visitByPerson matches all the upper criteria (time, place, isOutside), then the
                    //generalVisit has a contact person
                    return visitsByPersonMatching.contains(true);
                })
                //map visits to name of visitors
                .map(visit -> visit.getVisitor().getName())
                //remove duplicates, sort alphabetically, transform back to list
                .distinct().sorted().collect(Collectors.toList());
    }

    /**
     * Filters list of all visitors at given place at a given time and potentially all of their contact people, if
     * place is indoor
     * @param parsedArgs list of parsed arguments
     * @return list of names, which fulfill specifications
     */
    private List<String> visitorAndContacts(List<String> parsedArgs) {
        Place place = getPlaceByID(Integer.parseInt(parsedArgs.get(0)));
        LocalDateTime timeStamp = LocalDateTime.parse(parsedArgs.get(1));
        List<Person> peopleAtPlace = visits.stream()
                //filter for all visits which happen in the same place and at the same time
                .filter(visit -> visit.getPlace().getId() == place.getId() && isDuringVisit(timeStamp, visit))
                //map visits to their visitors
                .map(Visit::getVisitor)
                //remove duplicates
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        List<String> contactPeople = new ArrayList<>();
        //if place is indoor, then all the contact people of the visitors are added
        if (!place.isOutside()) {
            peopleAtPlace.forEach(person -> contactPeople.addAll(contactPerson(List.of("" + person.getId()))));
        }
        //concatenate visitors and their potential contact people, remove duplicates, sort alphabetically
        return Stream.concat(peopleAtPlace.stream().map(Person::getName), contactPeople.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * This function transforms the String input from the Program into usable List of arguments.
     * The String is split at a "=" and everything after that is split at a ",".
     * Only the list of arguments behind the "=" is returned.
     * For a Person, the argument order is: int id, String name
     * For a Place, the argument order is: int id, String name, boolean isOutdoor
     * For a Visit, the argument order is: LocalDateTime start, LocalDateTime end, int visitorID, int placeID
     * @param arg - Input from program
     * @return List of arguments behind the "=" sign, with removed " and trimmed
     */
    private List<String> parseArgument(String arg) {
        if (arg.split("=").length < 2) return new ArrayList<>(List.of(arg));
        return Arrays.stream(arg.split("=")[1].split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
    }

    /**
     * @param visit1 first visit to compare
     * @param visit2 second visit to compare
     * @return boolean, which tells whether the given visits overlap
     */
    private boolean doVisitsOverlap(Visit visit1, Visit visit2) {
        return visit1.getStart().isBefore(visit2.getEnd()) && visit1.getEnd().isAfter(visit2.getStart());
    }

    /**
     * @param visit1 first visit to compare
     * @param visit2 second visit to compare
     * @return boolean, which tells whether the place of the two visits is the same
     */
    private boolean areVisitsInSamePlace(Visit visit1, Visit visit2) {
        return visit1.getPlace().equals(visit2.getPlace());
    }

    /**
     * @param visit1 first visit to compare
     * @param visit2 second visit to compare
     * @return boolean, which tells whether the two visits were done by the same person
     */
    private boolean isSamePerson(Visit visit1, Visit visit2) {
        return (visit1.getVisitor().getId() == visit2.getVisitor().getId());
    }

    /**
     * @param timeStamp timestamp which has to be checked
     * @param visit visit which is to be checked
     * @return boolean, which tells whether the timestamp is within the duration of the visit
     */
    private boolean isDuringVisit(LocalDateTime timeStamp, Visit visit) {
        return visit.getStart().isEqual(timeStamp) ||
                visit.getEnd().isEqual(timeStamp) ||
                (visit.getStart().isBefore(timeStamp) && visit.getEnd().isAfter(timeStamp));
    }

    /**
     * Returns place corresponding to given id
     * @param id id of place to search for
     * @return place identified by id
     */
    private Place getPlaceByID(int id) {
        return places.stream().filter(place -> place.getId() == id).toList().get(0);
    }

    /**
     * @param input List of Strings which sha
     * @return String with content from input seperated by a ","
     */
    private String castToOutputFormat(List<String> input) {
        StringBuilder sb = new StringBuilder();
        input.forEach(string -> sb.append(string).append(", "));
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}