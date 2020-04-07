package draganddrop.studybuddy.model.statistics;

import java.util.List;

import draganddrop.studybuddy.model.task.Task;

/**
 * Usage statistics for the user. These statistics are used on the productivity page.
 */
public class Statistics {
    private GeneralStats generalStats;
    private CompletionStats completionStats;
    private OverdueStats OverdueStats;
    private ScoreStats scoreStats;

    public Statistics(GeneralStats generalStats, CompletionStats completionStats,
          OverdueStats OverdueStats, ScoreStats scoreStats) {
        this.generalStats = generalStats;
        this.completionStats = completionStats;
        this.OverdueStats = OverdueStats;
        this.scoreStats = scoreStats;
    }

    // Completion Stats
    /**
     * Records the completed task
     *
     * @param completedTask the task that has just been completed
     */
    public void recordCompletedTask(Task completedTask) {
        int dayIndex = StatsUtil.getDayIndex();
        addScore(10);
        completionStats.complete(dayIndex);
    }

    /**
     * Records the added task
     * @param addedTask
     */
    public void recordAddedTask(Task addedTask) {
        addScore(2);
    }

    /**
     * get number of tasks completed today
     *
     * @return returns the number of tasks completed today
     */
    public int getCompleteCountToday() {
        return completionStats.getDailyCompleteCount(StatsUtil.getDayIndex());
    }

    public int getCompleteCountThisWeek() {
        List<Integer> weeklyCompleteCountList = completionStats.getWeeklyCompleteCountList(StatsUtil.getDayIndex());
        return StatsUtil.sumIntegerList(weeklyCompleteCountList);
    }

    public List<Integer> getWeeklyCompleteCountList() {
        return completionStats.getWeeklyCompleteCountList(StatsUtil.getDayIndex());
    }

    public int getOverdueCountThisWeek() {
        List<Integer> weeklyOverdueCountList = OverdueStats.getWeeklyOverdueCountList(StatsUtil.getDayIndex());
        return StatsUtil.sumIntegerList(weeklyOverdueCountList);
    }

    // Overdue stats
    /**
     * Records an Overdue task
     *
     * @param OverdueTask task that has gone Overdue
     */
    public void recordOverdueTask(Task OverdueTask) {
        int dayIndex = StatsUtil.getDayIndex();
        OverdueStats.addOverdue(dayIndex);
        addScore(-1);
    }

    /**
     * get number of tasks completed late today
     *
     * @return returns the number of tasks completed late today
     */
    public int getLateCountToday() {
        return OverdueStats.getDailyOverdueCount(StatsUtil.getDayIndex());
    }

    // Score stats
    /**
     * get today's score
     * @return today's score
     */
    public int getScoreToday() {
        return scoreStats.getDailyScore(StatsUtil.getDayIndex());
    }

    public List<Integer> getWeeklyScores() {
        return scoreStats.getWeeklyScores(StatsUtil.getDayIndex());
    }

    /**
     * adds to the current score
     * @param scoreToAdd score to be added
     */
    public void addScore(int scoreToAdd) {
        scoreStats.addScore(StatsUtil.getDayIndex(), scoreToAdd);
    }

    /**
     * get user's goal for number of tasks
     * @return user's goal
     */
    public int getGoal() {
        return generalStats.getGoal();
    }

    /**
     * set user's goal
     * @param goal user's goal
     */
    public void setGoal(int goal) {
        generalStats.setGoal(goal);
    }

    /**
     * get user's current streak;
     * @return user's streak
     */
    public int getStreak() {
        return generalStats.getStreak();
    }

    public void addStreak() {
        generalStats.addStreak();
    }

    /**
     * return user's rank
     * @return user's rank
     */
    public String getRank() {
        return scoreStats.getRank();
    }

    /**
     * return a String of the user's next rank
     * @return user's next rank
     */
    public String getNextRank() {
        return scoreStats.getNextRank();
    }

    /**
     * returns the score required to achieve the next rank
     * @return score required
     */
    public int getScoreToNextRank() {
        return scoreStats.getScoreToNextRank();
    }

}
