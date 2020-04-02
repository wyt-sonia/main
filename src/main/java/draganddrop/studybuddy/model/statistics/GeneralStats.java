package draganddrop.studybuddy.model.statistics;

/**
 * stores general statistics
 */
public class GeneralStats {
    private int goal;
    private int streak;

    public GeneralStats() {
        super();
        this.goal = 5;
    }

    /**
     * Copies the values of this generalStats object
     */
    public void copy(GeneralStats generalStats) {
        this.goal = generalStats.getGoal();
        this.streak = generalStats.getStreak();
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getGoal() {
        return goal;
    }

    public int getStreak() {
        return streak;
    }

    public void addStreak() {
        streak += 1;
    }

}
