package draganddrop.studybuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import draganddrop.studybuddy.commons.exceptions.DataConversionException;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.StudyBuddy;

/**
 * Represents a storage for {@link StudyBuddy}.
 */
public interface StudyBuddyStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getStudyBuddyFilePath();

    /**
     * Returns StudyBuddy data as a {@link ReadOnlyStudyBuddy}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyStudyBuddy> readStudyBuddy() throws DataConversionException, IOException;

    /**
     * @see #getStudyBuddyFilePath()
     */
    Optional<ReadOnlyStudyBuddy> readStudyBuddy(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyStudyBuddy} to the storage.
     *
     * @param studyBuddy cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy) throws IOException;

    /**
     * @see #saveStudyBuddy(ReadOnlyStudyBuddy)
     */
    void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy, Path filePath) throws IOException;

}
