package draganddrop.studybuddy.model.statistics;

/**
 * stores general statistics
 */
public class GeneralStats {
    private int goal;

    private GeneralStats(int goal) {
        super();
        this.goal = goal;
    }

    public GeneralStats() {
        super();
    }

    /**
     * Copies the values of this generalStats object
     */
    public void copy(GeneralStats generalStats) {
        this.goal = generalStats.getGoal();
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getGoal() {
        return goal;
    }
}
