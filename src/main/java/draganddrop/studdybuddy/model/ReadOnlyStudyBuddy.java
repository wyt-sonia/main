package draganddrop.studdybuddy.model;

import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.task.Task;

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

}
