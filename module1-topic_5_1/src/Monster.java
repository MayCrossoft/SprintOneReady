import java.util.Random;
import java.util.Scanner;

public class Monster {
    private final MonsterType type;
    private final int x, y;
    private boolean alive = true;
    private static final Random r = new Random();

    Monster(int sizeBoard) {
        this.y    = r.nextInt(sizeBoard - 1);
        this.x    = r.nextInt(sizeBoard);
        this.type = randomType();
    }

    private static MonsterType randomType() {
        int roll = r.nextInt(100);
        if (roll < 40) return MonsterType.GOBLIN;
        if (roll < 65) return MonsterType.ORC;
        if (roll < 82) return MonsterType.TROLL;
        if (roll < 94) return MonsterType.DRAGON;
        return MonsterType.BOSS;
    }

    public String      getImage() { return type.image; }
    public MonsterType getType()  { return type; }
    public int         getX()     { return x; }
    public int         getY()     { return y; }
    public boolean     isAlive()  { return alive; }

    public boolean conflictPerson(int perX, int perY) {
        return alive && (perY - 1 == this.y) && (perX - 1 == this.x);
    }

    public BattleResult battle(int diff, Scanner sc, boolean hasSword, boolean[] hasScroll) {
        System.out.println("\n" + "═".repeat(44));
        System.out.println("  ⚔️  БИТВА С " + type.name.toUpperCase() + " " + type.image);
        System.out.println("  " + type.description);
        System.out.println("  ❤️  HP: " + type.hp + "   💥 Урон: " + type.damage
                + "   ❓ Вопросов: " + type.questions);
        System.out.println("═".repeat(44));

        int needed = hasSword ? 1 : type.questions;

        for (int q = 1; q <= needed; q++) {
            System.out.println("\n  [Вопрос " + q + "/" + needed + "]");
            int[] task = makeTask(diff, type);
            printTask(task, type);

            if (hasScroll[0]) {
                System.out.print("  Использовать свиток? (д/н): ");
                String choice = sc.next();
                if (choice.equalsIgnoreCase("д")) {
                    System.out.println("  📜 Свиток шепчет: ответ = " + task[2]);
                    hasScroll[0] = false;
                }
            }

            System.out.print("  Ваш ответ: ");
            int ans = sc.nextInt();
            if (ans == task[2]) {
                System.out.println("  ✅ Верно!");
            } else {
                System.out.println("  ❌ Неверно! Правильный ответ: " + task[2]);
                System.out.println("  " + type.name + " побеждает в этой схватке!");
                return BattleResult.LOSE;
            }
        }

        System.out.println("\n  🏅 " + type.name + " повержен!\n");
        alive = false;
        return BattleResult.WIN;
    }

    private static int[] makeTask(int diff, MonsterType type) {
        int range = diff * 10;
        int a, b, ans;
        switch (type) {
            case GOBLIN: {
                a = r.nextInt(range) + 1;
                b = r.nextInt(range) + 1;
                ans = a + b;
                break;
            }
            case ORC: {
                a = r.nextInt(range) + 1;
                b = r.nextInt(range) + 1;
                if (a < b) { int t = a; a = b; b = t; }
                ans = a - b;
                break;
            }
            case TROLL: {
                a = r.nextInt(diff * 5) + 2;
                b = r.nextInt(diff * 5) + 2;
                ans = a * b;
                break;
            }
            case DRAGON: {
                a = r.nextInt(diff * 8) + 2;
                b = r.nextInt(diff * 4) + 1;
                int c = r.nextInt(diff * 5) + 1;
                ans = a * b + c;
                return new int[]{a, b, ans, c};
            }
            case BOSS:
            default: {
                a = r.nextInt(diff * 10) + 2;
                b = r.nextInt(diff * 6) + 2;
                int c = r.nextInt(diff * 8) + 1;
                ans = a * b - c;
                return new int[]{a, b, ans, c};
            }
        }
        return new int[]{a, b, ans};
    }

    private static void printTask(int[] t, MonsterType type) {
        switch (type) {
            case GOBLIN: System.out.println("  " + t[0] + " + " + t[1] + " = ?"); break;
            case ORC:    System.out.println("  " + t[0] + " - " + t[1] + " = ?"); break;
            case TROLL:  System.out.println("  " + t[0] + " × " + t[1] + " = ?"); break;
            case DRAGON: System.out.println("  " + t[0] + " × " + t[1] + " + " + t[3] + " = ?"); break;
            case BOSS:   System.out.println("  " + t[0] + " × " + t[1] + " - " + t[3] + " = ?"); break;
        }
    }

    public enum BattleResult { WIN, LOSE }
}
