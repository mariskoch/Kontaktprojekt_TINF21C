package code;

import code.modellingclasses.Besuch;
import code.modellingclasses.Ort;
import code.modellingclasses.Person;
import code.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsingHelper {
    private final List<String> input;
    private final List<Person> people = new ArrayList<>();
    private final List<Ort> places = new ArrayList<>();
    private final List<Besuch> visits = new ArrayList<>();

    public ParsingHelper(String FILEPATH) {
        input = FileUtils.readFileToMemory(FILEPATH);
        parseToObject();
    }

    private void parseToObject() {
        if (input.size()==0) return;
        final int[] classFlag = {0};
        input.remove(0);
        input.forEach(string -> {
            if (string.contains("New_Entity:")) {
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

    private void addPersonToArray(String info) {
        List<String> split = splitString(info);
        boolean doesExistAlready = people.stream().anyMatch(person -> person.getId() == Integer.parseInt(split.get(0)));
        if (!doesExistAlready) people.add(createPerson(split));
    }

    private void addPlaceToArray(String info) {
        List<String> split = splitString(info);
        boolean doesExistAlready = places.stream().anyMatch(place -> place.getId() == Integer.parseInt(split.get(0)));
        if (!doesExistAlready) places.add(createPlace(split));
    }

    private void addVisitToArray(String info) {
        List<String> split = splitString(info);
        LocalDateTime start = LocalDateTime.parse(split.get(0));
        LocalDateTime end = LocalDateTime.parse(split.get(1));
        Person visitor = getPersonByID(Integer.parseInt(split.get(2)));
        Ort place = getPlaceByID(Integer.parseInt(split.get(3)));
        visits.add(new Besuch(start, end, visitor, place));
    }

    private Person createPerson(List<String> info) {
        int id = Integer.parseInt(info.get(0));
        String name = info.get(1);
        return new Person(id, name);
    }

    private Ort createPlace(List<String> info) {
        int id = Integer.parseInt(info.get(0));
        String name = info.get(1);
        boolean isOutside = info.get(2).equalsIgnoreCase("out_door");
        return new Ort(id, name, isOutside);
    }

    private List<String> splitString(String info) {
        return Arrays.stream(info.split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
    }

    private Person getPersonByID(int id) {
        return people.stream().filter(person -> person.getId() == id).toList().get(0);
    }

    private Ort getPlaceByID(int id) {
        return places.stream().filter(place -> place.getId() == id).toList().get(0);
    }

    public List<Person> getPeople() {
        return people;
    }

    public List<Ort> getPlaces() {
        return places;
    }

    public List<Besuch> getVisits() {
        return visits;
    }
}
