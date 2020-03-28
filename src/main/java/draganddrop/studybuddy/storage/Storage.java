package draganddrop.studybuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import draganddrop.studybuddy.commons.exceptions.DataConversionException;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.ReadOnlyUserPrefs;
import draganddrop.studybuddy.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends StudyBuddyStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getStudyBuddyFilePath();

    @Override
    Optional<ReadOnlyStudyBuddy> readStudyBuddy() throws DataConversionException, IOException;

    @Override
    void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy) throws IOException;

}
