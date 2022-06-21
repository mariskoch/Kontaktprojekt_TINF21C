package tests.code;

import code.OO.Besuch;
import code.OO.Ort;
import code.OO.Person;
import code.ParsingHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadInputTests {

    @Test
    public void testReadInput_People() {
        List<Person> expected = new ArrayList<>();
        expected.add(new Person(1, "Mia"));
        expected.add(new Person(2, "Emilia"));
        expected.add(new Person(3, "Hannah"));
        expected.add(new Person(4, "Emma"));
        expected.add(new Person(5, "Sophia"));
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata.db");
        List<Person> actual = ph.getPeople();
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    public void testReadInput_People_Empty() {
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata_empty.db");
        List<Person> actual = ph.getPeople();
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void testReadInput_Places() {
        List<Ort> expected = new ArrayList<>();
        expected.add(new Ort(1, "BÃ¤ckerei", false));
        expected.add(new Ort(2, "Supermarkt", false));
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata.db");
        List<Ort> actual = ph.getPlaces();
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName());
            Assertions.assertEquals(expected.get(i).isOutside(), actual.get(i).isOutside());
        }
    }

    @Test
    public void testReadInput_Places_Empty() {
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata_empty.db");
        List<Ort> actual = ph.getPlaces();
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void testReadInput_Visits() {
        List<Besuch> expected = new ArrayList<>();
        expected.add(new Besuch(LocalDateTime.of(2021, 5, 15, 15, 0, 0, 0),
                LocalDateTime.of(2021,5,15,16,0,0,0),
                new Person(1, "Mia"),
                new Ort(1, "BÃ¤ckerei", false)));
        expected.add(new Besuch(LocalDateTime.of(2021,5,15,14,0,0,0),
                LocalDateTime.of(2021,5,15,15,0,1,0),
                new Person(2, "Emilia"),
                new Ort(1, "BÃ¤ckerei", false)));
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata.db");
        List<Besuch> actual = ph.getVisits();
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(expected.get(i).getStart().isEqual(actual.get(i).getStart()));
            Assertions.assertTrue(expected.get(i).getEnd().isEqual(actual.get(i).getEnd()));
            Assertions.assertEquals(expected.get(i).getPlace().getId(), actual.get(i).getPlace().getId());
            Assertions.assertEquals(expected.get(i).getPlace().getName(), actual.get(i).getPlace().getName());
            Assertions.assertEquals(expected.get(i).getPlace().isOutside(), actual.get(i).getPlace().isOutside());
            Assertions.assertEquals(expected.get(i).getVisitor().getId(), actual.get(i).getVisitor().getId());
            Assertions.assertEquals(expected.get(i).getVisitor().getName(), actual.get(i).getVisitor().getName());
        }
    }

    @Test
    public void testReadInput_Visits_Empty() {
        ParsingHelper ph = new ParsingHelper("src/tests/testresources/testdata_empty.db");
        List<Besuch> actual = ph.getVisits();
        Assertions.assertEquals(0, actual.size());
    }
}