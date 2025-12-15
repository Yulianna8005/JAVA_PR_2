import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 */
public class BasicDataOperationUsingMap {
    // Вхідні дані
    private final Ferret KEY_TO_SEARCH_AND_DELETE = new Ferret("Бандит", "корм");
    private final Ferret KEY_TO_ADD = new Ferret("Кекс", "фрукти");

    private final String VALUE_TO_SEARCH_AND_DELETE = "Андрій";
    private final String VALUE_TO_ADD = "Богдан";

    private Hashtable<Ferret, String> hashtable;
    private LinkedHashMap<Ferret, String> linkedHashMap;

    /**
     * Record Ferret для зберігання інформації про тхора (Ferret).
     * Record автоматично створює конструктор, геттери (nickname(), foodRation()), equals(), hashCode() та toString().
     * Тіло класу порожнє {}.
     *
     * @param nickname кличка тварини
     * @param foodRation раціон харчування
     */
    public record Ferret(String nickname, String foodRation) {}

    /**
     * Компаратор для порівняння об'єктів Ferret.
     * Сортування: спочатку за кличкою (nickname) за зростанням, потім за раціоном (foodRation) за зростанням.
     */
    private static final Comparator<Ferret> FERRET_COMPARATOR =
        Comparator.comparing(Ferret::nickname)
            .thenComparing(Ferret::foodRation);


    /**
     * Конструктор, який ініціалізує об'єкт з готовими даними.
     */
    BasicDataOperationUsingMap(Hashtable<Ferret, String> hashtable, LinkedHashMap<Ferret, String> linkedHashMap) {
        this.hashtable = hashtable;
        this.linkedHashMap = linkedHashMap;
    }

    /**
     * Виконує комплексні операції з Map.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з Hashtable
        System.out.println("========= Операції з Hashtable =========");
        System.out.println("Початковий розмір Hashtable: " + hashtable.size());

        // Операції пошуку та сортування
        findByKeyInHashtable();
        findByValueInHashtable();

        printHashtable("Початковий стан (порядок не гарантований)");
        sortHashtable();
        printHashtable("Після сортування за Ferret (nickname ASC, foodRation ASC)");

        // Повторні операції пошуку після сортування
        findByKeyInHashtable();
        findByValueInHashtable();

        // Операції додавання/видалення
        addEntryToHashtable();
        removeByKeyFromHashtable();
        removeByValueFromHashtable();

        System.out.println("Кінцевий розмір Hashtable: " + hashtable.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashMap.size());

        // Операції пошуку та сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        printLinkedHashMap("Початковий стан (збережений порядок додавання)");
        sortLinkedHashMap();
        printLinkedHashMap("Після сортування за Ferret (nickname ASC, foodRation ASC)");

        // Повторні операції пошуку після сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        // Операції додавання/видалення
        addEntryToLinkedHashMap();
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();

        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashMap.size());
    }


    // ===== Методи для Hashtable =====

    private void printHashtable(String title) {
        System.out.println("\n=== Пари ключ-значення в Hashtable (" + title + ") ===");
        long timeStart = System.nanoTime();

        // Використовуємо Stream API для сортування перед виведенням, щоб відобразити порядок
        // Тут використовуємо FERRET_COMPARATOR, щоб показати, як виглядають відсортовані дані
        hashtable.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .forEach(entry ->
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
            );

        // PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в Hashtable");
        System.out.println("Час операції (виведення пари ключ-значення в Hashtable): " + (System.nanoTime() - timeStart) + " нс");
    }

    private void sortHashtable() {
        long timeStart = System.nanoTime();

        // Сортування Hashtable за ключами з використанням FERRET_COMPARATOR
        // Створюємо нову Hashtable з відсортованих Map.Entry
        hashtable = hashtable.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1, // Об'єднання: залишаємо старе значення при конфлікті ключів
                Hashtable::new // Створюємо новий Hashtable
            ));

        // PerformanceTracker.displayOperationTime(timeStart, "сортування Hashtable за ключами");
        System.out.println("Час операції (сортування Hashtable за ключами): " + (System.nanoTime() - timeStart) + " нс");
    }

    void findByKeyInHashtable() {
        long timeStart = System.nanoTime();
        boolean found = hashtable.containsKey(KEY_TO_SEARCH_AND_DELETE);
        System.out.println("Час операції (пошук за ключем в Hashtable): " + (System.nanoTime() - timeStart) + " нс");

        if (found) {
            String value = hashtable.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("✅ Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("❌ Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    void findByValueInHashtable() {
        long timeStart = System.nanoTime();
        Map.Entry<Ferret, String> foundEntry = hashtable.entrySet().stream()
            .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
            .findFirst()
            .orElse(null);
        System.out.println("Час операції (пошук за значенням в Hashtable): " + (System.nanoTime() - timeStart) + " нс");

        if (foundEntry != null) {
            System.out.println("✅ Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Ferret: " + foundEntry.getKey());
        } else {
            System.out.println("❌ Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    void addEntryToHashtable() {
        long timeStart = System.nanoTime();
        hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);
        System.out.println("Час операції (додавання запису до Hashtable): " + (System.nanoTime() - timeStart) + " нс");
        System.out.println("✅ Додано новий запис: Ferret='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromHashtable() {
        long timeStart = System.nanoTime();
        String removedValue = hashtable.remove(KEY_TO_SEARCH_AND_DELETE);
        System.out.println("Час операції (видалення за ключем з Hashtable): " + (System.nanoTime() - timeStart) + " нс");

        if (removedValue != null) {
            System.out.println("✅ Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("❌ Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromHashtable() {
        long timeStart = System.nanoTime();
        List<Ferret> keysToRemove = hashtable.entrySet().stream()
            .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        keysToRemove.forEach(hashtable::remove);
        System.out.println("Час операції (видалення за значенням з Hashtable): " + (System.nanoTime() - timeStart) + " нс");
        System.out.println("✅ Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    private void printLinkedHashMap(String title) {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap (" + title + ") ===");
        long timeStart = System.nanoTime();

        // Оскільки LinkedHashMap зберігає порядок вставки, для сортування необхідно створити потік
        // Тут використовуємо FERRET_COMPARATOR, щоб показати, як виглядають відсортовані дані
        linkedHashMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .forEach(entry ->
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
            );

        System.out.println("Час операції (виведення пар ключ-значення в LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");
    }

    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Сортування LinkedHashMap за ключами з використанням FERRET_COMPARATOR
        // Створюємо нову LinkedHashMap з відсортованих Map.Entry
        linkedHashMap = linkedHashMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new // Зберігаємо порядок сортування у новій LinkedHashMap
            ));

        System.out.println("Час операції (сортування LinkedHashMap за ключами): " + (System.nanoTime() - timeStart) + " нс");
    }

    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();
        boolean found = linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);
        System.out.println("Час операції (пошук за ключем в LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");

        if (found) {
            String value = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("✅ Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("❌ Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();
        Map.Entry<Ferret, String> foundEntry = linkedHashMap.entrySet().stream()
            .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
            .findFirst()
            .orElse(null);
        System.out.println("Час операції (пошук за значенням в LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");

        if (foundEntry != null) {
            System.out.println("✅ Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Ferret: " + foundEntry.getKey());
        } else {
            System.out.println("❌ Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();
        linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
        System.out.println("Час операції (додавання запису до LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");
        System.out.println("✅ Додано новий запис: Ferret='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();
        String removedValue = linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);
        System.out.println("Час операції (видалення за ключем з LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");

        if (removedValue != null) {
            System.out.println("✅ Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("❌ Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();
        List<Ferret> keysToRemove = linkedHashMap.entrySet().stream()
            .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        keysToRemove.forEach(linkedHashMap::remove);
        System.out.println("Час операції (видалення за значенням з LinkedHashMap): " + (System.nanoTime() - timeStart) + " нс");
        System.out.println("✅ Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Створюємо початкові дані для Hashtable
        Hashtable<Ferret, String> hashtable = new Hashtable<>();
        hashtable.put(new Ferret("Аміго", "мясо"), "Олександр");
        hashtable.put(new Ferret("Бандит", "корм"), "Марія");
        hashtable.put(new Ferret("Вінні", "фрукти"), "Андрій");
        hashtable.put(new Ferret("Гоша", "мясо"), "Софія");
        hashtable.put(new Ferret("Бандит", "овочі"), "Дмитро");
        hashtable.put(new Ferret("Джек", "корм"), "Наталя");
        hashtable.put(new Ferret("Єшка", "фрукти"), "Андрій");
        hashtable.put(new Ferret("Жорик", "мясо"), "Ірина");
        hashtable.put(new Ferret("Зевс", "овочі"), "Марія");
        hashtable.put(new Ferret("Ікар", "корм"), "Олена");

        // Створюємо початкові дані для LinkedHashMap
        LinkedHashMap<Ferret, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(new Ferret("Аміго", "мясо"), "Олександр");
        linkedHashMap.put(new Ferret("Бандит", "корм"), "Марія");
        linkedHashMap.put(new Ferret("Вінні", "фрукти"), "Андрій");
        linkedHashMap.put(new Ferret("Гоша", "мясо"), "Софія");
        linkedHashMap.put(new Ferret("Бандит", "овочі"), "Дмитро");
        linkedHashMap.put(new Ferret("Джек", "корм"), "Наталя");
        linkedHashMap.put(new Ferret("Єшка", "фрукти"), "Андрій");
        linkedHashMap.put(new Ferret("Жорик", "мясо"), "Ірина");
        linkedHashMap.put(new Ferret("Зевс", "овочі"), "Марія");
        linkedHashMap.put(new Ferret("Ікар", "корм"), "Олена");


        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(hashtable, linkedHashMap);
        operations.executeDataOperations();
    }
}