package code;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;
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
        final int[] classFlag = {0};
        input.remove(0);
        input.forEach(string -> {
            if (string.contains("New_Entity:")) {
                classFlag[0]++;
                return;
            }
            switch (classFlag[0]) {
                case 0 -> people.add(createPerson(string));
                case 1 -> places.add(createPlace(string));
                case 2 -> visits.add(createVisit(string));
            }
        });
    }

    private Person createPerson(String info) {
        List<String> split = splitString(info);
        int id = Integer.parseInt(split.get(0));
        String name = split.get(1);
        return new Person(id, name);
    }

    private Ort createPlace(String info) {
        List<String> split = splitString(info);
        int id = Integer.parseInt(split.get(0));
        String name = split.get(1);
        boolean isOutside = split.get(2).equalsIgnoreCase("out_door");
        return new Ort(id, name, isOutside);
    }

    private Besuch createVisit(String info) {
        List<String> split = splitString(info);
        LocalDateTime start = LocalDateTime.parse(split.get(0));
        LocalDateTime end = LocalDateTime.parse(split.get(1));
        int visitorID = Integer.parseInt(split.get(2));
        int placeID = Integer.parseInt(split.get(3));
        return new Besuch(start, end, visitorID, placeID);
    }

    private List<String> splitString(String info) {
        return Arrays.stream(info.split(","))
                .map(string -> string.replaceAll("\"", "").trim())
                .toList();
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
