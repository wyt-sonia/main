package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores statistics about number of Overdue tasks
 */
public class OverdueStats {
    /**
     * A list containing daily number of Overdue tasks
     */
    private List<Integer> OverdueCountList;

    public OverdueStats() {
        this.initList();
    }

    public List<Integer> getOverdueCountList() {
        return OverdueCountList;
    }

    public void setOverdueCountList(List<Integer> OverdueCountList) {
        this.OverdueCountList = OverdueCountList;
    }

    /**
     * initializes an empty OverdueCountList
     */
    public void initList() {
        OverdueCountList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            OverdueCountList.add(0);
        }
    }

    /**
     * increments the completeLate count at current day index
     *
     * @param dayIndex the day in which the task is completed
     */
    public void addOverdue(int dayIndex) {
        int currentOverdueCount = OverdueCountList.get(dayIndex);
        OverdueCountList.set(dayIndex, currentOverdueCount + 1);
    }

    /**
     * Returns number of task completed late on the given day
     *
     * @param dayIndex
     * @return number of tasks completed late on the given day
     */
    public int getDailyOverdueCount(int dayIndex) {
        return OverdueCountList.get(dayIndex);
    }


    public List<Integer> getWeeklyOverdueCountList(int dayIndex) {
        return OverdueCountList.subList(dayIndex - 6, dayIndex + 1);
    }
}
