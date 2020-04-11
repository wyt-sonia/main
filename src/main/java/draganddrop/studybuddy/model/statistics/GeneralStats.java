package draganddrop.studybuddy.model.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * stores general statistics
 */
public class GeneralStats {
    private int goal;
    private int streak;
    private List<Boolean> isStreakAddedList;
    private GoalObserver goalObserver;

    public GeneralStats() {
        super();
        this.goal = 5;
        initStreakList();
    }

    /**
     * sets the observer to be the given GoalObserver
     * @param goalObserver the given GoalObserver
     */
    public void setObserver(GoalObserver goalObserver) {
        this.goalObserver = goalObserver;
    }

    /**
     * initializes a streak list with all false values
     */
    public void initStreakList() {
        isStreakAddedList = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            isStreakAddedList.add(false);
        }
    }

    /**
     * Copies the values of this generalStats object
     */
    public void copy(GeneralStats generalStats) {
        this.goal = generalStats.getGoal();
        this.streak = generalStats.getStreak();
        this.isStreakAddedList = generalStats.getIsStreakAddedList();
    }

    public List<Boolean> getIsStreakAddedList() {
        return isStreakAddedList;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        if (goalObserver != null) {
            goalObserver.update();
        }
    }

    public int getGoal() {
        return goal;
    }

    public int getStreak() {
        return streak;
    }

    /**
     * adds to the streak only if streak has not already been added today
     * @return true if streak is added
     */
    public boolean addStreak() {
        int dayIndex = StatsUtil.getDayIndex();
        if (!isStreakAddedList.get(dayIndex)) {
            isStreakAddedList.set(dayIndex, true);
            streak += 1;
            return true;
        } else {
            return false;
        }
    }
}
