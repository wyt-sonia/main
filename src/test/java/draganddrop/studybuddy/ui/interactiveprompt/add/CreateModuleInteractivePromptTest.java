package draganddrop.studybuddy.ui.interactiveprompt.add;

import static draganddrop.studybuddy.testutil.TestModules.getSampleModule;
import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import draganddrop.studybuddy.logic.LogicManager;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.StorageManager;

class CreateModuleInteractivePromptTest {

    @TempDir
    public Path testFolder;
    private CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
    private Model modelStub = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        JsonStudyBuddyStorage studyBuddyStorage = new JsonStudyBuddyStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        StorageManager storageStub = new StorageManager(studyBuddyStorage, userPrefsStorage);
        LogicManager logicStub = new LogicManager(modelStub, storageStub);
        for (Module m : getSampleModule()) {
            logicStub.getStudyBuddy().getModuleList().add(m);
        }
        prompt.setLogic(logicStub);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void interactFirstInputReturnKeywordPrompt() {
        assertEquals("Please key in the name of the module that you want to create",
                prompt.interact(""));
    }

    @Test
    public void interactQuitCommandReturnMessage() {
        assertEquals(CreateModuleInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interactSecondInputReturnKeywordPrompt() {
        prompt.interact("");
        assertTrue(prompt.interact("Software engineering").contains("Software engineering"));
    }

    @Test
    public void interactThirdInputReturnKeywordPrompt() {
        prompt.interact("");
        prompt.interact("a");
        assertTrue(prompt.interact("CS1101S").contains("CS1101S"));
    }

    @Test
    public void interactFourthInputReturnKeywordPrompt() {
        prompt.interact("");
        prompt.interact("a");
        prompt.interact("CS1101S");
        assertEquals(CreateModuleInteractivePrompt.SUCCESS_COMMAND_MSG, prompt.interact(""));
    }

    @Test
    public void interactNoModuleNameReturnCorrectReply() {
        prompt.interact("");
        assertTrue(prompt.interact("").contains(new ModuleException("emptyInputError").getErrorMessage()));
    }

    @Test
    public void interactInvalidModuleCodeReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("a");
        assertTrue(prompt.interact("Csdsdsa")
            .contains(new ModuleException("wrongModuleCodeFormatError").getErrorMessage()));
        assertTrue(prompt.interact("C111")
            .contains(new ModuleException("wrongModuleCodeFormatError").getErrorMessage()));
        assertTrue(prompt.interact("C1110")
            .contains(new ModuleException("wrongModuleCodeFormatError").getErrorMessage()));
    }

    @Test
    public void endInteract_test() {
        prompt.endInteract("a");
    }
}
