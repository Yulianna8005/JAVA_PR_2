import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;

/**
 * Клас BasicDataOperationUsingSet реалізує операції з множиною TreeSet для LocalTime.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив LocalTime.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві LocalTime.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить граничні значення в масиві.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині LocalTime.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeArrayAndSet()} - Аналізує елементи масиву та множини.</li>
 * </ul>
 */
public class BasicDataOperationUsingSet {
    LocalTime localTimeValueToSearch;
    LocalTime[] localTimeArray;
    Set<LocalTime> dateTimeSet = new TreeSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param localTimeValueToSearch Значення для пошуку
     * @param localTimeArray Масив LocalTime
     */
    BasicDataOperationUsingSet(LocalTime localTimeValueToSearch, LocalTime[] localTimeArray) {
        this.localTimeValueToSearch = localTimeValueToSearch;
        this.localTimeArray = localTimeArray;
        this.dateTimeSet = new TreeSet<>(Arrays.asList(localTimeArray));
    }
    
    /**
     * Запускає комплексний аналіз даних з використанням множини TreeSet.
     * 
     * Метод завантажує дані, виконує операції з множиною та масивом LocalTime.
     */
    public void executeDataAnalysis() {
        // спочатку аналізуємо множину дати та часу
        findInSet();
        locateMinMaxInSet();
        analyzeArrayAndSet();

        // потім обробляємо масив
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(localTimeArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів LocalTime за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    private void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(localTimeArray);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву дати i часу");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    private void findInArray() {
        long timeStart = System.nanoTime();

        int position = Arrays.binarySearch(this.localTimeArray, localTimeValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi дати i часу");

        if (position >= 0) {
            System.out.println("Елемент '" + localTimeValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + localTimeValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві LocalTime.
     */
    private void locateMinMaxInArray() {
        if (localTimeArray == null || localTimeArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalTime minValue = localTimeArray[0];
        LocalTime maxValue = localTimeArray[0];

        for (LocalTime currentDateTime : localTimeArray) {
            if (currentDateTime.isBefore(minValue)) {
                minValue = currentDateTime;
            }
            if (currentDateTime.isAfter(maxValue)) {
                maxValue = currentDateTime;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в масивi");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Здійснює пошук конкретного значення в множині дати та часу.
     */
    private void findInSet() {
        long timeStart = System.nanoTime();
        
        boolean elementExists = dateTimeSet.stream()
            .anyMatch(dateTime -> dateTime.equals(localTimeValueToSearch));
        
        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в TreeSet дати i часу");

        if (elementExists) {
            System.out.println("Елемент '" + localTimeValueToSearch + "' знайдено в TreeSet");
        } else {
            System.out.println("Елемент '" + localTimeValueToSearch + "' відсутній в TreeSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині LocalTime.
     */
    private void locateMinMaxInSet() {
        if (dateTimeSet == null || dateTimeSet.isEmpty()) {
            System.out.println("TreeSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalTime minValue = dateTimeSet.stream()
            .min(LocalTime::compareTo)
            .orElse(null);
        
        LocalTime maxValue = dateTimeSet.stream()
            .max(LocalTime::compareTo)
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в TreeSet");

        System.out.println("Найменше значення в TreeSet: " + minValue);
        System.out.println("Найбільше значення в TreeSet: " + maxValue);
    }

    /**
     * Аналізує та порівнює елементи масиву та множини.
     */
private void analyzeArrayAndSet() {
    System.out.println("Кiлькiсть елементiв в масивi: " + localTimeArray.length);
    System.out.println("Кiлькiсть елементiв в TreeSet: " + dateTimeSet.size());

    boolean allElementsPresent = Arrays.stream(localTimeArray)
        .allMatch(dateTimeSet::contains);

    if (allElementsPresent) {
        System.out.println("Всi елементи масиву наявні в TreeSet.");
    } else {
        System.out.println("Не всi елементи масиву наявні в TreeSet.");
    }
}
}