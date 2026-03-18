import java.util.Random;

public class Item {
    private final ItemType type;
    private final int x, y;
    private static final Random r = new Random();
    private static final ItemType[] NON_CHEST = {
        ItemType.POTION, ItemType.SWORD, ItemType.SHIELD, ItemType.SCROLL, ItemType.BOOTS
    };

    Item(int sizeBoard) {
        this.y    = r.nextInt(sizeBoard - 1); // 0-based, не на последней строке
        this.x    = r.nextInt(sizeBoard);
        this.type = randomType();
    }

    private static ItemType randomType() {
        int roll = r.nextInt(100);
        if (roll < 30) return ItemType.POTION;
        if (roll < 50) return ItemType.SWORD;
        if (roll < 65) return ItemType.SHIELD;
        if (roll < 80) return ItemType.SCROLL;
        if (roll < 90) return ItemType.BOOTS;
        return ItemType.CHEST;
    }

    public static ItemType randomNonChest() {
        return NON_CHEST[r.nextInt(NON_CHEST.length)];
    }

    public ItemType getType() { return type; }
    public String   getImage(){ return type.image; }
    public int      getX()    { return x; }
    public int      getY()    { return y; }
}
