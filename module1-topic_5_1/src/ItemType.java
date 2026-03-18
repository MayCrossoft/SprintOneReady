public enum ItemType {
    POTION  ("Зелье здоровья", "\uD83E\uDDEA", "Восстанавливает 2 жизни."),
    SWORD   ("Меч",            "\uD83D\uDDE1\uFE0F",  "Следующий бой требует только 1 ответ."),
    SHIELD  ("Щит",            "\uD83D\uDEE1\uFE0F",  "Поглощает следующий урон (не теряете жизнь)."),
    SCROLL  ("Свиток мудреца", "\uD83D\uDCDC", "Один раз подсказывает правильный ответ в бою."),
    BOOTS   ("Сапоги-скороходы","\uD83D\uDC62","Следующий ход можно прыгнуть на 2 клетки."),
    CHEST   ("Сундук",         "\uD83D\uDCE6", "Внутри случайный предмет!");

    public final String name;
    public final String image;
    public final String description;

    ItemType(String name, String image, String description) {
        this.name        = name;
        this.image       = image;
        this.description = description;
    }
}
