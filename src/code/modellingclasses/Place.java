package code.modellingclasses;

public class Place {
    private final int id;
    private final String name;
    private final boolean isOutside;

    public Place(int id, String name, boolean isOutside) {
        this.id = id;
        this.name = name;
        this.isOutside = isOutside;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + isOutside;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOutside() {
        return isOutside;
    }
}
