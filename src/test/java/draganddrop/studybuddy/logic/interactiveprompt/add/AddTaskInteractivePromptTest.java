package draganddrop.studybuddy.logic.interactiveprompt.add;

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
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.StorageManager;

class AddTaskInteractivePromptTest {

    @TempDir
    public Path testFolder;
    private AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
    private Model modelStub = new ModelManager(getTypicalTaskList(), new UserPrefs());
    private LocalDateTime endDateTime = LocalDateTime.now().plusDays(20);
    private LocalDateTime startDateTime = LocalDateTime.now().plusDays(10);

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
    public void addTaskInteractivePromptTestInteractFirstInputReturnKeywordPrompt() {
        assertTrue(prompt.interact("").contains(prompt.REQUIRED_MODULE_MSG));
    }

    @Test
    public void addTaskInteractivePromptTestInteractQuitCommandReturnMessage() {
        assertEquals(AddTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void addTaskInteractivePromptTestInteractModuleIndexWrongFormatReturnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractMultipleModuleIndexWrongFormatReturnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractMultipleModuleIndexWrongFormatFollowByValidIndexReturnMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("1").contains("The module has been set as: "
            + modelStub.getStudyBuddy().getModuleList().get(0).toString()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractModuleCodeReturnMessage() {
        Module module = modelStub.getStudyBuddy().getModuleList().get(0);
        prompt.interact("");
        assertTrue(prompt.interact(module.getModuleCode().toString())
            .contains("The module has been set as: " + module.toString()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractInvalidModuleCodeReturnMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("CSCSCS")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractModuleIndexOutOfRangeZeroReturnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("0")
            .contains(new AddOrEditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractModuleIndexOutOfRangeSizePluOneReturnErrorMessage() {
        int size = modelStub.getStudyBuddy().getModuleList().size();
        prompt.interact("");
        assertTrue(prompt.interact(size + 1 + "")
            .contains(new AddOrEditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidModuleCodeReturnMessage() {
        String code = modelStub.getStudyBuddy().getModuleList().get(0).getModuleCode().toString();
        prompt.interact("");
        assertTrue(prompt.interact(code)
            .contains(code));
    }

    @Test
    public void addTaskInteractivePromptTestInteractPastDateTimeReturnErrorMessage() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("1");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(pastDateTime))
            .contains(new AddOrEditTaskCommandException("pastDateTime").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractEndBeforeStartDateTimeReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(endDateTime)
            + "-" + TimeParser.getDateTimeString(startDateTime))
            .contains(new AddOrEditTaskCommandException("eventEndBeforeStartError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidDateTimeReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime))
            .contains(TimeParser.getDateTimeString(startDateTime) + "-" + TimeParser.getDateTimeString(endDateTime)));
    }

    @Test
    public void addTaskInteractivePromptTestInteractEmptyTaskIndexReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact("")
            .contains(new AddOrEditTaskCommandException("emptyInputError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractEmptyTaskNameReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("")
            .contains(new AddOrEditTaskCommandException("emptyInputError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractTooLongTaskNameReturnErrorMessage() {
        String longTaskName = "This Task Name is Longer Than 20 Char";
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact(longTaskName)
            .contains(new AddOrEditTaskCommandException("taskNameLengthError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidTaskNameReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("taskName")
            .contains("The name of task is set to: taskName."));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongTaskTypeIndexFormatReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongTaskTypeIndexRangeZeroReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("0");
        assertTrue(prompt.interact("0")
            .contains(new AddOrEditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongTaskTypeIndexRangeSizePlusOneReturnErrorMessage() {
        int size = TaskType.getTaskTypes().length;
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact(size + 1 + "")
            .contains(new AddOrEditTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidTaskTypeIndexReturnMessage() {
        int size = TaskType.getTaskTypes().length;
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact(size + "")
            .contains(TaskType.getTaskTypes()[size - 1].toString()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractTooLongTaskDescIndexReturnErrorMessage() {
        String longTaskDescription = "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong"
            + "Thistaskdescriptionismorethan300characterslong";
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        assertTrue(prompt.interact(longTaskDescription)
            .contains(new AddOrEditTaskCommandException("taskDescriptionLengthError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractEmptyTaskDescIndexReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        assertTrue(prompt.interact("")
            .contains("Please enter the weight of the task or press enter to skip."));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongWeightFormatReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("wrongWeightFormat")
            .contains(new AddOrEditTaskCommandException("wrongWeightFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongWeightValueOver100ReturnErrorMessage() {

        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("101")
            .contains(new AddOrEditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongWeightValueNegativeReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("-1")
            .contains(new AddOrEditTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractEmptyWeightReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("")
            .contains("Please enter the estimated number of hours cost or press enter to skip."));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidWeightValueReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("10")
            .contains("Please enter the estimated number of hours cost or press enter to skip."));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongTimeCostFormatReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("wrongTimeCostFormat")
            .contains(new AddOrEditTaskCommandException("wrongEstimatedTimeFormatError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractWrongTimeCostValueNegativeReturnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("-1")
            .contains(new AddOrEditTaskCommandException("wrongEstimatedTimeRangeError").getErrorMessage()));
    }

    @Test
    public void addTaskInteractivePromptTestInteractValidTimeCostValueReturnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("10")
            .contains("The estimated number of hours the task might take has been set as 10"));
    }
}
