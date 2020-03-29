package draganddrop.studybuddy.model.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Counts number of overdue tasks
 */
public class OverdueCountList {
    private List<Integer> overdueCountList;

    /**
     * initializes an empty overdueCountList
     */
    public void initList() {
        overdueCountList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            overdueCountList.add(0);
        }
    }

    /**
     * increments the completeLate count at current day index
     *
     * @param dayIndex the day in which the task is completed
     */
    public void addOverdue(int dayIndex) {
        int currentLateCount = overdueCountList.get(dayIndex);
        if (currentLateCount != 0) {
            overdueCountList.set(dayIndex, 1);
        } else {
            overdueCountList.set(dayIndex, currentLateCount + 1);
        }
    }

    /**
     * Returns number of task completed late on the given day
     *
     * @param dayIndex
     * @return number of tasks completed late on the given day
     */
    public int getDailyOverdueCount(int dayIndex) {
        return overdueCountList.get(dayIndex);
    }

}
