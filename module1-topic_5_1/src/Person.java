import java.util.Random;

public class Person {
    private int x, y;
    private final String image = "\uD83E\uDDD9\u200D";
    private int live;
    private final int maxLive = 5;

    private boolean sword   = false;
    private boolean shield  = false;
    private boolean boots   = false;
    private boolean[] scroll = {false};

    private static final Random r = new Random();

    Person(int sizeBoard) {
        this.live = maxLive;
        this.y    = sizeBoard;
        this.x    = r.nextInt(sizeBoard) + 1;
    }

    public int     getX()           { return x; }
    public int     getY()           { return y; }
    public int     getLive()        { return live; }
    public int     getMaxLive()     { return maxLive; }
    public String  getImage()       { return image; }
    public boolean hasSword()       { return sword; }
    public boolean hasShield()      { return shield; }
    public boolean[] getScrollRef() { return scroll; }
    public boolean isDead()         { return live <= 0; }

    public boolean moveCorrect(int nx, int ny) {
        int dx = Math.abs(this.x - nx);
        int dy = Math.abs(this.y - ny);
        int maxStep = boots ? 2 : 1;
        return (this.x == nx && dy >= 1 && dy <= maxStep)
            || (this.y == ny && dx >= 1 && dx <= maxStep);
    }

    public void move(int nx, int ny) {
        if (boots) boots = false;
        this.x = nx;
        this.y = ny;
    }

    public void downLive(int dmg) {
        if (shield) {
            System.out.println("  🛡️  Щит поглотил удар!");
            shield = false;
            return;
        }
        live = Math.max(0, live - dmg);
    }

    public void heal(int amount) {
        live = Math.min(maxLive, live + amount);
    }

    public void pickupItem(ItemType type) {
        switch (type) {
            case POTION  -> { System.out.println("  ❤️  +2 жизни! (" + Math.min(maxLive, live + 2) + "/" + maxLive + ")"); heal(2); }
            case SWORD   -> { sword     = true;       System.out.println("  🗡️  Меч получен! Следующий бой: 1 вопрос."); }
            case SHIELD  -> { shield    = true;       System.out.println("  🛡️  Щит получен! Следующий урон заблокирован."); }
            case SCROLL  -> { scroll[0] = true;       System.out.println("  📜 Свиток получен! Можно подсмотреть один ответ."); }
            case BOOTS   -> { boots     = true;       System.out.println("  👢 Сапоги получены! Следующий ход — прыжок на 2 клетки."); }
            default      -> {}
        }
    }

    public void consumeSword() { sword = false; }

    public String inventoryString() {
        StringBuilder sb = new StringBuilder();
        if (sword)     sb.append("🗡️ ");
        if (shield)    sb.append("🛡️ ");
        if (scroll[0]) sb.append("📜 ");
        if (boots)     sb.append("👢 ");
        return sb.length() == 0 ? "пусто" : sb.toString().trim();
    }
}
