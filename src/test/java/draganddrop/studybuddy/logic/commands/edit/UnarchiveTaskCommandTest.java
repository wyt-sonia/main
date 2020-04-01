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

class UnarchiveTaskCommandTest {

    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Task taskToArchive = model.getFilteredArchivedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        UnarchiveTaskCommand atCommand = new UnarchiveTaskCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(UnarchiveTaskCommand.MESSAGE_ARCHIVE_TASK_SUCCESS, taskToArchive);

        ModelManager expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
        expectedModel.unarchiveTask(taskToArchive);

        assertCommandSuccess(atCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        UnarchiveTaskCommand archiveCommand = new UnarchiveTaskCommand(outOfBoundIndex);

        assertCommandFailure(archiveCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {

        Task taskToArchive = model.getFilteredArchivedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        UnarchiveTaskCommand atCommand = new UnarchiveTaskCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(UnarchiveTaskCommand.MESSAGE_ARCHIVE_TASK_SUCCESS, taskToArchive);

        Model expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
        expectedModel.unarchiveTask(taskToArchive);

        assertCommandSuccess(atCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UnarchiveTaskCommand archiveFirstCommand = new UnarchiveTaskCommand(INDEX_FIRST_TASK);
        UnarchiveTaskCommand archiveSecondCommand = new UnarchiveTaskCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(archiveFirstCommand.equals(archiveFirstCommand));

        // same values -> returns true
        UnarchiveTaskCommand archiveFirstCommandCopy = new UnarchiveTaskCommand(INDEX_FIRST_TASK);
        assertTrue(archiveFirstCommand.equals(archiveFirstCommandCopy));

        // different types -> returns false
        assertFalse(archiveFirstCommand.equals(1));

        // null -> returns false
        assertFalse(archiveFirstCommand == null);

        // different person -> returns false
        assertFalse(archiveFirstCommand.equals(archiveSecondCommand));
    }

}
