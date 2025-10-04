package types;

/** Represents the type of brick */
public class BrickType {
    private final String name;
    private final int durability;

    /** Constructor */
    public BrickType(String name, int durability) {
        this.name = name;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability;
    }
}
