package draganddrop.studybuddy.model.user;

import java.time.LocalDateTime;

import draganddrop.studybuddy.model.task.Task;

/**
 * Statistics about number of tasks completed
 */
public class CompletionStats {
    private CompleteCountList completeCountList;

    public CompletionStats(CompleteCountList completeCountList) {
        this.completeCountList = completeCountList;
    }

    /**
     * Records the completed task
     *
     * @param completedTask the task that has just been completed
     */
    public void recordCompletedTask(Task completedTask) {
        LocalDateTime timeOfCompletion = completedTask.getFinishDateTime();
        int dayIndex = StatsUtil.getDayIndex();
        completeCountList.complete(dayIndex);
    }

    /**
     * get number of tasks completed today
     * @return returns the number of tasks completed today
     */
    public int getCompleteCountToday() {
        return completeCountList.getDailyCompleteCount(StatsUtil.getDayIndex());
    }

}
