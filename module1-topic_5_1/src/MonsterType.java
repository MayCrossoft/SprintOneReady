public enum MonsterType {
    GOBLIN  ("Гоблин",   "\uD83D\uDC7A", 10,  1, 1, "Слабый трус. Одно лёгкое уравнение."),
    ORC     ("Орк",      "\uD83D\uDC79", 25,  2, 1, "Грубая сила. Нужно вычитание."),
    TROLL   ("Тролль",   "\uD83E\uDDCC", 40,  2, 2, "Тупой, но живучий. Умножение!"),
    DRAGON  ("Дракон",   "\uD83D\uDC09", 70,  3, 3, "Огнедышащий. Комбо-задачи."),
    BOSS    ("БОСС",     "\uD83D\uDC80", 100, 4, 4, "Повелитель тьмы. 4 задачи подряд!");

    public final String name;
    public final String image;
    public final int hp;
    public final int damage;
    public final int questions;
    public final String description;

    MonsterType(String name, String image, int hp, int damage, int questions, String description) {
        this.name        = name;
        this.image       = image;
        this.hp          = hp;
        this.damage      = damage;
        this.questions   = questions;
        this.description = description;
    }
}
