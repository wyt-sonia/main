package draganddrop.studybuddy.logic.commands.add;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleList;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;

public class CreateModCommandTest {
    Module moduleA = new Module("A", "CS1111");
    Module moduleB = new Module("B", "CS2222");
    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());


    @Test
    public void addDuplicateModule_throwsCommandException() {
        CreateModCommand createModCommand = new CreateModCommand(moduleA);
        Assertions.assertThrows(CommandException.class, () -> {
            createModCommand.execute(model);
            createModCommand.execute(model);
        });
    }

    @Test
    public void addDuplicateModule_correctExceptionMessage() {
        String expectedMessage = CreateModCommand.MESSAGE_DUPLICATE_TASK;
        CreateModCommand createModCommand = new CreateModCommand(moduleA);
        try {
            createModCommand.execute(model);
            createModCommand.execute(model);
        } catch (CommandException ex) {
            assertEquals(expectedMessage, ex.getMessage());
        }
    }

    @Test
    public void addModule_success() throws CommandException {
        String expected_message = String.format(CreateModCommand.MESSAGE_SUCCESS, moduleB);
        CreateModCommand createModCommand = new CreateModCommand(moduleB);
        assertEquals(createModCommand.execute(model).getFeedbackToUser(), expected_message);
    }

    @Test
    public void equalCreateModCommand() {
        CreateModCommand createModCommand1 = new CreateModCommand(moduleA);
        CreateModCommand createModCommand2 = new CreateModCommand(moduleA);
        CreateModCommand createModCommand3 = new CreateModCommand(moduleB);

        assertTrue(createModCommand1.equals(createModCommand2));
        assertFalse(createModCommand1.equals(createModCommand3));
    }
}
