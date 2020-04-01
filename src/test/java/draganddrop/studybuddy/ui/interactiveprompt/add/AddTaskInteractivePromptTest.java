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
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
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
    private StorageManager storageStub;
    private LogicManager logicStub;
    private AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
    private Model modelStub = new ModelManager(getTypicalTaskList(), new UserPrefs());
    private LocalDateTime endDateTime = LocalDateTime.now().plusDays(20);
    private LocalDateTime startDateTime = LocalDateTime.now().plusDays(10);

    @BeforeEach
    public void setUp() {
        JsonStudyBuddyStorage studyBuddyStorage = new JsonStudyBuddyStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageStub = new StorageManager(studyBuddyStorage, userPrefsStorage);
        logicStub = new LogicManager(modelStub, storageStub);
        for (Module m : getSampleModule()) {
            logicStub.getStudyBuddy().getModuleList().add(m);
        }
        prompt.setLogic(logicStub);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        assertTrue(prompt.interact("").contains(prompt.REQUIRED_MODULE_MSG));
    }

    @Test
    public void interact_quitCommand_returnMessage() {
        assertEquals(AddTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_moduleIndexWrongFormat_returnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void interact_multipleModuleIndexWrongFormat_returnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void interact_multipleModuleIndexWrongFormatFollowByCorrectIndex_returnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
        assertTrue(prompt.interact("1").contains("The module has been set as: "
            + modelStub.getStudyBuddy().getModuleList().get(0).toString()));
    }

    @Test
    public void interact_moduleCode_returnMessage() {
        Module module = logicStub.getStudyBuddy().getModuleList().get(0);
        prompt.interact("");
        assertTrue(prompt.interact(module.getModuleCode().toString())
            .contains("The module has been set as: " + module.toString()));
    }

    @Test
    public void interact_invalidModuleCode_returnMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("CSCSCS")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void interact_moduleIndexOutOfRangeZero_returnErrorMessage() {
        prompt.interact("");
        assertTrue(prompt.interact("0")
            .contains(new AddTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void interact_moduleIndexOutOfRangeSizePluOne_returnErrorMessage() {
        int size = logicStub.getStudyBuddy().getModuleList().size();
        prompt.interact("");
        assertTrue(prompt.interact(size + 1 + "")
            .contains(new AddTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void interact_validModuleCode_returnMessage() {
        String code = logicStub.getStudyBuddy().getModuleList().get(0).getModuleCode().toString();
        prompt.interact("");
        assertTrue(prompt.interact(code)
            .contains(code));
    }

    @Test
    public void interact_pastDateTime_returnErrorMessage() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("1");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(pastDateTime))
            .contains(new AddTaskCommandException("pastDateTime").getErrorMessage()));
    }

    @Test
    public void interact_endBeforeStartDateTime_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(endDateTime)
            + "-" + TimeParser.getDateTimeString(startDateTime))
            .contains(new AddTaskCommandException("eventEndBeforeStartError").getErrorMessage()));
    }

    @Test
    public void interact_validDateTime_returnMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        assertTrue(prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime))
            .contains(TimeParser.getDateTimeString(startDateTime) + "-" + TimeParser.getDateTimeString(endDateTime)));
    }

    @Test
    public void interact_emptyTaskIndex_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact("")
            .contains(new AddTaskCommandException("emptyInputError").getErrorMessage()));
    }

    @Test
    public void interact_emptyTaskName_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("")
            .contains(new AddTaskCommandException("emptyInputError").getErrorMessage()));
    }

    @Test
    public void interact_tooLongTaskName_returnErrorMessage() {
        String longTaskName = "This Task Name is Longer Than 20 Char";
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact(longTaskName)
            .contains(new AddTaskCommandException("taskNameLengthError").getErrorMessage()));
    }

    @Test
    public void interact_validTaskName_returnMessage() {
        prompt.interact("");
        prompt.interact("");
        assertTrue(prompt.interact("taskName")
            .contains("The name of task is set to: taskName."));
    }

    @Test
    public void interact_wrongTaskTypeIndexFormat_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact("wrongIndexFormat")
            .contains(new AddTaskCommandException("wrongIndexFormatError").getErrorMessage()));
    }

    @Test
    public void interact_wrongTaskTypeIndexRangeZero_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        String t = prompt.interact("0");
        assertTrue(prompt.interact("0")
            .contains(new AddTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void interact_wrongTaskTypeIndexRangeSizePlusOne_returnErrorMessage() {
        int size = TaskType.getTaskTypes().length;
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact(size + 1 + "")
            .contains(new AddTaskCommandException("invalidIndexRangeError").getErrorMessage()));
    }

    @Test
    public void interact_validTaskTypeIndex_returnMessage() {
        int size = TaskType.getTaskTypes().length;
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        assertTrue(prompt.interact(size + "")
            .contains(TaskType.getTaskTypes()[size - 1].toString()));
    }

    @Test
    public void interact_tooLongTaskDescIndex_returnErrorMessage() {
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
            .contains(new AddTaskCommandException("taskDescriptionLengthError").getErrorMessage()));
    }

    @Test
    public void interact_emptyTaskDescIndex_returnMessage() {
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
    public void interact_wrongWeightFormat_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("wrongWeightFormat")
            .contains(new AddTaskCommandException("wrongWeightFormatError").getErrorMessage()));
    }

    @Test
    public void interact_wrongWeightValueOver100_returnErrorMessage() {

        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("101")
            .contains(new AddTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }

    @Test
    public void interact_wrongWeightValueNegative_returnErrorMessage() {
        prompt.interact("");
        prompt.interact("");
        prompt.interact("taskName");
        prompt.interact("2");
        prompt.interact(TimeParser.getDateTimeString(startDateTime)
            + "-" + TimeParser.getDateTimeString(endDateTime));
        prompt.interact("");
        assertTrue(prompt.interact("-1")
            .contains(new AddTaskCommandException("wrongWeightRangeError").getErrorMessage()));
    }
}
