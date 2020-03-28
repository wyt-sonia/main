package draganddrop.studdybuddy.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.GuiSettings;
import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.commons.util.CollectionUtil;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.TaskType;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;


/**
 * Represents the in-memory model of the study buddy data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final StudyBuddy studyBuddy;
    private final UserPrefs userPrefs;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<Module> filteredModules;
    private final FilteredList<Task> filteredArchiveTasks;
    private final FilteredList<Task> filteredDueSoonTasks;

    /**
     * Initializes a ModelManager with the given studyBuddy and userPrefs.
     */
    public ModelManager(ReadOnlyStudyBuddy studyBuddy, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(studyBuddy.getTaskList(), userPrefs);

        logger.fine("Initializing with study buddy: " + studyBuddy + " and user prefs " + userPrefs);

        this.studyBuddy = new StudyBuddy(studyBuddy);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredTasks = new FilteredList<>(this.studyBuddy.getTaskList());
        filteredModules = new FilteredList<>(this.studyBuddy.getModuleList());
        filteredArchiveTasks = new FilteredList<>(this.studyBuddy.getArchivedList());
        filteredDueSoonTasks = new FilteredList<>(this.studyBuddy.getDueSoonList());
    }

    public ModelManager() {
        this(new StudyBuddy(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getStudyBuddyFilePath() {
        return userPrefs.getStudyBuddyFilePath();
    }

    @Override
    public void setStudyBuddyFilePath(Path studyBuddyFilePath) {
        requireNonNull(studyBuddyFilePath);
        userPrefs.setStudyBuddyFilePath(studyBuddyFilePath);
    }

    //=========== StudyBuddy ================================================================================

    @Override
    public ReadOnlyStudyBuddy getStudyBuddy() {
        return studyBuddy;
    }

    @Override
    public void setStudyBuddy(ReadOnlyStudyBuddy studyBuddy) {
        this.studyBuddy.resetData(studyBuddy);
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return studyBuddy.hasTask(task);
    }

    @Override
    public void completeTask(Task target) {
        studyBuddy.completeTask(target);
    }

    @Override
    public void setTaskName(Task target, String taskName) {
        studyBuddy.setTaskName(target, taskName);
    }

    @Override
    public void setTaskType(Task target, TaskType newTaskType) {
        studyBuddy.setTaskType(target, newTaskType);
    }

    @Override
    public void setTaskDateTime(Task target, LocalDateTime[] newTaskDateTime) {
        studyBuddy.setTaskDateTime(target, newTaskDateTime);
    }

    @Override
    public void setTaskMod(Task target, Module mod) throws ModuleCodeException {
        studyBuddy.setModuleInTask(target, mod);
    }

    @Override
    public void deleteTask(Task target) {
        studyBuddy.removeTask(target);
    }

    @Override
    public void deleteDueSoonTask(Task target) {
        studyBuddy.removeDueSoonTask(target);
    }

    @Override
    public void sortTasks(String keyword) {
        studyBuddy.sortTasks(keyword);
    }

    @Override
    public void sortDueSoonTasks() {
        studyBuddy.sortDueSoonTasks();
    }

    @Override
    public void archiveTask(Task task) {
        studyBuddy.addArchivedTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void addDueSoonTask(Task task) {
        studyBuddy.addDueSoonTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void addTask(Task task) {
        studyBuddy.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public boolean hasMod(Module mod) {
        requireNonNull(mod);
        return studyBuddy.hasModule(mod);
    }


    /**
     * STILL NEEDS MORE REFINEMENT DUE TO ABSENCE OF UpdateModuleList.
     *
     * @param mod
     */
    @Override
    public void addMod(Module mod) {
        System.out.println("ModelManager add mod");
        studyBuddy.addModule(mod);
        System.out.println("ModelManager add mod success");

    }

    @Override
    public void setTask(Task target, Task editedTask) {
        CollectionUtil.requireAllNonNull(target, editedTask);

        studyBuddy.setTasks(target, editedTask);
    }

    //=========== Filtered Task List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of
     * {@code versionedStudyBuddy}
     */

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return filteredTasks;
    }

    @Override
    public ObservableList<Task> getFilteredArchivedTaskList() {
        return filteredArchiveTasks;
    }

    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return filteredModules;
    }

    @Override
    public ObservableList<Task> getFilteredDueSoonTaskList() {
        return filteredDueSoonTasks;
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return studyBuddy.equals(other.studyBuddy)
            && userPrefs.equals(other.userPrefs)
            && filteredTasks.equals(other.filteredTasks)
            && filteredDueSoonTasks.equals(other.filteredDueSoonTasks)
            && filteredArchiveTasks.equals(other.filteredArchiveTasks);
    }


}
