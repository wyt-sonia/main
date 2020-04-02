package draganddrop.studybuddy.model.statistics;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods for handling statistics
 */
public class StatsUtil {
    /**
     * utility method to get the number of days between two LocalDateTime
     *
     * @param first  the first LocalDateTime
     * @param second the second LocalDateTime
     * @return the absolute difference in number of days between the two LocalDateTime
     */
    public static int getDaysBetween(LocalDateTime first, LocalDateTime second) {
        return (int) Math.abs(ChronoUnit.DAYS.between(first, second));
    }

    /**
     * get the current day, which is used as index for complete count list
     *
     * @return the number of days from the start of year. 0 if the day is the start of year itself
     */
    public static int getDayIndex() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.with(firstDayOfYear());
        return getDaysBetween(now, startOfYear);
    }

    /**
     * checks if the task is completed late
     *
     * @param dueDate date that the task is due on
     * @return true if the task is completed late.
     */
    public static boolean isLate(LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(now, dueDate) < 0;
    }

    public static int sumIntegerList(List<Integer> integerList) {
        return integerList.stream()
            .mapToInt(x -> x)
            .sum();
    }

    public static List<String> getDayList() {
        List<String> initialList = new ArrayList<>(
            List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDateTime.now());
        int dayOfWeekValue = dayOfWeek.getValue();
        return initialList.subList(dayOfWeekValue, dayOfWeekValue + 7);
    }
}
