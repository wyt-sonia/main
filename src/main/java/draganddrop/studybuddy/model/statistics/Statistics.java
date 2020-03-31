package draganddrop.studybuddy.model.statistics;

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

}
