import java.util.Random;
import java.util.Scanner;

public class Main {

    static final String CASTLE = "\uD83C\uDFF0";
    static final Random r = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        printBanner();

        System.out.println("Готов начать игру? (ДА / НЕТ)");
        String answer = sc.nextLine().trim();

        switch (answer) {
            case "ДА"  -> startGame(sc);
            case "НЕТ" -> System.out.println("Жаль, приходи ещё! 👋");
            default    -> System.out.println("Некорректный ввод. Перезапусти игру.");
        }
        sc.close();
    }

    static void startGame(Scanner sc) {
        System.out.println("\nВыбери сложность (1 — легко, 5 — хардкор):");
        int diff = Math.max(1, Math.min(5, sc.nextInt()));

        int size = 4 + diff;
        Person person = new Person(size);

        String[][] board = new String[size][size];
        clearBoard(board);

        int monsterCount = size * size / 3;
        Monster[] monsters = new Monster[monsterCount];
        for (int i = 0; i < monsterCount; ) {
            Monster m = new Monster(size);
            if (isFree(board, m.getX(), m.getY(), person)) {
                board[m.getY()][m.getX()] = m.getImage();
                monsters[i++] = m;
            }
        }

        int itemCount = Math.max(3, size * size / 7 + 2);
        Item[] items = new Item[itemCount];
        for (int i = 0; i < itemCount; ) {
            Item it = new Item(size);
            if (isFree(board, it.getX(), it.getY(), person)) {
                board[it.getY()][it.getX()] = it.getImage();
                items[i++] = it;
            }
        }

        int castleX;
        do { castleX = r.nextInt(size); } while (!isFree(board, castleX, 0, person));
        board[0][castleX] = CASTLE;

        System.out.printf("\n🗺️  Карта %dx%d | Сложность: %d | Монстров: %d | Предметов: %d%n",
                size, size, diff, monsterCount, itemCount);
        System.out.println("Цель: доберись до замка " + CASTLE + " в верхней строке!\n");

        int step = 0;
        boolean running = true;

        while (running) {
            board[person.getY() - 1][person.getX() - 1] = person.getImage();
            printBoard(board, person);

            if (person.isDead()) {
                System.out.println("💀 Жизни закончились — GAME OVER после " + step + " ходов.");
                break;
            }

            String inv = person.inventoryString();
            System.out.println("Ход (x y) | позиция: (" + person.getX() + "," + person.getY() + ")"
                    + (inv.equals("пусто") ? "" : " | 🎒 " + inv));
            int nx = sc.nextInt();
            int ny = sc.nextInt();

            if (nx < 1 || nx > size || ny < 1 || ny > size) {
                System.out.println("❗ За пределами доски!"); continue;
            }
            if (!person.moveCorrect(nx, ny)) {
                System.out.println("❗ Некорректный ход (горизонталь/вертикаль"
                        + (inv.contains("👢") ? ", до 2 клеток с 👢" : ", 1 клетка") + ").");
                continue;
            }

            String cell = board[ny - 1][nx - 1];

            if (cell.equals("  ")) {
                doMove(board, person, nx, ny);
                step++;

            } else if (cell.equals(CASTLE)) {
                doMove(board, person, nx, ny);
                board[person.getY() - 1][person.getX() - 1] = person.getImage();
                printBoard(board, person);
                System.out.println("🏆🏆🏆  Вы взяли замок за " + step + " ходов! Поздравляем!  🏆🏆🏆");
                running = false;

            } else {
                Item item = findItem(items, nx, ny);
                if (item != null) {
                    System.out.println("\n✨ Найдено: " + item.getType().name + " " + item.getType().image);
                    System.out.println("   " + item.getType().description);
                    if (item.getType() == ItemType.CHEST) {
                        ItemType reward = Item.randomNonChest();
                        System.out.println("   📦 Внутри: " + reward.image + " " + reward.name + "!");
                        person.pickupItem(reward);
                    } else {
                        person.pickupItem(item.getType());
                    }
                    board[ny - 1][nx - 1] = "  ";
                    doMove(board, person, nx, ny);
                    step++;

                } else {
                    Monster monster = findMonster(monsters, nx, ny);
                    if (monster != null) {
                        boolean useSword = person.hasSword();
                        Monster.BattleResult res = monster.battle(diff, sc, useSword, person.getScrollRef());
                        if (useSword) person.consumeSword();

                        if (res == Monster.BattleResult.WIN) {
                            board[ny - 1][nx - 1] = "  ";
                            doMove(board, person, nx, ny);
                            step++;
                        } else {
                            person.downLive(monster.getType().damage);
                            System.out.printf("  💔 Жизни: %d/%d%n", person.getLive(), person.getMaxLive());
                            if (person.isDead()) {
                                printBoard(board, person);
                                System.out.println("💀 Вы погибли — GAME OVER после " + step + " ходов.");
                                running = false;
                            }
                        }
                    }
                }
            }
        }
    }

    static void doMove(String[][] board, Person person, int nx, int ny) {
        board[person.getY() - 1][person.getX() - 1] = "  ";
        person.move(nx, ny);
    }

    static void clearBoard(String[][] board) {
        for (String[] row : board)
            for (int x = 0; x < row.length; x++) row[x] = "  ";
    }

    static boolean isFree(String[][] board, int x, int y, Person person) {
        return board[y][x].equals("  ")
            && !(x == person.getX() - 1 && y == person.getY() - 1);
    }

    static Item findItem(Item[] items, int nx, int ny) {
        for (Item it : items)
            if (it != null && it.getX() == nx - 1 && it.getY() == ny - 1)
                return it;
        return null;
    }

    static Monster findMonster(Monster[] monsters, int nx, int ny) {
        for (Monster m : monsters)
            if (m != null && m.conflictPerson(nx, ny))
                return m;
        return null;
    }

    static void printBoard(String[][] board, Person person) {
        int w = board[0].length;
        String wall = ("+ —— ").repeat(w) + "+";
        System.out.println();
        for (String[] row : board) {
            System.out.println(wall);
            for (String cell : row) System.out.print("| " + cell + " ");
            System.out.println("|");
        }
        System.out.println(wall);
        int lv = person.getLive(), mx = person.getMaxLive();
        String hearts = "♥ ".repeat(lv) + "♡ ".repeat(Math.max(0, mx - lv));
        System.out.println("❤️  " + hearts.trim() + "  (" + lv + "/" + mx + ")\n");
    }

    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      🧙  DUNGEON QUEST — Math RPG  🧙     ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  МОНСТРЫ:                                ║");
        System.out.println("║  Символ  Имя           HP  Урон  Вопр.  ║");
        for (MonsterType mt : MonsterType.values())
            System.out.printf("║  %-4s  %-14s %3d   %2d    %d     ║%n",
                    mt.image, mt.name, mt.hp, mt.damage, mt.questions);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  ПРЕДМЕТЫ:                               ║");
        for (ItemType it : ItemType.values())
            System.out.printf("║  %s %-36s║%n", it.image, it.name);
        System.out.println("╚══════════════════════════════════════════╝\n");
    }
}
