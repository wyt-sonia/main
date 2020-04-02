package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import draganddrop.studybuddy.logic.LogicManager;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.StorageManager;
import javafx.collections.ObservableList;

/**
 * This is the test class for clear tasks interactive prompt
 */
class EditTaskInteractivePromptTestTest {

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
    public void editTaskInteractivePromptTestInteractQuitCommandReturnMessage() {
        EditTaskInteractivePrompt prompt = new EditTaskInteractivePrompt();
        assertEquals(EditTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void editTaskInteractivePromptTestInteractFirstInputReturnKeywordPrompt() {
        assertEquals("Please enter the index of the task that you wish to edit.",
            prompt.interact(""));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskIndexLowerBoundaryReturnMessage() {
        ObservableList<Task> tasksStub = modelStub.getStudyBuddy().getTaskList();
        String lowerBoundary = "1";
        prompt.interact("");
        String expectedLowerResponse = "Please choose the field that you wish to edit for task: "
            + tasksStub.get(0).getTaskName() + ".\n\n" + TaskField.getFieldString();
        String actualLowerResponse = prompt.interact(lowerBoundary);
        assertEquals(expectedLowerResponse, actualLowerResponse);
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskIndexHigherBoundaryReturnMessage() {
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
    public void editTaskInteractivePromptTestInteractTaskIndexOutOfRangeZeroReturnErrorMessage() {
        String zero = "0";
        prompt.interact("");
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact(zero));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskIndexOutOfRangeSizePlusOneReturnErrorMessage() {
        ObservableList<Task> tasksStub = modelStub.getStudyBuddy().getTaskList();
        int taskListSizePlusOne = tasksStub.size() + 1;
        prompt.interact("");
        assertEquals(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage(),
            prompt.interact(taskListSizePlusOne + ""));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskIndexWrongFormatReturnErrorMessage() {
        prompt.interact("");
        assertEquals(new EditTaskCommandException("wrongIndexFormatError").getErrorMessage(),
            prompt.interact("wrongIndexFormat"));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskFieldIndexWrongFormatReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("1");
        prompt.interact("wrongIndexFormat");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new EditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskFieldIndexOutOfRangeZeroReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("1");
        assertTrue(prompt.interact("0")
            .contains(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskFieldIndexOutOfRangeSizePlusOneReturnErrorMessage() {
        int size = TaskField.values().length;
        prompt.interact("");
        prompt.interact("1");
        assertTrue(prompt.interact(size + 1 + "")
            .contains(new EditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskEmptyNameErrorReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("2");
        String expectedResponse = new EditTaskCommandException("emptyInputError").getErrorMessage() + "\n\n"
            + "Please enter the task name.";
        assertEquals(expectedResponse, prompt.interact(""));
    }

    @Test
    public void editTaskInteractivePromptTestInteractTaskWeightErrorReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("6");
        assertTrue(prompt.interact("101")
            .contains(new EditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void editTaskInteractivePromptTestiIteractTaskWeightErrorNegativeReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("6");
        assertTrue(prompt.interact("-1")
            .contains(new EditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void editTaskInteractivePromptTestInteracEditTaskNameReturnErrorMessage() {
        String newName = "new task name";
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("2");
        prompt.interact(newName);
        assertEquals(newName, modelStub.getStudyBuddy().getTaskList().get(1).getTaskName());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditModuleReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("1");
        prompt.interact("");
        assertEquals(new EmptyModule(), modelStub.getStudyBuddy().getTaskList().get(1).getModule());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditTaskWeightReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("6");
        prompt.interact("0.0");
        assertEquals(0.0, modelStub.getStudyBuddy().getTaskList().get(1).getWeight());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditTaskDescriptionReturnErrorMessage() {
        String taskDescription = "taskDescription";
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("5");
        prompt.interact(taskDescription);
        assertEquals(taskDescription, modelStub.getStudyBuddy().getTaskList().get(1).getTaskDescription());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditTaskTimeCostReturnErrorMessage() {
        double timeCost = 10.0;
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("7");
        prompt.interact(timeCost + "");
        assertEquals(timeCost, modelStub.getStudyBuddy().getTaskList().get(1).getEstimatedTimeCost());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditTaskTypeReturnErrorMessage() {
        TaskType taskType = TaskType.getTaskTypes()[0];
        prompt.interact("");
        prompt.interact("2");
        prompt.interact("3");
        prompt.interact("1");
        assertEquals(taskType, modelStub.getStudyBuddy().getTaskList().get(1).getTaskType());
    }

    @Test
    public void editTaskInteractivePromptTestInteractEditTaskDateReturnErrorMessage() {
        int index = 0;
        for (Task t : modelStub.getStudyBuddy().getTaskList()) {
            if (t.getTaskType().equals(TaskType.Assignment)) {
                index = modelStub.getStudyBuddy().getTaskList().indexOf(t);
                break;
            }
        }
        String dateTime = TimeParser.getDateTimeString(LocalDateTime.now().plusDays(7));
        prompt.interact("");
        prompt.interact(index + 1 + "");
        prompt.interact("4");
        prompt.interact(dateTime);
        assertEquals(dateTime,
            TimeParser.getDateTimeString(modelStub.getStudyBuddy().getTaskList().get(index).getDateTimes()[0]));
    }
}
