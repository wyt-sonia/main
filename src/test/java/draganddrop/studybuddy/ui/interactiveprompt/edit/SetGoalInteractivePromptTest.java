package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import draganddrop.studybuddy.logic.LogicManager;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.statistics.CompletionStats;
import draganddrop.studybuddy.model.statistics.GeneralStats;
import draganddrop.studybuddy.model.statistics.OverdueStats;
import draganddrop.studybuddy.model.statistics.ScoreStats;
import draganddrop.studybuddy.model.statistics.Statistics;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.StorageManager;

class SetGoalInteractivePromptTest {

    @TempDir
    public Path testFolder;
    private StorageManager storageStub;
    private LogicManager logicStub;
    private SetGoalInteractivePrompt prompt = new SetGoalInteractivePrompt();
    private Model modelStub = new ModelManager(getTypicalTaskList(), new UserPrefs());
    private Statistics statistics = new Statistics(new GeneralStats(), new CompletionStats(),
        new OverdueStats(), new ScoreStats());

    @BeforeEach
    public void setUp() {
        JsonStudyBuddyStorage studyBuddyStorage = new JsonStudyBuddyStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageStub = new StorageManager(studyBuddyStorage, userPrefsStorage);
        logicStub = new LogicManager(modelStub, storageStub);
        prompt.setLogic(logicStub);
        SetGoalInteractivePrompt.setStatistics(statistics);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    void handleSetGoal() {
        prompt.parseGoalValue("14");
        assertEquals(14, statistics.getGoal());
    }

    @Test
    void interact() {
        prompt.interact("goal");
        assertEquals(prompt.getEndOfCommandMsg(), prompt.interact("15"));
    }

    @Test
    void interactValidInputCorrectStatistics() {
        prompt.interact("goal");
        prompt.interact("16");
        assertEquals(statistics.getGoal(), 16);
    }

    @Test
    void interactInvalidInputGetException() {
        prompt.interact("goal");
        prompt.interact("102");
        String errorMessage = (new EditTaskCommandException("invalidIndexRangeError")).getErrorMessage();
        assertEquals(errorMessage, prompt.interact("102"));
    }

}
