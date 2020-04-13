package draganddrop.studybuddy.logic.commands.delete;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.UserPrefs;

/**
 * Test class for ClearTasksCommand.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class ClearTasksCommandTest {

    @Test
    public void execute_emptyStudyBuddy_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearTasksCommand(), model, Messages.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyStudyBuddy_success() {
        Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTaskList(), new UserPrefs());
        expectedModel.setStudyBuddy(new StudyBuddy());

        assertCommandSuccess(new ClearTasksCommand(), model, Messages.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

}
