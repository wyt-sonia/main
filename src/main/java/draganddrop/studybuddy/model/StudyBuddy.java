package draganddrop.studybuddy.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;

import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleList;
import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.UniqueTaskList;

import javafx.collections.ObservableList;

/**
 * Wraps all data at the study buddy
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class StudyBuddy implements ReadOnlyStudyBuddy {

    private final UniqueTaskList archivedTasks;
    private final UniqueTaskList dueSoonTasks;
    private final UniqueTaskList tasks;
    private final ModuleList moduleList;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        tasks = new UniqueTaskList();
        archivedTasks = new UniqueTaskList();
        dueSoonTasks = new UniqueTaskList();
        moduleList = new ModuleList();
    }

    public StudyBuddy() {
    }

    /**
     * Creates an StudyBuddy using the Persons in the {@code toBeCopied}
     */
    public StudyBuddy(ReadOnlyStudyBuddy toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the task list.
     * The task identity of {@code editedTask} must not be the same as another existing task in the task list.
     */
    public void setTasks(Task target, Task editedTask) {
        requireNonNull(editedTask);
        tasks.setTask(target, editedTask);
    }

    /**
     * Sort tasks by the given {@code keyword}.
     */
    public void sortTasks(String keyword) {
        tasks.sortTasks(keyword);
    }


    /**
     * Sort tasks by the given {@code keyword}.
     */
    public void sortDueSoonTasks() {
        dueSoonTasks.sortTasks("deadline / task start date");
    }

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task list.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }


    /**
     * Removes {@code key} from this {@code StudyBuddy}.
     * {@code key} must exist in the task list.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
        if (this.getDueSoonList().contains(key)) {
            removeDueSoonTask(key);
        }
        sortDueSoonTasks();
    }

    /**
     * Removes {@code key} from this {@code StudyBuddy}.
     * {@code key} must exist in the task list.
     */
    public void removeDueSoonTask(Task key) {
        dueSoonTasks.remove(key);
    }

    public void setArchivedTasks(List<Task> aTasks) {
        this.archivedTasks.setTasks(aTasks);
    }

    public void setDueSoonTasks(List<Task> aTasks) {
        this.dueSoonTasks.setTasks(aTasks);
    }

    /**
     * Resets the existing data of this {@code StudyBuddy} with {@code newData}.
     */
    public void resetData(ReadOnlyStudyBuddy newData) {
        requireNonNull(newData);
        setArchivedTasks(newData.getArchivedList());
        setDueSoonTasks(newData.getDueSoonList());
        setTasks(newData.getTaskList());
        setModuleList(newData.getModuleList());
    }

    //// task-level operations

    /**
     * Adds an archived person to the study buddy.
     *
     * @param p must not already exist in the study buddy.
     */
    public void archiveTask(Task p) {
        tasks.remove(p);
        archivedTasks.add(p);
    }

    /**
     * Adds to archive task list. (For Json files)
     */
    public void addArchiveTask(Task p) {
        archivedTasks.add(p);
    }

    /**
     * Moves an archived task back to main task list.
     *
     * @param p must not already exist in study buddy.
     */
    public void unarchiveTask(Task p) {
        archivedTasks.remove(p);
        tasks.add(p);
    }

    /**
     * Adds a due soon task to the dueSoonTasks list.
     *
     * @param p must not already exist in the study buddy.
     */
    public void addDueSoonTask(Task p) {
        if (p.isDueSoon()) {
            dueSoonTasks.add(p);
            sortDueSoonTasks();
        }
    }

    /**
     * Adds a module to the ModuleList.
     *
     * @param module
     */
    public void addModule(Module module) {
        try {
            moduleList.add(module);
        } catch (ModuleCodeException ex) {
            System.out.println("AddModule Failed, from studyBuddy.addModule()");
        }
    }

    /**
     * Adds a task to the task list.
     * The task must not already exist in the task list.
     */
    public void addTask(Task t) {
        tasks.add(t);
        if (t.isDueSoon()) {
            addDueSoonTask(t);
        }
    }

    public void completeTask(Task target) {
        tasks.completeTask(target);
    }

    public void setTaskName(Task target, String newTaskName) {
        tasks.setTaskName(target, newTaskName);
    }

    public void setTaskDescription(Task target, String newTaskDescription) {
        tasks.setTaskDescription(target, newTaskDescription);
    }

    public void setTaskTimeCost(Task target, double newTaskTimeCost) {
        tasks.setTaskTimeCost(target, newTaskTimeCost);
    }

    public void setTaskWeight(Task target, double newTaskWeight) {
        tasks.setTaskWeight(target, newTaskWeight);
    }


    public void setTaskType(Task target, TaskType newTaskType) {
        tasks.setTaskType(target, newTaskType);
    }

    public void setTaskDateTime(Task target, LocalDateTime[] newDateTimes) {
        tasks.setTaskDateTime(target, newDateTimes);
    }

    public void setModuleInTask(Task target, Module module) throws ModuleCodeException {
        if (moduleList.contains(module)) {
            tasks.setTaskMod(target, module);
        } else {
            throw new ModuleCodeException("Module does not exist in studyBuddy!");
        }
    }

    // public void collectTaskBasedOnMod(Module module) throws ModuleCodeException {
    //     ObservableList<Task> collectedTask = tasks.filterTaskByMod(module);
    //     int modIndex = moduleList.indexOf(module);
    //     Module originalModule = moduleList.get(module.toString());
    //    originalModule.setInternalTaskList(collectedTask);
    // }

    @Override
    public String toString() {
        return tasks.asUnmodifiableObservableList().size() + " tasks";
        // TODO: refine later
    }

    //// util methods

    @Override
    public ObservableList<Task> getArchivedList() {
        return archivedTasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getDueSoonList() {
        return dueSoonTasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Module> getModuleList() {
        return moduleList.getInternalList();
    }

    public void setModuleList(List<Module> modules) {
        this.moduleList.setModuleList(modules);
    }

    public boolean hasModule(Module module) {
        return moduleList.contains(module);
    }

    @Override
    public int hashCode() {
        return tasks.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof StudyBuddy // instanceof handles nulls
            && tasks.equals(((StudyBuddy) other).tasks));
    }

}
