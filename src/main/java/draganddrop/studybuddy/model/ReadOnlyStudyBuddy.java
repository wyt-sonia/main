package draganddrop.studybuddy.model;

import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;

import draganddrop.studybuddy.model.user.Statistics;
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

    /**
     * Returns a statistic object.
     */
    Statistics getStatistics();
}
