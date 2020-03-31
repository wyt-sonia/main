package draganddrop.studybuddy.model.statistics;

import java.time.LocalDateTime;

import draganddrop.studybuddy.model.task.Task;

/**
 * Usage statistics for the user. These statistics are used on the productivity page.
 */
public class Statistics {
    private GeneralStats generalStats;
    private CompletionStats completionStats;
    private OverdueStats overdueStats;

    public Statistics(GeneralStats generalStats, CompletionStats completionStats, OverdueStats overdueStats) {
        this.generalStats = generalStats;
        this.completionStats = completionStats;
        this.overdueStats = overdueStats;
    }

    public Statistics() {
    }

    public void setGeneralStats(GeneralStats generalStats) {
        this.generalStats = generalStats;
    }

    public void setCompletionStats(CompletionStats completionStats) {
        this.completionStats = completionStats;
    }

    public void setOverdueStats(OverdueStats overdueStats) {
        this.overdueStats = overdueStats;
    }


    /**
     * Records the completed task
     *
     * @param completedTask the task that has just been completed
     */
    public void recordCompletedTask(Task completedTask) {
        LocalDateTime timeOfCompletion = completedTask.getFinishDateTime();
        int dayIndex = StatsUtil.getDayIndex();
        completionStats.complete(dayIndex);
    }

    /**
     * get number of tasks completed today
     *
     * @return returns the number of tasks completed today
     */
    public int getCompleteCountToday() {
        return completionStats.getDailyCompleteCount(StatsUtil.getDayIndex());
    }

    /**
     * Records an overdue task
     *
     * @param overdueTask task that has gone overdue
     */
    public void recordOverdueTask(Task overdueTask) {
        int dayIndex = StatsUtil.getDayIndex();
        overdueStats.addOverdue(dayIndex);
    }

    /**
     * get number of tasks completed late today
     *
     * @return returns the number ofo tasks completed late today
     */
    public int getLateCountToday() {
        return overdueStats.getDailyOverdueCount(StatsUtil.getDayIndex());
    }


}
