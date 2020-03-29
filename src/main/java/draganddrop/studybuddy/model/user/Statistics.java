package draganddrop.studybuddy.model.user;

/**
 * Usage statistics for the user
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
