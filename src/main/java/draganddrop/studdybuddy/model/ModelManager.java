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
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.TaskType;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;


/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<Module> filteredModules;
    private final FilteredList<Task> filteredArchiveTasks;
    private final FilteredList<Task> filteredDueSoonTasks;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(addressBook.getTaskList(), userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredTasks = new FilteredList<>(this.addressBook.getTaskList());
        filteredModules = new FilteredList<>(this.addressBook.getModuleList());
        filteredArchiveTasks = new FilteredList<>(this.addressBook.getArchivedList());
        filteredDueSoonTasks = new FilteredList<>(this.addressBook.getDueSoonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
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
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return addressBook.hasTask(task);
    }

    @Override
    public void completeTask(Task target) {
        addressBook.completeTask(target);
    }

    @Override
    public void setTaskName(Task target, String taskName) {
        addressBook.setTaskName(target, taskName);
    }

    @Override
    public void setTaskType(Task target, TaskType newTaskType) {
        addressBook.setTaskType(target, newTaskType);
    }

    @Override
    public void setTaskDateTime(Task target, LocalDateTime[] newTaskDateTime) {
        addressBook.setTaskDateTime(target, newTaskDateTime);
    }

    @Override
    public void deleteTask(Task target) {
        addressBook.removeTask(target);
    }

    @Override
    public void deleteDueSoonTask(Task target) {
        addressBook.removeDueSoonTask(target);
    }

    @Override
    public void sortTasks(String keyword) {
        addressBook.sortTasks(keyword);
    }

    @Override
    public void sortDueSoonTasks() {
        addressBook.sortDueSoonTasks();
    }

    @Override
    public void archiveTask(Task task) {
        addressBook.addArchivedTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void addDueSoonTask(Task task) {
        addressBook.addDueSoonTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void addTask(Task task) {
        addressBook.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public boolean hasMod(Module mod) {
        requireNonNull(mod);
        return addressBook.hasModule(mod);
    }

    /**
     * STILL NEEDS MORE REFINEMENT DUE TO ABSENCE OF UpdateModuleList.
     *
     * @param mod
     */
    @Override
    public void addMod(Module mod) {
        System.out.println("ModelManager add mod");
        addressBook.addModule(mod);
        System.out.println("ModelManager add mod success");

    }

    @Override
    public void setTask(Task target, Task editedTask) {
        CollectionUtil.requireAllNonNull(target, editedTask);

        addressBook.setTasks(target, editedTask);
    }

    //=========== Filtered Task List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of
     * {@code versionedAddressBook}
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
        return addressBook.equals(other.addressBook)
            && userPrefs.equals(other.userPrefs)
            && filteredTasks.equals(other.filteredTasks)
            && filteredDueSoonTasks.equals(other.filteredDueSoonTasks)
            && filteredArchiveTasks.equals(other.filteredArchiveTasks);
    }


}
