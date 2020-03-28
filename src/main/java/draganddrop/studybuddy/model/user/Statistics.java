package draganddrop.studybuddy.model.user;

import draganddrop.studybuddy.model.task.Task;

/**
 * Usage statistics for the user
 */
public class Statistics {
    // total
    private static int totalCompleteCount = 0;
    private static int completeLateCount = 0;
    private static int completeOnTimeCount = 0;
    // weekly
    private static int weeklyCompleteCount = 0;
    private static int weeklyCompleteLateCount = 0;
    private static int weeklyCompleteOnTimeCount = 0;
    // daily
    private static int dailyCompleteCount = 0;
    private static int dailyCompleteLateCount = 0;
    private static int dailyCompleteOnTimeCount = 0;

    /**
     * Records the completed task
     * @param completedTask
     */
    public static void recordCompletedTask(Task completedTask) {
        totalCompleteCount++;



    }
}
