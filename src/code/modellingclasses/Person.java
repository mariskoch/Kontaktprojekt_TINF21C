package code.modellingclasses;

public class Person {
    private final int id;
    private final String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + "\t" + name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
