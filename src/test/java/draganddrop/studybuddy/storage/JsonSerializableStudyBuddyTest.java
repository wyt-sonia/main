package draganddrop.studybuddy.storage;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.util.JsonUtil;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.testutil.TypicalTasks;

public class JsonSerializableStudyBuddyTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableStudyBuddyTest");
    private static final Path TYPICAL_TASKS_FILE = TEST_DATA_FOLDER.resolve("typicalTasksStudyBuddy.json");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER.resolve("invalidTaskStudyBuddy.json");

    @Test
    public void toModelType_typicalTasksFile_success() throws Exception {
        JsonSerializableStudyBuddy dataFromFile = JsonUtil.readJsonFile(TYPICAL_TASKS_FILE,
                JsonSerializableStudyBuddy.class).get();
        StudyBuddy studyBuddyFromFile = dataFromFile.toModelType();
        StudyBuddy typicalTasksStudyBuddy = TypicalTasks.getOnlyTaskList();
        assertEquals(studyBuddyFromFile, typicalTasksStudyBuddy);
    }

    @Test
    public void toModelType_invalidTaskFile_throwsIllegalValueException() throws Exception {
        JsonSerializableStudyBuddy dataFromFile = JsonUtil.readJsonFile(INVALID_TASK_FILE,
                JsonSerializableStudyBuddy.class).get();
        assertThrows(InteractiveCommandException.class, dataFromFile::toModelType);
    }


}
