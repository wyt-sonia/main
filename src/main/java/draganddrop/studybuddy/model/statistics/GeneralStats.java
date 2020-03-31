package draganddrop.studybuddy.model.statistics;

/**
 * stores general statistics
 */
public class GeneralStats {
    private int score;

    private GeneralStats(int score) {
        super();
        this.score = score;
    }

    public GeneralStats() {
        super();
    }

    /**
     * Copies the values of this generalStats object
     */
    public void copy(GeneralStats generalStats) {
        this.score = generalStats.getScore();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
