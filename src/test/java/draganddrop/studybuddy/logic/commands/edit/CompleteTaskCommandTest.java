package draganddrop.studybuddy.logic.commands.edit;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.assertCommandFailure;
import static draganddrop.studybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studybuddy.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static draganddrop.studybuddy.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.exceptions.TaskNotFoundException;

class CompleteTaskCommandTest {

    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        try {
            Task taskToComplete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
            CompleteTaskCommand atCommand = new CompleteTaskCommand(INDEX_FIRST_TASK);
            String expectedMessage = String.format(Messages.MESSAGE_COMPLETE_TASK_SUCCESS, taskToComplete);

            ModelManager expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
            expectedModel.deleteTask(taskToComplete);
            expectedModel.completeTask(taskToComplete);
            expectedModel.completeTask(taskToComplete);
            assertCommandSuccess(atCommand, model, expectedMessage, expectedModel);
        } catch (TaskNotFoundException ex) {
            ex.getMessage();
        }
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        CompleteTaskCommand completeCommand = new CompleteTaskCommand(outOfBoundIndex);
        assertCommandFailure(completeCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        try {
            Task taskToComplete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
            CompleteTaskCommand atCommand = new CompleteTaskCommand(INDEX_FIRST_TASK);

            String expectedMessage = String.format(Messages.MESSAGE_COMPLETE_TASK_SUCCESS, taskToComplete);

            Model expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
            expectedModel.deleteTask(taskToComplete);
            expectedModel.completeTask(taskToComplete);
            assertCommandSuccess(atCommand, model, expectedMessage, expectedModel);
        } catch (TaskNotFoundException ex) {
            ex.getMessage();
        }
    }

    @Test
    public void equals() {
        CompleteTaskCommand completeFirstCommand = new CompleteTaskCommand(INDEX_FIRST_TASK);
        CompleteTaskCommand completeSecondCommand = new CompleteTaskCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(completeFirstCommand.equals(completeFirstCommand));

        // different types -> returns false
        assertFalse(completeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(completeFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(completeFirstCommand.equals(completeSecondCommand));
    }

}
