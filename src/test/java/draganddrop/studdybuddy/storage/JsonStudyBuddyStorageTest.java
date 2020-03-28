package draganddrop.studdybuddy.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import draganddrop.studdybuddy.commons.exceptions.DataConversionException;
import draganddrop.studdybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studdybuddy.model.StudyBuddy;
import draganddrop.studdybuddy.testutil.Assert;

public class JsonStudyBuddyStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonStudyBuddyStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readStudyBuddy_nullFilePath_throwsNullPointerException() {
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
    public void read_missingFile_emptyResult() throws Exception {
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
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveStudyBuddy(ReadOnlyStudyBuddy addressBook, String filePath) {
        try {
            new JsonStudyBuddyStorage(Paths.get(filePath))
                .saveStudyBuddy(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveStudyBuddy_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveStudyBuddy(new StudyBuddy(), null));
    }
}
