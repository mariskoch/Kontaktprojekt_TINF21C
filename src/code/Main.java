package code;

public class Main {

    public static void main(String[] args) {
        final String FILEPATH = "resources/contacts2021.db";
        //final String FILEPATH = "resources/test.db";
        ParsingHelper ph = new ParsingHelper(FILEPATH);

        ContactTracingManager ctm = new ContactTracingManager(
                ph.getPeople(),
                ph.getPlaces(),
                ph.getVisits()
        );

        if (args.length != 1) {
            System.out.println("Invalid amount of arguments. Please enter one argument of:\n" +
                    "\"--personensuche=\", \"--ortssuche=\", \"--kontaktperson=\", \"--besucher=\"");
        } else if (args[0].startsWith("--personensuche=")) {
            ctm.findPerson(args[0]);
        } else if (args[0].startsWith("--ortssuche=")) {
            ctm.findPlace(args[0]);
        } else if (args[0].startsWith("--kontaktperson")) {
            ctm.contactPerson(args[0]);
        } else if (args[0].startsWith("--besucher=")) {
            ctm.visitor(args[0]);
        } else {
            System.out.println("Unknown argument.");
        }

    }

}
