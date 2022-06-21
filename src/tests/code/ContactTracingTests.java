package tests.code;

import code.ContactTracingManager;
import code.ParsingHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContactTracingTests {
    private final String TESTDATA_FULL = "src/tests/testresources/testdata_full.db";
    private final ParsingHelper ph = new ParsingHelper(TESTDATA_FULL);
    private final ContactTracingManager ctm = new ContactTracingManager(
            ph.getPeople(),
            ph.getPlaces(),
            ph.getVisits()
    );

    @Test
    public void testProcessInput_InvalidAmountOfArguments() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            ctm.processInput(new String[]{});
        });
        Assertions.assertEquals("Invalid amount of arguments. Please enter one argument of:\n" +
                "\"--personensuche=\", \"--ortssuche=\", \"--kontaktperson=\", \"--besucher=\"", thrown.getMessage());
    }

    @Test
    public void testProcessInput_UnknownArgument() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            ctm.processInput(new String[]{"this is an unknown argument"});
        });
        Assertions.assertEquals("Unknown argument. Please enter one argument of:\n" +
                "\"--personensuche=\", \"--ortssuche=\", \"--kontaktperson=\", \"--besucher=\"", thrown.getMessage());
    }

    @Test
    public void testProcessInput_FindPerson() {
        String expected = """
                8\tMila
                103\tMilan
                """;
        Assertions.assertEquals(expected, ctm.processInput(new String[]{"--personensuche=\"ila\""}));
    }

    @Test
    public void testProcessInput_FindPlace() {
        String expected = """
                2\tSupermarkt\tfalse
                5\tSpielplatz\ttrue
                """;
        Assertions.assertEquals(expected, ctm.processInput(new String[]{"--ortssuche=\"p\""}));
    }

    @Test
    public void testProcessInput_ContactPerson() {
        String expected = """
                Aaron, Amelie, Ben, Emil, Emilia, Emily, Felix, Hannah, Hannes, Julius, Leonard, Levi, Louis, Malia, Marlene, Ole, Rosalie, Sophia, Victoria""";
        Assertions.assertEquals(expected, ctm.processInput(new String[]{"--kontaktpersonen=1"}));
    }

    @Test
    public void testProcessInput_VisitorAndContacts() {
        String expected = """
                Adam, Amelie, Carla, Carlotta, Charlotte, Elli, Emil, Emilia, Emily, Emma, Eva, Fiona, Hannah, Hannes, Jonah, Jonas, Joshua, Konstantin, Lian, Lisa, Luisa, Malia, Mara, Maria, Mattis, Max, Melina, Mia, Mohammed, Noah, Ole, Sophia, Tim, Tom, Toni, Victoria""";
        Assertions.assertEquals(expected, ctm.processInput(new String[]{"--besucher=1,\"2021-05-15T14:16:00\""}));
    }
}
