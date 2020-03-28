package draganddrop.studdybuddy.logic.commands;

import static draganddrop.studdybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studdybuddy.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

import draganddrop.studdybuddy.logic.commands.delete.ClearTasksCommand;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.ModelManager;
import draganddrop.studdybuddy.model.StudyBuddy;
import draganddrop.studdybuddy.model.UserPrefs;

public class ClearTasksCommandTest {

    @Test
    public void execute_emptyStudyBuddy_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearTasksCommand(), model, ClearTasksCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyStudyBuddy_success() {
        Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTaskList(), new UserPrefs());
        expectedModel.setStudyBuddy(new StudyBuddy());

        assertCommandSuccess(new ClearTasksCommand(), model, ClearTasksCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
