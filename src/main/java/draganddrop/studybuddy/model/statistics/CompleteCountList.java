package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * List that contains the count of completed task for each day
 */
public class CompleteCountList {
    private List<Integer> completeCountList;

    public CompleteCountList() {
        this.initList();
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
        if (currentCount != 0) {
            completeCountList.set(dayIndex, 1);
        } else {
            completeCountList.set(dayIndex, currentCount + 1);
        }
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
}
