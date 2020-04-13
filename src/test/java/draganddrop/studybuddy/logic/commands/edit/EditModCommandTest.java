package draganddrop.studybuddy.logic.commands.edit;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms.CHANGE_MOD_CODE;
import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms.CHANGE_MOD_NAME;
import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms.DELETE_MOD;
import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms.INIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.add.CreateModCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;


public class EditModCommandTest {
    private Model model = new ModelManager();

    @Test
    public void createCommandWithEmptyFirstModThrowNullPointerException() {
        assertThrows(NullPointerException.class, (
            ) -> new EditModCommand(null, new EmptyModule(), CHANGE_MOD_NAME));
    }

    @Test
    public void createCommandWithEmptySecondModThrowNullPointerException() {
        assertThrows(NullPointerException.class, (
            ) -> new EditModCommand(new EmptyModule(), null, CHANGE_MOD_NAME));
    }

    @Test
    public void executeNonExistentModReturnReply() throws CommandException {
        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS2111"),
                new Module("name", "CS1111"), null);
        assertEquals("Module does not exist in Study Buddy!",
                editModCommand.execute(model).getFeedbackToUser());
    }

    @Test
    public void executeInvalidTermReturnReply() throws CommandException {
        CreateModCommand createModCommand = new CreateModCommand(new Module("", "CS1231"));
        createModCommand.execute(model);
        EditModCommand editModCommand = new EditModCommand(new Module("", "CS1231"),
                new Module("new name", "CS1231"), INIT);
        assertEquals(new CommandResult("Did nothing! from EditModCommand."),
                editModCommand.execute(model));

    }

    @Test
    public void executionChangeModuleNameSuccess() throws CommandException {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        createModCommand.execute(model);

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("new name", "CS1231"), CHANGE_MOD_NAME);
        assertEquals(new CommandResult("Module name Change successful!"), editModCommand.execute(model));
        assertEquals(1, model.getFilteredModuleList()
                .filtered(x -> x.getModuleName().equals("new name")).size());
    }

    @Test
    public void executionChangeModuleCodeSuccess() throws CommandException {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        createModCommand.execute(model);

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("name", "CS0000"), CHANGE_MOD_CODE);
        assertEquals(new CommandResult("Module code change successful!"), editModCommand.execute(model));
        assertEquals(1, model.getFilteredModuleList()
                .filtered(x -> x.getModuleCode().equals("CS0000")).size());
    }

    @Test
    public void executionDeleteModSuccess() throws CommandException {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        createModCommand.execute(model);

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("name", "CS1231"), DELETE_MOD);
        assertEquals(new CommandResult("Module deleted!"),
                    editModCommand.execute(model));
        assertEquals(0, model.getFilteredModuleList()
                .filtered(x -> x.getModuleCode().equals("CS1231")).size());
    }

    @Test
    public void executeDuplicateModThrowCommandException() throws CommandException {
        CreateModCommand createModCommand1 = new CreateModCommand(
                new Module("name1", "CS1231"));
        CreateModCommand createModCommand2 = new CreateModCommand(
                new Module("name2", "CS2040"));

        createModCommand1.execute(model);
        createModCommand2.execute(model);

        EditModCommand editModCommand = new EditModCommand(new Module("name1", "CS1231"),
                new Module("name2", "CS1231"), CHANGE_MOD_NAME);

        assertThrows(CommandException.class, () -> editModCommand.execute(model));
    }

    @Test
    public void executeDuplicateCodeThrowCommandException() throws CommandException {
        CreateModCommand createModCommand1 = new CreateModCommand(
                new Module("name1", "CS1231"));
        CreateModCommand createModCommand2 = new CreateModCommand(
                new Module("name2", "CS2040"));

        createModCommand1.execute(model);
        createModCommand2.execute(model);

        EditModCommand editModCommand = new EditModCommand(new Module("name1", "CS1231"),
                new Module("name1", "CS2040"), CHANGE_MOD_CODE);

        assertThrows(CommandException.class, () -> editModCommand.execute(model));
    }
}
