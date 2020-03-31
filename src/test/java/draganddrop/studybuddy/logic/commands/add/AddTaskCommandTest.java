package draganddrop.studybuddy.logic.commands.add;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.ReadOnlyUserPrefs;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.testutil.TaskBuilder;

import javafx.collections.ObservableList;

class AddTaskCommandTest {

    @Test
    public void constructor_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTaskCommand(null));
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTaskAdded modelStub = new ModelStubAcceptingTaskAdded();
        Task validTask = new TaskBuilder().build();

        CommandResult commandResult = new AddTaskCommand(validTask).execute(modelStub);

        assertEquals(String.format(AddTaskCommand.MESSAGE_SUCCESS, validTask), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTask), modelStub.tasksAdded);
    }

    @Test
    public void equals() {
        Task task1 = new TaskBuilder().withName("ass1").build();
        Task task2 = new TaskBuilder().withName("ass2").build();

        AddTaskCommand add1command = new AddTaskCommand(task1);
        AddTaskCommand add2command = new AddTaskCommand(task2);

        //same object -> returns true
        assertTrue(add1command.equals(add1command));

        //same values -> returns true
        AddTaskCommand add1copycommand = new AddTaskCommand(task1);
        assertTrue(add1command.equals(add1copycommand));

        //different types -> reutrns false
        assertFalse(add1command.equals(1));

        //null -> returns false
        assertFalse(add1command.equals(null));

        //diff task -> returns false
        assertFalse(add1command.equals(add2command));

    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getStudyBuddyFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudyBuddyFilePath(Path studyBuddyFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyStudyBuddy getStudyBuddy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudyBuddy(ReadOnlyStudyBuddy newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void archiveTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void unarchiveTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addDueSoonTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        public void deleteTask(Task target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void sortDueSoonTasks(){}

        @Override
        public void setTask(Task target, Task editedTask) {

        }

        @Override
        public boolean hasMod(Module mod) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addMod(Module mod) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void completeTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskName(Task target, String newTaskName) {
            // empty
        }

        @Override
        public void setTaskType(Task target, TaskType newTaskType) {
            // empty
        }

        @Override
        public void setTaskDateTime(Task target, LocalDateTime[] newDateTimes) {
            // empty
        }

        @Override
        public void setTaskMod(Task target, Module mod) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredArchivedTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Module> getFilteredModuleList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredDueSoonTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteDueSoonTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortTasks(String keyword) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single task.
     */
    private class ModelStubWithTask extends AddTaskCommandTest.ModelStub {
        private final Task task;

        ModelStubWithTask(Task task) {
            requireNonNull(task);
            this.task = task;
        }

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return this.task.isSameTask(task);
        }
    }

    /**
     * A Model stub that always accept the task being added.
     */
    private class ModelStubAcceptingTaskAdded extends AddTaskCommandTest.ModelStub {
        final ArrayList<Task> tasksAdded = new ArrayList<>();

        @Override
        public void addTask(Task task) {
            requireNonNull(task);
            tasksAdded.add(task);
        }

        @Override
        public ReadOnlyStudyBuddy getStudyBuddy() {
            return new StudyBuddy();
        }

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return tasksAdded.stream().anyMatch(task::isSameTask);
        }
    }
}
