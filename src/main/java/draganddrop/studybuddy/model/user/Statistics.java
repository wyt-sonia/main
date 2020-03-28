package draganddrop.studybuddy.model.user;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import draganddrop.studybuddy.model.task.Task;

/**
 * Usage statistics for the user
 */
public class Statistics {

    private static CompleteCountList completeCountList;

    public Statistics() {
        completeCountList = new CompleteCountList();
    }

    /**
     * Records the completed task
     * @param completedTask the task that has just been completed
     */
    public static void recordCompletedTask(Task completedTask) {
        LocalDateTime timeOfCompletion = completedTask.getFinishDateTime();
        int dayIndex = getDayIndex();
        completeCountList.complete(dayIndex);
        LocalDateTime taskDueDate = completedTask.getDueDate();
        if (isLate(taskDueDate)) {
            completeCountList.completeLate(dayIndex);
        }
    }

    /**
     * utility method to get the number of days between two LocalDateTime
     * @param first the first LocalDateTime
     * @param second the second LocalDateTime
     * @return the absolute difference in number of days between the two LocalDateTime
     */
    public static int getDaysBetween(LocalDateTime first, LocalDateTime second) {
        return (int) Math.abs(ChronoUnit.DAYS.between(first, second));
    }

    /**
     * get the current day, which is used as index for complete count list
     * @return the number of days from the start of year. 0 if the day is the start of year itself
     */
    public static int getDayIndex() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.with(firstDayOfYear());
        return getDaysBetween(now, startOfYear);
    }

    /**
     * checks if the task is completed late
     * @param dueDate date that the task is due on
     * @return true if the task is completed late.
     */
    public static boolean isLate(LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(now, dueDate) < 0;
    }
}
