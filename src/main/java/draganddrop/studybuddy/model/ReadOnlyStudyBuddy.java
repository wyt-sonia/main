package draganddrop.studybuddy.model;

import java.util.List;

import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.statistics.GeneralStats;
import draganddrop.studybuddy.model.statistics.ScoreStats;
import draganddrop.studybuddy.model.task.Task;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an study buddy
 */
public interface ReadOnlyStudyBuddy {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */

    ObservableList<Task> getArchivedList();

    ObservableList<Task> getDueSoonList();

    ObservableList<Module> getModuleList();

    /**
     * Returns an unmodifiable view of the task list.
     * This list will not contain any duplicate tasks.
     */
    ObservableList<Task> getTaskList();

    // Statistics
    List<Integer> getCompleteCountList();

    List<Integer> getOverdueCountList();

    ScoreStats getScoreStats();

    GeneralStats getGeneralStats();


}
