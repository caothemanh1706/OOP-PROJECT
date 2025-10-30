package types;

/** Represents the type of brick */
public class BrickType {
    private final int type;
    private final int durability;

    /** Constructor */
    public BrickType(int type, int durability) {
        this.type = type;
        this.durability = durability;
    }

    public int getType() {
        return type;
    }

    public int getDurability() {
        return durability;
    }
}
