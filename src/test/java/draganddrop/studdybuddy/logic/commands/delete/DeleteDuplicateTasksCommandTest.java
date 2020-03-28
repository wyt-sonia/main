package draganddrop.studdybuddy.logic.commands.delete;

import static draganddrop.studdybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studdybuddy.testutil.TypicalTasks.getSampleTasks;
import static draganddrop.studdybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import draganddrop.studdybuddy.commons.core.Messages;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.ModelManager;
import draganddrop.studdybuddy.model.UserPrefs;

public class DeleteDuplicateTasksCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTaskList(), new UserPrefs());
        expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
    }

    @Test
    public void execute_checkDeleteDuplicates_success() {
        String expectedMessage = String.format(Messages.MESSAGE_DELETE_DUPLICATE_TASK_SUCCESS);
        DeleteDuplicateTaskCommand command = new DeleteDuplicateTaskCommand();
        expectedModel.deleteTask(model.getFilteredTaskList().get(2));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(getSampleTasks()[0], getSampleTasks()[1],
                getSampleTasks()[3], getSampleTasks()[4]),
                model.getFilteredTaskList());
    }
}
