package draganddrop.studybuddy.model.user;

import draganddrop.studybuddy.model.task.Task;

/**
 * Statistics about number of overdue tasks
 */
public class OverdueStats {

    private OverdueCountList overdueCountList;

    public OverdueStats(OverdueCountList overdueCountList) {
        this.overdueCountList = overdueCountList;
    }

    /**
     * Records an overdue task
     *
     * @param overdueTask task that has gone overdue
     */
    public void recordOverdueTask(Task overdueTask) {
        int dayIndex = StatsUtil.getDayIndex();
        overdueCountList.addOverdue(dayIndex);
    }

    /**
     * get number of tasks completed late today
     *
     * @return returns the number ofo tasks completed late today
     */
    public int getLateCountToday() {
        return overdueCountList.getDailyOverdueCount(StatsUtil.getDayIndex());
    }

}
