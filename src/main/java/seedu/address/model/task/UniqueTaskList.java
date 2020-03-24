package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.logic.parser.TimeParser;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/**
 * pending.
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    private final ObservableList<Task> internalUnmodifiableList =
        FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(Task toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTask);
    }

    /**
     * Adds a task to the list.
     * The task must not already exist in the list.
     */
    public void add(Task toAdd) {
        requireNonNull(toAdd);
        /*if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }*/
        internalList.add(toAdd);
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the list.
     * The task identity of {@code editedTask} must not be the same as another existing task in the list.
     */
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        /*if (!target.isSameTask(editedTask) && contains(editedTask)) {
            throw new DuplicateTaskException();
        }*/

        internalList.set(index, editedTask);
    }

    /**
     * Removes the equivalent task from the list.
     * The task must exist in the list.
     */
    public void remove(Task toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TaskNotFoundException();
        }
    }

    public void setTasks(UniqueTaskList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        requireAllNonNull(tasks);
        /*if (!tasksAreUnique(tasks)) {
            throw new DuplicateTaskException();
        }*/

        internalList.setAll(tasks);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Task> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UniqueTaskList // instanceof handles nulls
            && internalList.equals(((UniqueTaskList) other).internalList));
    }

    /**
     * Returns true if {@code tasks} contains only unique tasks.
     */
    private boolean tasksAreUnique(List<Task> tasks) {
        for (int i = 0; i < tasks.size() - 1; i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                if (tasks.get(i).isSameTask(tasks.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Completes a task.
     *
     * @param target a task
     */
    public void completeTask(Task target) {
        requireNonNull(target);
        if (!internalList.contains(target)) {
            throw new TaskNotFoundException();
        } else {
            target.setStatus("finished");
            String finishDateTimeString = TimeParser.getDateTimeString(LocalDateTime.now());
            target.setFinishDateTime(TimeParser.parseDateTime(finishDateTimeString));
            int index = internalList.indexOf(target);
            internalList.set(index, target);
        }
    }

    /**
     * Set the task name.
     *
     * @param target a task
     * @param newTaskName the new name of the task
     */
    public void setTaskName(Task target, String newTaskName) {
        requireNonNull(target);
        requireNonNull(newTaskName);
        target.setTaskName(newTaskName);
    }

    /**
     * Set the task type
     * @param target a task
     * @param newTaskType the new task type
     */
    public void setTaskType(Task target, TaskType newTaskType) {
        requireNonNull(target);
        requireNonNull(newTaskType);
        target.setTaskType(newTaskType);
    }

    /**
     * Set the task date time
     * @param target a task
     * @param newDateTimes the new date and time
     */
    public void setTaskDateTime(Task target, LocalDateTime[] newDateTimes) {
        requireNonNull(target);
        requireNonNull(newDateTimes);
        target.setDateTimes(newDateTimes);
    }

    /**
     * Sort tasks by the given {@code keyword}.
     */
    public void sortTasks(String keyword) {

        if (keyword.equalsIgnoreCase("deadline / task start date")) {
            FXCollections.sort(internalList, Task::compareTo);
        } else if (keyword.equalsIgnoreCase("task name")) {
            FXCollections.sort(internalList, Comparator.comparing(Task::getTaskName));
        } else {
            FXCollections.sort(internalList, Comparator.comparing(Task::getCreationDateTime));
        }
    }

}
