package draganddrop.studybuddy.logic.commands.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms.CHANGE_MOD_CODE;
import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms.CHANGE_MOD_NAME;
import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms.DELETE_MOD;
import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms.INIT;

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
    private Module moduleA = new Module("A", "CS1111");
    private Module moduleB = new Module("B", "CS2222");
    private Model model = new ModelManager();

    @Test
    public void createCommandWithEmptyFirstMod_throwNullPointerException() {
        assertThrows(NullPointerException.class, (
            ) -> new EditModCommand(null, new EmptyModule(), CHANGE_MOD_NAME));
    }

    @Test
    public void createCommandWithEmptySecondMod_throwNullPointerException() {
        assertThrows(NullPointerException.class, (
            ) -> new EditModCommand(new EmptyModule(), null, CHANGE_MOD_NAME));
    }

    @Test
    public void execute_nonExistentMod_returnReply() {
        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS2111"),
                new Module("name", "CS1111"), null);
        try {
            assertEquals("Module does not exist in Study Buddy!",
                    editModCommand.execute(model).getFeedbackToUser());
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
    }

    @Test
    public void execute_invalidTerm_returnReply() {
        CreateModCommand createModCommand = new CreateModCommand(new Module("", "CS1231"));
        try {
            createModCommand.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
        EditModCommand editModCommand = new EditModCommand(new Module("", "CS1231"),
                new Module("new name", "CS1231"), INIT);
        try {
            assertEquals(new CommandResult("Did nothing! from EditModCommand."),
                editModCommand.execute(model));
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
    }

    @Test
    public void executionChangeModuleName_success() {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        try {
            createModCommand.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("new name", "CS1231"), CHANGE_MOD_NAME);
        try {
            assertEquals(new CommandResult("Module name Change successful!"),
                    editModCommand.execute(model));
            assertEquals(1, model.getFilteredModuleList()
                    .filtered(x -> x.getModuleName().equals("new name"))
                        .size());
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
    }

    @Test
    public void executionChangeModuleCode_success() {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        try {
            createModCommand.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("name", "CS0000"), CHANGE_MOD_CODE);
        try {
            assertEquals(new CommandResult("Module code change successful!"),
                    editModCommand.execute(model));
            assertEquals(1, model.getFilteredModuleList()
                    .filtered(x -> x.getModuleCode().equals("CS0000"))
                    .size());
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
    }

    @Test
    public void executionDeleteMod_success() {
        CreateModCommand createModCommand = new CreateModCommand(new Module("name", "CS1231"));
        try {
            createModCommand.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }

        EditModCommand editModCommand = new EditModCommand(new Module("name", "CS1231"),
                new Module("name", "CS1231"), DELETE_MOD);
        try {
            assertEquals(new CommandResult("Module deleted!"),
                    editModCommand.execute(model));
            assertEquals(0, model.getFilteredModuleList()
                    .filtered(x -> x.getModuleCode().equals("CS1231"))
                    .size());
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }
    }

    @Test
    public void executeDuplicateMod_throwCommandException() {
        CreateModCommand createModCommand1 = new CreateModCommand(
                new Module("name1", "CS1231"));
        CreateModCommand createModCommand2 = new CreateModCommand(
                new Module("name2", "CS2040"));

        try {
            createModCommand1.execute(model);
            createModCommand2.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }

        EditModCommand editModCommand = new EditModCommand(new Module("name1", "CS1231"),
                new Module("name2", "CS1231"), CHANGE_MOD_NAME);

        assertThrows(CommandException.class, () -> editModCommand.execute(model));
    }

    @Test
    public void executeDuplicateCode_throwCommandException() {
        CreateModCommand createModCommand1 = new CreateModCommand(
                new Module("name1", "CS1231"));
        CreateModCommand createModCommand2 = new CreateModCommand(
                new Module("name2", "CS2040"));

        try {
            createModCommand1.execute(model);
            createModCommand2.execute(model);
        } catch (CommandException ex) {
            System.out.println("Test failed");
        }

        EditModCommand editModCommand = new EditModCommand(new Module("name1", "CS1231"),
                new Module("name1", "CS2040"), CHANGE_MOD_CODE);

        assertThrows(CommandException.class, () -> editModCommand.execute(model));
    }
}
