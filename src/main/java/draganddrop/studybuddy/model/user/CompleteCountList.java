package draganddrop.studybuddy.model.user;

import java.util.ArrayList;
import java.util.List;

/**
 * List that contains the count of completed task for each day
 */
public class CompleteCountList {
    private static List<Integer> completeList;
    private static List<Integer> completeLateList;

    public CompleteCountList () {
        this.initList();
    }

    /**
     * initializes an empty completeCount List
     */
    public void initList() {
        completeList = new ArrayList<>();
        completeLateList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            completeList.add(0);
            completeLateList.add(0);
        }
    }

    /**
     * increments the complete count at current day index
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
     * increments the completeLate count at current day index
     * @param dayIndex the day in which the task is completed
     */
    public void completeLate(int dayIndex) {
        int currentLateCount = completeLateList.get(dayIndex);
        if (currentLateCount != 0) {
            completeLateList.set(dayIndex, 1);
        } else {
            completeLateList.set(dayIndex, currentLateCount + 1);
        }
    }

    /**
     * Returns number of task completed on the given day
     * @param dayIndex
     * @return number of tasks completed on the given day
     */
    public int getDailyCompleteCount(int dayIndex) {
        return completeList.get(dayIndex);
    }

    /**
     * Returns number of task completed late on the given day
     * @param dayIndex
     * @return number of tasks completed late on the given day
     */
    public int getDailyLateCount(int dayIndex) {
        return completeLateList.get(dayIndex);
    }

}
