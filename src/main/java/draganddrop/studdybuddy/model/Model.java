package draganddrop.studdybuddy.model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import draganddrop.studdybuddy.commons.core.GuiSettings;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.TaskType;

import javafx.collections.ObservableList;
<<<<<<< HEAD:src/main/java/seedu/address/model/Model.java
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.module.Module;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskType;
=======
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/model/Model.java


/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Task> PREDICATE_SHOW_ALL_TASKS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getStudyBuddyFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setStudyBuddyFilePath(Path studyBuddyFilePath);

    /**
     * Returns the StudyBuddy
     */
    ReadOnlyStudyBuddy getStudyBuddy();

    /**
     * Replaces address book data with the data in {@code studyBuddy}.
     */
    void setStudyBuddy(ReadOnlyStudyBuddy studyBuddy);

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task list.
     */
    boolean hasTask(Task task);

    public void deleteDueSoonTask(Task target);
    /**
     * completes the given task.
     * The task must exist in the address book.
     */
    void completeTask(Task target);

    /**
     * Set the task name
     * @param target a task
     * @param newTaskName the new name of the task
     */
    void setTaskName(Task target, String newTaskName);

    /**
     * Set the task type
     * @param target a task
     * @param newTaskType the new task type
     */
    void setTaskType(Task target, TaskType newTaskType);

    /**
     * Set the task date time
     * @param target a task
     * @param newDateTimes the new date and time
     */
    void setTaskDateTime(Task target, LocalDateTime[] newDateTimes);

    void setTaskMod(Task target, Module mod) throws ModuleCodeException;

    /**
     * Deletes the given task.
     * The task must exist in the list.
     */
    void deleteTask(Task task);

    /**
     * Archives the given task.
     *
     * @param task must not already exist in the address book.
     */
    void archiveTask(Task task);

    /**
     * Adds the given task.
     *
     * @param task must not already exist in the address book.
     */
    void addDueSoonTask(Task task);

    /**
     * Adds the given task.
     * {@code task} must not already exist in the task list.
     */
    void addTask(Task task);

    void sortDueSoonTasks();

    void setTask(Task target, Task editedTask);
    boolean hasMod(Module mod);

    void addMod(Module mod);


    /**
     * Returns an unmodifiable view of the filtered task list
     */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Returns an unmodifiable view of the filtered archived task list.
     */
    ObservableList<Task> getFilteredArchivedTaskList();

    /**
     * Returns a List of modules
     * @return
     */
    ObservableList<Module> getFilteredModuleList();

    /**
     * Returns an unmodifiable view of the filtered archived task list.
     */
    ObservableList<Task> getFilteredDueSoonTaskList();


    /**
     * Updates the filter of the filtered task list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    /**
     * Sort tasks by the given {@code keyword}.
     */
    void sortTasks(String keyword);


}
