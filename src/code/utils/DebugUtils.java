package code.utils;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;

import java.util.List;

public class DebugUtils {

    //TODO: Remove Debug implementation
    public static void debug(List<Person> people, List<Ort> places, List<Besuch> visits) {
        for (Person person : people) {
            System.out.println(person.toString());
        }
        for (Ort place : places) {
            System.out.println(place.toString());
        }
        for (Besuch visit : visits) {
            System.out.println(visit.toString());
        }
    }
}
