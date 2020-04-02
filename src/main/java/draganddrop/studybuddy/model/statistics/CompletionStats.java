package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores statistics about number of tasks completed
 */
public class CompletionStats {
    /**
     * List that contains the count of completed task for each day
     */
    private List<Integer> completeCountList;

    public CompletionStats() {
        this.initList();
    }

    public List<Integer> getCompleteCountList() {
        return completeCountList;
    }

    public void setCompleteCountList(List<Integer> completeCountList) {
        this.completeCountList = completeCountList;
    }

    /**
     * initializes an empty completeCount List
     */
    public void initList() {
        completeCountList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            completeCountList.add(0);
        }
    }

    /**
     * increments the complete count at current day index
     *
     * @param dayIndex the day in which the task is completed
     */
    public void complete(int dayIndex) {
        int currentCount = completeCountList.get(dayIndex);
        completeCountList.set(dayIndex, currentCount + 1);
    }

    /**
     * Returns number of task completed on the given day
     *
     * @param dayIndex the day of interest
     * @return number of tasks completed on the given day
     */
    public int getDailyCompleteCount(int dayIndex) {
        return completeCountList.get(dayIndex);
    }

    /**
     * Returns a list containing the number of tasks completed daily over the past 7 days
     * @param dayIndex the day of interest
     * @return a list containing the number of tasks completed daily over the past 7 days
     */
    public List<Integer> getWeeklyCompleteCountList(int dayIndex) {
        return completeCountList.subList(dayIndex - 6, dayIndex + 1);
    }
}
