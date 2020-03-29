package draganddrop.studybuddy.model.statistics;

/**
 * Usage statistics for the user. These statistics are used on the productivity page.
 */
public class Statistics {
    private int points;
    private CompletionStats completionStats;
    private OverdueStats overdueStats;

    public Statistics(int points, CompletionStats completionStats, OverdueStats overdueStats) {
        this.points = points;
        this.completionStats = completionStats;
        this.overdueStats = overdueStats;
    }
}
