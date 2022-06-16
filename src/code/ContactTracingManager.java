package code;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;

import java.util.Arrays;
import java.util.List;

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

    public void findPerson(String arg) {
        List<String> parsedArgs = parseArgument(arg);
        people.stream()
                .filter(person -> person.getName().toLowerCase().contains(parsedArgs.get(0).toLowerCase()))
                .forEach(System.out::println);
    }

    public void findPlace(String arg) {
        List<String> parsedArgs = parseArgument(arg);
    }

    public void contactPerson(String arg) {

    }

    public void visitor(String arg) {

    }


}
