package draganddrop.studdybuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.commons.exceptions.DataConversionException;
import draganddrop.studdybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studdybuddy.model.ReadOnlyUserPrefs;
import draganddrop.studdybuddy.model.UserPrefs;

/**
 * Manages storage of StudyBuddy data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private StudyBuddyStorage studyBuddyStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(StudyBuddyStorage studyBuddyStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.studyBuddyStorage = studyBuddyStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ StudyBuddy methods ==============================

    @Override
    public Path getStudyBuddyFilePath() {
        return studyBuddyStorage.getStudyBuddyFilePath();
    }

    @Override
    public Optional<ReadOnlyStudyBuddy> readStudyBuddy() throws DataConversionException, IOException {
        return readStudyBuddy(studyBuddyStorage.getStudyBuddyFilePath());
    }

    @Override
    public Optional<ReadOnlyStudyBuddy> readStudyBuddy(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return studyBuddyStorage.readStudyBuddy(filePath);
    }

    @Override
    public void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy) throws IOException {
        saveStudyBuddy(studyBuddy, studyBuddyStorage.getStudyBuddyFilePath());
    }

    @Override
    public void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        studyBuddyStorage.saveStudyBuddy(studyBuddy, filePath);
    }

}
