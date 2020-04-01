package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import draganddrop.studybuddy.logic.LogicManager;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.StorageManager;
import javafx.collections.ObservableList;

/**
 * This is the test class for clear tasks interactive prompt
 */
class EditTaskInteractivePromptTest {

    @TempDir
    public Path testFolder;
    private StorageManager storageStub;
    private LogicManager logicStub;
    private EditTaskInteractivePrompt prompt = new EditTaskInteractivePrompt();
    private Model modelStub = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        JsonStudyBuddyStorage studyBuddyStorage = new JsonStudyBuddyStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageStub = new StorageManager(studyBuddyStorage, userPrefsStorage);
        logicStub = new LogicManager(modelStub, storageStub);
        prompt.setLogic(logicStub);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void interact_quitCommand_returnMessage() {
        EditTaskInteractivePrompt prompt = new EditTaskInteractivePrompt();
        assertEquals(EditTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        assertEquals("Please enter the index of the task that you wish to edit.",
            prompt.interact(""));
    }

    @Test
    public void interact_taskIndexLowerBoundary_returnMessage() {
        ObservableList<Task> tasksStub = modelStub.getStudyBuddy().getTaskList();
        String lowerBoundary = "1";
        prompt.interact("");
        String expectedLowerResponse = "Please choose the field that you wish to edit for task: "
            + tasksStub.get(0).getTaskName() + ".\n\n" + TaskField.getFieldString();
        String actualLowerResponse = prompt.interact(lowerBoundary);
        assertEquals(expectedLowerResponse, actualLowerResponse);
    }

    @Test
    public void interact_taskIndexHigherBoundary_returnMessage() {
        ObservableList<Task> tasksStub = modelStub.getStudyBuddy().getTaskList();
        int taskListSize = tasksStub.size();
        String higherBoundary = "" + taskListSize;
        prompt.interact("");
        String expectedHigherResponse = "Please choose the field that you wish to edit for task: "
            + tasksStub.get(taskListSize - 1).getTaskName() + ".\n\n" + TaskField.getFieldString();
        String actualHigherResponse = prompt.interact(higherBoundary);
        assertEquals(expectedHigherResponse, actualHigherResponse);
    }

    @Test
    public void interact_taskIndexOutOfRangeZero_returnErrorMessage() {
        String zero = "0";
        prompt.interact("");
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact(zero));
    }

    @Test
    public void interact_taskIndexOutOfRangeSizePlusOne_returnErrorMessage() {
        ObservableList<Task> tasksStub = modelStub.getStudyBuddy().getTaskList();
        int taskListSizePlusOne = tasksStub.size() + 1;
        prompt.interact("");
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact(taskListSizePlusOne + ""));
    }

    @Test
    public void interact_taskIndexWrongFormat_returnErrorMessage() {
        prompt.interact("");
        assertEquals(new EditTaskCommandException("wrongIndexFormatError").getErrorMessage(),
            prompt.interact("wrongIndexFormat"));
    }

    @Test
    public void interact_taskFieldIndexWrongFormat_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        assertEquals(new EditTaskCommandException("wrongIndexFormatError").getErrorMessage(),
            prompt.interact("wrongIndexFormat"));
    }

    @Test
    public void interact_taskFieldIndexOutOfRangeZero_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact("0"));
    }

    @Test
    public void interact_taskFieldIndexOutOfRangeSizePlusOne_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        int size = TaskField.values().length;
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact(size + 1 + ""));
    }

    @Test
    public void interact_taskEmptyNameError_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        prompt.interact("2");
        prompt.interact("2");
        String expectedResponse = new EditTaskCommandException("emptyInputError").getErrorMessage() + "\n\n"
            + "Please enter the task name.";
        assertEquals(expectedResponse, prompt.interact(""));
    }

    @Test
    public void interact_taskWeightError_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        prompt.interact("2");
        prompt.interact("6");
        assertTrue(prompt.interact("101")
            .contains(new EditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void interact_taskWeightErrorNegative_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("0");
        prompt.interact("2");
        prompt.interact("6");
        assertTrue(prompt.interact("-1")
            .contains(new EditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }
}
