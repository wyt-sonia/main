package draganddrop.studybuddy.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonSerializableStudyBuddyTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableStudyBuddyTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalTasksStudyBuddy.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidTaskStudyBuddy.json");

}
