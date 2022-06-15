package code;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;

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

    private String[] parseArgument(String arg) {
        String[] parsedArgs = arg.split("=");
        parsedArgs = parsedArgs[1].split(",");
        //TODO: " und whitespaces entfernen. funktioniert so noch nicht. muss direkt in parsedArgs geschrieben werden
        for (String argument : parsedArgs) {
            argument = argument.replace("\"", "").trim();
        }
        return parsedArgs;
    }

    public void findPerson(String arg) {
        String[] parsedArgs = parseArgument(arg);
        people.stream()
                .filter(p -> p.getName().toLowerCase().contains(parsedArgs[0].toLowerCase()))
                .forEach(p -> System.out.println(p.toString()));
    }

    public void findPlace(String arg) {

    }

    public void contactPerson(String arg) {

    }

    public void visitor(String arg) {

    }


}
