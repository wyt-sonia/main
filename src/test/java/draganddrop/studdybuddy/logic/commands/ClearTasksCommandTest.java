package draganddrop.studdybuddy.logic.commands;

import static draganddrop.studdybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studdybuddy.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

<<<<<<< HEAD:src/test/java/seedu/address/logic/commands/ClearTasksCommandTest.java
import seedu.address.logic.commands.delete.ClearTasksCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
=======
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.ModelManager;
import draganddrop.studdybuddy.model.StudyBuddy;
import draganddrop.studdybuddy.model.UserPrefs;
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/test/java/draganddrop/studdybuddy/logic/commands/ClearTasksCommandTest.java

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
