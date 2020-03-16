package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;

class ArchiveTaskCommandTest {

    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Task taskToArchive = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        ArchiveTaskCommand atCommand = new ArchiveTaskCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(ArchiveTaskCommand.MESSAGE_ARCHIVE_PERSON_SUCCESS, taskToArchive);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToArchive);
        expectedModel.archiveTask(taskToArchive);

        assertCommandSuccess(atCommand, model, expectedMessage, expectedModel);
    }

}
