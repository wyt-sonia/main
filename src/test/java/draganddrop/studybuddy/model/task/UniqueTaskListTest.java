package draganddrop.studybuddy.model.task;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TWO;
import static draganddrop.studybuddy.logic.commands.CommandTestUtil.VALID_TASK_NAME_TWO;
import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static draganddrop.studybuddy.testutil.TypicalTasks.getSampleTasks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.task.exceptions.TaskNotFoundException;
import draganddrop.studybuddy.testutil.TaskBuilder;

/**
 * Test class for Unique Task List
 *
 * @@author Souwmyaa Sabarinathann
 */
public class UniqueTaskListTest {
    private final UniqueTaskList uniqueTaskList = new UniqueTaskList();
    private Task task1 = getSampleTasks()[0];
    private Task task2 = getSampleTasks()[1];

    @Test
    public void uniqueTaskListTestContainsNullTaskThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.isContains(null));
    }

    @Test
    public void uniqueTaskListTestContainsTaskNotInListReturnsFalse() {
        assertFalse(uniqueTaskList.isContains(task1));
    }

    @Test
    public void contains_taskInList_returnsTrue() {
        uniqueTaskList.add(task1);
        assertTrue(uniqueTaskList.isContains(task1));
    }

    @Test
    public void uniqueTaskListTestAddNullTaskThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.add(null));
    }

    @Test
    public void uniqueTaskListTestSetTaskNullTargetTaskThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.setTask(null, task1));
    }

    @Test
    public void uniqueTaskListTestSetTaskNullEditedTaskThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.setTask(task1, null));
    }

    @Test
    public void uniqueTaskListTestSetTaskTargetTaskNotInListThrowsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> uniqueTaskList.setTask(task1, task1));
    }

    @Test
    public void uniqueTaskListTestSetTaskEditedTaskIsSameTaskSuccess() {
        uniqueTaskList.add(task1);
        uniqueTaskList.setTask(task1, task1);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(task1);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestSetTaskEditedTaskHasSameIdentitySuccess() {
        uniqueTaskList.add(task1);
        Task editedTask1 = new TaskBuilder(task1).withName(VALID_TASK_NAME_TWO).withDescription(VALID_DESCRIPTION_TWO)
                .build();
        uniqueTaskList.setTask(task1, editedTask1);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(editedTask1);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestSetTaskEditedTaskHasDifferentIdentitySuccess() {
        uniqueTaskList.add(task1);
        uniqueTaskList.setTask(task1, task2);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(task2);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestEemoveNullTaskThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.remove(null));
    }

    @Test
    public void uniqueTaskListTestRemoveTaskDoesNotExistThrowsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> uniqueTaskList.remove(task1));
    }

    @Test
    public void uniqueTaskListTestRemoveExistingTaskRemovesTask() {
        uniqueTaskList.add(task1);
        uniqueTaskList.remove(task1);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestSetTasksNullUniqueTaskListThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.setTasks((UniqueTaskList) null));
    }

    @Test
    public void uniqueTaskListTestSetTasksUniqueTaskListReplacesOwnListWithProvidedUniqueTaskList() {
        uniqueTaskList.add(task1);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(task2);
        uniqueTaskList.setTasks(expectedUniqueTaskList);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestSetTasksNullListThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTaskList.setTasks((List<Task>) null));
    }

    @Test
    public void uniqueTaskListTestSetTasksListReplacesOwnListWithProvidedList() {
        uniqueTaskList.add(task1);
        List<Task> taskList = Collections.singletonList(task2);
        uniqueTaskList.setTasks(taskList);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(task2);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void uniqueTaskListTestAsUnmodifiableObservableListModifyListThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueTaskList.asUnmodifiableObservableList().remove(0));
    }
}

