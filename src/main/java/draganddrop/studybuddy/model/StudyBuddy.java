package draganddrop.studybuddy.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;

import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleList;
import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.model.statistics.CompletionStats;
import draganddrop.studybuddy.model.statistics.GeneralStats;
import draganddrop.studybuddy.model.statistics.OverdueStats;
import draganddrop.studybuddy.model.statistics.ScoreStats;
import draganddrop.studybuddy.model.statistics.Statistics;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.UniqueTaskList;

import draganddrop.studybuddy.ui.ProductivityPage;
import draganddrop.studybuddy.ui.interactiveprompt.edit.SetGoalInteractivePrompt;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the study buddy
 * Duplicates are allowed.
 */
public class StudyBuddy implements ReadOnlyStudyBuddy {

    private final UniqueTaskList archivedTasks;
    private final UniqueTaskList dueSoonTasks;
    private final UniqueTaskList tasks;
    private final ModuleList moduleList;
    private final CompletionStats completionStats;
    private final OverdueStats overdueStats;
    private final GeneralStats generalStats;
    private final Statistics statistics;
    private final ScoreStats scoreStats;

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
        completionStats = new CompletionStats();
        overdueStats = new OverdueStats();
        generalStats = new GeneralStats();
        scoreStats = new ScoreStats();
        // the singleton instance of statistics is created here
        statistics = new Statistics(generalStats, completionStats, overdueStats, scoreStats);
        UniqueTaskList.setStatistics(statistics);
        Task.setStatistics(statistics);
        ProductivityPage.setStatistics(statistics);
        SetGoalInteractivePrompt.setStatistics(statistics);
    }

    public StudyBuddy() {
    }

    /**
     * Creates an StudyBuddy using the Tasks in the {@code toBeCopied}
     */
    public StudyBuddy(ReadOnlyStudyBuddy toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    // Statistics
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public List<Integer> getCompleteCountList() {
        return completionStats.getCompleteCountList();
    }

    @Override
    public List<Integer> getOverdueCountList() {
        return overdueStats.getOverdueCountList();
    }

    @Override
    public GeneralStats getGeneralStats() {
        return generalStats;
    }

    @Override
    public ScoreStats getScoreStats() {
        return scoreStats;
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
        updateDeleteDueSoon(key);
    }

    /**
     * Checks if task belongs in due soon list and deletes it
     * @param p
     */
    public void updateDeleteDueSoon(Task p) {
        if (this.getDueSoonList().contains(p)) {
            dueSoonTasks.remove(p);
            sortDueSoonTasks();
        }
    }

    /**
     * Removes {@code key} from this {@code StudyBuddy}.
     * {@code key} must exist in the task list.
     */
    public void setArchivedTasks(List<Task> aTasks) {
        this.archivedTasks.setTasks(aTasks);
    }

    public void setDueSoonTasks(List<Task> aTasks) {
        this.dueSoonTasks.setTasks(aTasks);
    }

    public void setCompleteList(List<Integer> completeList) {
        this.completionStats.setCompleteCountList(completeList);
    }

    public void setOverdueList(List<Integer> overdueList) {
        this.overdueStats.setOverdueCountList(overdueList);
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
        setCompleteList(newData.getCompleteCountList());
        setOverdueList(newData.getOverdueCountList());
        generalStats.copy(newData.getGeneralStats());
        scoreStats.copy(newData.getScoreStats());
    }

    public void clearDueSoon(ReadOnlyStudyBuddy newData) {
        setDueSoonTasks(newData.getDueSoonList());
    }

    public void addDueSoonTask(Task task) {
        dueSoonTasks.add(task);
    }

    //// task-level operations

    /**
     * Adds an archived person to the study buddy.
     *
     * @param p must not already exist in the study buddy.
     */
    public void archiveTask(Task p) {
        tasks.remove(p);
        updateDeleteDueSoon(p);
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
        updateAddDueSoon(p);
        tasks.add(p);
    }

    public void unarchiveDuplicateTask(Task p) {
        archivedTasks.remove(p);
        updateAddDueSoon(p);
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
     * Change Module Name.
     * @param oldModule
     * @param newModule
     * @throws ModuleCodeException
     */
    public void changeModuleName(Module oldModule, Module newModule) throws ModuleCodeException {
        refreshAllModuleTaskList(oldModule, newModule);
        moduleList.changeModuleName(oldModule, newModule);
    }

    /**
     * Change Module Code.
     * @param oldModule
     * @param newModule
     * @throws ModuleCodeException
     */
    public void changeModuleCode(Module oldModule, Module newModule) throws ModuleCodeException {
        refreshAllModuleTaskList(oldModule, newModule);
        moduleList.changeModuleCode(oldModule, newModule);
    }

    /**
     * Delete a existing mod. Transfers all task with the existing mod to emptyMod.
     * @param module
     * @throws ModuleCodeException
     */
    public void deleteMod(Module module) throws ModuleCodeException {
        refreshAllModuleTaskList(module, new EmptyModule());
        moduleList.remove(module);
    }


    private void refreshAllModuleTaskList(Module oldModule, Module newModule) {
        refreshModuleCodeTaskList(tasks, oldModule, newModule);
        refreshModuleCodeTaskList(archivedTasks, oldModule, newModule);
        refreshModuleCodeTaskList(dueSoonTasks, oldModule, newModule);
    }

    /**
     * performs setModule(newMod) for all task with oldMod within the taskList.
     * @param taskList
     * @param oldModule
     * @param newModule
     */
    private void refreshModuleCodeTaskList(UniqueTaskList taskList, Module oldModule, Module newModule) {
        taskList.forEach(x -> {
            if (x.getModule().equals(oldModule)) {
                x.setModule(newModule);
            }
        });
    }


    /**
     * Adds a task to the task list.
     * The task must not already exist in the task list.
     */
    public void addTask(Task t) {
        tasks.add(t);
        if (t.isDueSoon()) {
            dueSoonTasks.add(t);
            sortDueSoonTasks();
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

    /**
     * Adds task to due soon list if it is due soon.
     * @param target
     */
    public void updateAddDueSoon(Task target) {
        if (!dueSoonTasks.contains(target)) {
            if (target.isDueSoon()) {
                dueSoonTasks.add(target);
                sortDueSoonTasks();
            }
        }
    }

    public void setModuleInTask(Task target, Module module) throws ModuleCodeException {
        if (moduleList.contains(module) || module.equals(new EmptyModule())) {
            tasks.setTaskMod(target, module);
        } else {
            throw new ModuleCodeException("Module does not exist in studyBuddy!");
        }
    }


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

    /*@Override
    public Statistics getStatistics() {
        return statistics;
    }*/

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
