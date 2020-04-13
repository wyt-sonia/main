package draganddrop.studybuddy.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.exceptions.DataConversionException;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.testutil.Assert;

/**
 * Test class for JsonStudyBuddyStorage
 *
 * @@author Souwmyaa Sabarinathann
 */
public class JsonStudyBuddyStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonStudyBuddyStorageTest");

    @Test
    public void readStudyBuddyNullFilePathThrowsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> readStudyBuddy(null));
    }

    private java.util.Optional<ReadOnlyStudyBuddy> readStudyBuddy(String filePath) throws Exception {
        return new JsonStudyBuddyStorage(Paths.get(filePath)).readStudyBuddy(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void readMissingFileEmptyResult() throws Exception {
        assertFalse(readStudyBuddy("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        Assert.assertThrows(DataConversionException.class, () -> readStudyBuddy("notJsonFormatStudyBuddy.json"));
    }


    @Test
    public void saveStudyBuddy_nullStudyBuddy_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveStudyBuddy(null, "SomeFile.json"));
    }

    /**
     * Saves {@code studyBuddy} at the specified {@code filePath}.
     */
    private void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy, String filePath) {
        try {
            new JsonStudyBuddyStorage(Paths.get(filePath))
                .saveStudyBuddy(studyBuddy, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveStudyBuddy_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveStudyBuddy(new StudyBuddy(), null));
    }
}
