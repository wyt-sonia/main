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
    private ScoreStats scoreStats;

    public Statistics(GeneralStats generalStats, CompletionStats completionStats,
          OverdueStats overdueStats, ScoreStats scoreStats) {
        this.generalStats = generalStats;
        this.completionStats = completionStats;
        this.overdueStats = overdueStats;
        this.scoreStats = scoreStats;
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
     * @return returns the number of tasks completed late today
     */
    public int getLateCountToday() {
        return overdueStats.getDailyOverdueCount(StatsUtil.getDayIndex());
    }

    /**
     * get today's score
     * @return today's score
     */
    public int getScoreToday() {
        return scoreStats.getDailyScore(StatsUtil.getDayIndex());
    }

    /**
     * adds to the current score
     * @param scoreToAdd score to be added
     */
    public void addScore(int scoreToAdd) {
        scoreStats.addScore(StatsUtil.getDayIndex(), scoreToAdd);
    }
}
