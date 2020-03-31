package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * List that contains the count of completed task for each day
 */
public class CompleteCountList {
    private List<Integer> completeList;

    public CompleteCountList() {}

    public CompleteCountList(List<Integer> completeList) {
        this.completeList = completeList;
    }

    public List<Integer> getCompleteList() {
        return completeList;
    }

    public void setCompleteList(List<Integer> completeList) {
        this.completeList = completeList;
    }
    /**
     * initializes an empty completeCount List
     */
    public void initList() {
        completeList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            completeList.add(0);
        }
    }

    /**
     * increments the complete count at current day index
     *
     * @param dayIndex the day in which the task is completed
     */
    public void complete(int dayIndex) {
        int currentCount = completeList.get(dayIndex);
        if (currentCount != 0) {
            completeList.set(dayIndex, 1);
        } else {
            completeList.set(dayIndex, currentCount + 1);
        }
    }

    /**
     * Returns number of task completed on the given day
     *
     * @param dayIndex the day of interest
     * @return number of tasks completed on the given day
     */
    public int getDailyCompleteCount(int dayIndex) {
        return completeList.get(dayIndex);
    }
}
