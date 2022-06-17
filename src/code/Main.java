package code;

public class Main {

    public static void main(String[] args) {
        final String FILEPATH = "resources/contacts2021.db";
        ParsingHelper ph = new ParsingHelper(FILEPATH);

        ContactTracingManager ctm = new ContactTracingManager(
                ph.getPeople(),
                ph.getPlaces(),
                ph.getVisits()
        );

        System.out.println(ctm.processInput(args));
    }
}
