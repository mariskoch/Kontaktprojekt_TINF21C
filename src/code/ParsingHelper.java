package code;

import code.modellingclasses.Visit;
import code.modellingclasses.Place;
import code.modellingclasses.Person;
import code.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsingHelper {
    private final List<Person> people = new ArrayList<>();
    private final List<Place> places = new ArrayList<>();
    private final List<Visit> visits = new ArrayList<>();

    /**
     * The constructor reads alle the lines from the file at the given path and parses the content into the
     * people, places and visits Lists.
     * @param FILEPATH String leading to the *.db file to be analyzed
     */
    public ParsingHelper(String FILEPATH) {
        parseToObject(FileUtils.readFileToMemory(FILEPATH));
    }

    /**
     * Parses Strings from file into Objects in people, places and visits lists
     * @param input lines from file
     */
    private void parseToObject(List<String> input) {
        if (input.size() == 0) return;
        final int[] classFlag = {0};
        //remove first line, since it is a "New_Entity: ..." line
        input.remove(0);
        input.forEach(string -> {
            if (string.startsWith("New_Entity:")) {
                classFlag[0]++;
                return;
            }
            switch (classFlag[0]) {
                case 0 -> addPersonToArray(string);
                case 1 -> addPlaceToArray(string);
                case 2 -> addVisitToArray(string);
            }
        });
    }

    /**
     * Adds a person to people list from given information
     * @param info Information on how to build Person
     *             format must be: "int id","String name"
     */
    private void addPersonToArray(String info) {
        List<String> split = splitString(info);
        boolean doesExistAlready = people.stream().anyMatch(person -> person.getId() == Integer.parseInt(split.get(0)));
        if (!doesExistAlready) people.add(createPerson(split));
    }

    /**
     * Adds place to places list from given information
     * @param info Information on how to build Place
     *             format must be: "int id","String name","in_door/out_door"
     */
    private void addPlaceToArray(String info) {
        List<String> split = splitString(info);
        boolean doesExistAlready = places.stream().anyMatch(place -> place.getId() == Integer.parseInt(split.get(0)));
        if (!doesExistAlready) places.add(createPlace(split));
    }

    /**
     * Adds visit to visits list from given information
     * @param info Information on how to build a Visit
     *             format must be: "LocalDateTime start","LocalDateTime end","int visitorID","int placeID"
     */
    private void addVisitToArray(String info) {
        List<String> split = splitString(info);
        LocalDateTime start = LocalDateTime.parse(split.get(0));
        LocalDateTime end = LocalDateTime.parse(split.get(1));
        Person visitor = getPersonByID(Integer.parseInt(split.get(2)));
        Place place = getPlaceByID(Integer.parseInt(split.get(3)));
        visits.add(new Visit(start, end, visitor, place));
    }

    /**
     * @param info Information on how to build the Place
     *             format must be: index 0 = id, index 1 = name
     * @return Person created from information
     */
    private Person createPerson(List<String> info) {
        int id = Integer.parseInt(info.get(0));
        String name = info.get(1);
        return new Person(id, name);
    }

    /**
     * @param info Information on how to build the Place
     *             format must be: index 0 = id, index 1 = name, index 2 = in_door/outdoor
     * @return Place created from Information
     */
    private Place createPlace(List<String> info) {
        int id = Integer.parseInt(info.get(0));
        String name = info.get(1);
        boolean isOutside = info.get(2).equalsIgnoreCase("out_door");
        return new Place(id, name, isOutside);
    }

    /**
     * String will be split at ",", all " will be removed and it will be trimmed
     * @param info String to be split
     * @return List of parts from split
     */
    private List<String> splitString(String info) {
        return Arrays.stream(info.split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
    }

    /**
     * @param id id of person to be found
     * @return Person identified by id
     */
    private Person getPersonByID(int id) {
        return people.stream().filter(person -> person.getId() == id).toList().get(0);
    }

    /**
     * @param id id of place to be found
     * @return Place identified by id
     */
    private Place getPlaceByID(int id) {
        return places.stream().filter(place -> place.getId() == id).toList().get(0);
    }

    /**
     * @return List of "Person" containing all parsed objects from file of filepath
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * @return List of "Place" containing all parsed objects from file of filepath
     */
    public List<Place> getPlaces() {
        return places;
    }

    /**
     * @return List of "Visit" containing all parsed objects from file of filepath
     */
    public List<Visit> getVisits() {
        return visits;
    }
}
