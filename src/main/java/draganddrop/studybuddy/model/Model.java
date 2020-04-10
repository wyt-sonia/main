package draganddrop.studybuddy.model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;

import javafx.collections.ObservableList;

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

    void clearDueSoonList(ReadOnlyStudyBuddy studyBuddy);
    public void forceAddDueSoonTask(Task task);

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    void addDuplicateTask(Task task);
    public void unarchiveDuplicateTask(Task task);

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' study buddy file path.
     */
    Path getStudyBuddyFilePath();

    /**
     * Sets the user prefs' study buddy file path.
     */
    void setStudyBuddyFilePath(Path studyBuddyFilePath);

    /**
     * Returns the StudyBuddy
     */
    ReadOnlyStudyBuddy getStudyBuddy();

    /**
     * Replaces study buddy data with the data in {@code studyBuddy}.
     */
    void setStudyBuddy(ReadOnlyStudyBuddy studyBuddy);

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task list.
     */
    boolean hasTask(Task task);

    public void deleteDueSoonTask(Task target);

    /**
     * completes the given task.
     * The task must exist in the study buddy.
     */
    void completeTask(Task target);

    /**
     * Set the task name.
     *
     * @param target a task
     * @param newTaskName the new name of the task
     */
    void setTaskName(Task target, String newTaskName);

    /**
     * Set the task description.
     *
     * @param target a task
     * @param newTaskDescription the new description of the task
     */
    void setTaskDescription(Task target, String newTaskDescription);

    /**
     * Set the task weight.
     *
     * @param target a task
     * @param newTaskWeight the new weight of the task
     */
    void setTaskWeight(Task target, double newTaskWeight);

    /**
     * Set the task estimated time cost.
     * @param target a task
     * @param newTaskTimeCost the new time cost of the task
     */
    void setTaskTimeCost(Task target, double newTaskTimeCost);


    /**
     * Set the task type.
     *
     * @param target a task
     * @param newTaskType the new task type
     */
    void setTaskType(Task target, TaskType newTaskType);

    /**
     * Set the task date time.
     *
     * @param target a task
     * @param newDateTimes the new date and time
     */
    void setTaskDateTime(Task target, LocalDateTime[] newDateTimes);

    void setTaskMod(Task target, Module mod) throws ModuleException;

    /**
     * Deletes the given task.
     * The task must exist in the list.
     */
    void deleteTask(Task task);

    /**
     * Archives the given task.
     *
     * @param task must not already exist in the study buddy.
     */
    void archiveTask(Task task);

    /**
     * Moves an archived task back to main taskList.
     *
     */
    void unarchiveTask(Task task);

    /**
     * Adds the given task.
     *
     * @param task must not already exist in the study buddy.
     */
    void addDueSoonTask(Task task);

    /**
     * Adds the given task.
     *
     * {@code task} must not already exist in the task list.
     */
    void addTask(Task task);

    void sortDueSoonTasks();

    void setTask(Task target, Task editedTask);
    boolean hasMod(Module mod);

    void addMod(Module mod);

    void changeModName(Module oldMod, Module newMod) throws ModuleException;

    void changeModCode(Module oldMod, Module newMod) throws ModuleException;

    void deleteMod(Module mod) throws ModuleException;

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
     *
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
