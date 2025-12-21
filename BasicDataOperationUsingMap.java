import java.util.ArrayList;
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
    private final Ferret KEY_TO_SEARCH_AND_DELETE = new Ferret("Бандит", "корм");
    private final Ferret KEY_TO_ADD = new Ferret("Кекс", "фрукти");

    private final String VALUE_TO_SEARCH_AND_DELETE = "Андрій";
    private final String VALUE_TO_ADD = "Богдан";

    private Hashtable<Ferret, String> hashtable;
    private LinkedHashMap<Ferret, String> linkedHashMap;

    /**
     * Компаратор для сортування Map.Entry за значеннями String.
     */
    static class OwnerValueComparator implements Comparator<Map.Entry<Ferret, String>> {
        @Override
        public int compare(Map.Entry<Ferret, String> e1, Map.Entry<Ferret, String> e2) {
            String v1 = e1.getValue();
            String v2 = e2.getValue();
            if (v1 == null && v2 == null) return 0;
            if (v1 == null) return -1;
            if (v2 == null) return 1;
            return v1.compareTo(v2);
        }
    }

        /**
     * Внутрішній клас Ferret для зберігання інформації про тхора.
     * @param nickname
     * @param foodRation
     * Реалізує Comparable<Ferret> для визначення природного порядку сортування.
     * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за раціоном (foodRation) за зростанням.
     */
    public record Ferret(String nickname, String foodRation) {}
    
    /**
     * Компаратор для порівняння об'єктів Ferret.
     * Сортування: спочатку за кличкою (за зростанням), потім за раціоном (за спаданням).
     */
    private static final Comparator<Ferret> FERRET_COMPARATOR = 
        Comparator.comparing(Ferret::nickname).thenComparing(Ferret::foodRation, Comparator.reverseOrder());

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
        
        findByKeyInHashtable();
        findByValueInHashtable();

        printHashtable();
        sortHashtable();
        printHashtable();

        findByKeyInHashtable();
        findByValueInHashtable();

        addEntryToHashtable();
        
        removeByKeyFromHashtable();
        removeByValueFromHashtable();
               
        System.out.println("Кінцевий розмір Hashtable: " + hashtable.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashMap.size());
        
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        printLinkedHashMap();
        sortLinkedHashMap();
        printLinkedHashMap();

        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        addEntryToLinkedHashMap();
        
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();
        
        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashMap.size());
    }


    // ===== Методи для Hashtable =====

    private void printHashtable() {
        System.out.println("\n=== Пари ключ-значення в Hashtable ===");
        long timeStart = System.nanoTime();

        hashtable.entrySet().forEach(entry ->
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
        );

        PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в Hashtable");
    }

    private void sortHashtable() {
        long timeStart = System.nanoTime();

        hashtable = hashtable.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                Hashtable::new
            ));

        PerformanceTracker.displayOperationTime(timeStart, "сортування Hashtable за ключами");
    }

    void findByKeyInHashtable() {
        long timeStart = System.nanoTime();

        boolean found = hashtable.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в Hashtable");

        if (found) {
            String value = hashtable.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    void findByValueInHashtable() {
        long timeStart = System.nanoTime();

        Map.Entry<Ferret, String> foundEntry = hashtable.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
            .findFirst()
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за значенням в Hashtable");

        if (foundEntry != null) {
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Ferret: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    void addEntryToHashtable() {
        long timeStart = System.nanoTime();

        hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до Hashtable");

        System.out.println("Додано новий запис: Ferret='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromHashtable() {
        long timeStart = System.nanoTime();

        String removedValue = hashtable.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з Hashtable");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromHashtable() {
        long timeStart = System.nanoTime();

        List<Ferret> keysToRemove = hashtable.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        keysToRemove.forEach(hashtable::remove);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з Hashtable");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");
        long timeStart = System.nanoTime();

        linkedHashMap.entrySet().forEach(entry ->
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
        );

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в LinkedHashMap");
    }

    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashMap = linkedHashMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(FERRET_COMPARATOR))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();

        boolean found = linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (found) {
            String value = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        Map.Entry<Ferret, String> foundEntry = linkedHashMap.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
            .findFirst()
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за значенням в LinkedHashMap");

        if (foundEntry != null) {
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Ferret: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

        System.out.println("Додано новий запис: Ferret='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Ferret> keysToRemove = linkedHashMap.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        keysToRemove.forEach(linkedHashMap::remove);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
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
        LinkedHashMap<Ferret, String> linkedHashMap = new LinkedHashMap<Ferret, String>() {{
            put(new Ferret("Аміго", "мясо"), "Олександр");
            put(new Ferret("Бандит", "корм"), "Марія");
            put(new Ferret("Вінні", "фрукти"), "Андрій");
            put(new Ferret("Гоша", "мясо"), "Софія");
            put(new Ferret("Бандит", "овочі"), "Дмитро");
            put(new Ferret("Джек", "корм"), "Наталя");
            put(new Ferret("Єшка", "фрукти"), "Андрій");
            put(new Ferret("Жорик", "мясо"), "Ірина");
            put(new Ferret("Зевс", "овочі"), "Марія");
            put(new Ferret("Ікар", "корм"), "Олена");
        }};

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(hashtable, linkedHashMap);
        operations.executeDataOperations();
    }
}