package code.OO;

import java.time.LocalDateTime;

public class Besuch {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int visitorID;
    private final int placeID;

    public Besuch(LocalDateTime start, LocalDateTime end, int visitorID, int placeID) {
        this.start = start;
        this.end = end;
        this.visitorID = visitorID;
        this.placeID = placeID;
    }

    @Override
    public String toString() {
        return start.toString() + "\t" + end.toString() + "\t" + visitorID + "\t" + placeID;
    }
}
