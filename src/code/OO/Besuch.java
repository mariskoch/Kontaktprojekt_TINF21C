package code.OO;

import java.time.LocalDateTime;

public class Besuch {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Person visitor;
    private final Ort place;

    public Besuch(LocalDateTime start, LocalDateTime end, Person visitor, Ort place) {
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

    public Ort getPlace() {
        return place;
    }
}
