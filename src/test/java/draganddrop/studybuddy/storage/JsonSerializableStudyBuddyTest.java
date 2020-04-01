package draganddrop.studybuddy.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonSerializableStudyBuddyTest {

    final private static Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableStudyBuddyTest");
    final private static Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalTasksStudyBuddy.json");
    final private static Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidTaskStudyBuddy.json");

}
