package code.modellingclasses;

import java.time.LocalDateTime;

public class Visit {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Person visitor;
    private final Place place;

    public Visit(LocalDateTime start, LocalDateTime end, Person visitor, Place place) {
        this.start = start;
        this.end = end;
        this.visitor = visitor;
        this.place = place;
    }

    @Override
    public String toString() {
        return start.toString() + "\t" + end.toString() + "\t" + visitor + "\t" + place;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Person getVisitor() {
        return visitor;
    }

    public Place getPlace() {
        return place;
    }
}
