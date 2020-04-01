package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * stores information about the user's score
 */
public class ScoreStats {
    /**
     * List that contains the user's score for each day
     */
    private List<Integer> scoreList;

    private int currentScore;

    public ScoreStats() {
        this.initList();
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    /**
     * initializes a scoreList with zero score for all days
     */
    public void initList() {
        scoreList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            scoreList.add(0);
        }
    }

    /**
     * copies the currentScore and scoreList from the scoreStats
     * @param scoreStats the scoreStats to be copied from
     */
    public void copy(ScoreStats scoreStats) {
        this.currentScore = scoreStats.getCurrentScore();
        this.scoreList = scoreStats.getScoreList();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * increments the score at current day index
     *
     * @param dayIndex the day in which the score is to be added
     */
    public void addScore(int dayIndex, int scoreToAdd) {
        currentScore += scoreToAdd;
        scoreList.set(dayIndex, currentScore);
    }

    /**
     * Returns user's score on the given day
     *
     * @param dayIndex the day of interest
     * @return user's score on the given day
     */
    public int getDailyScore(int dayIndex) {
        return scoreList.get(dayIndex);
    }

    /**
     * Returns a list containing the user's daily scores over the past 7 days
     * @param dayIndex the day of interest
     * @return a list containing the user's daily scores over the past 7 days
     */
    public List<Integer> getWeeklyCompleteCount(int dayIndex) {
        return scoreList.subList(dayIndex - 6, dayIndex + 1);
    }
}

