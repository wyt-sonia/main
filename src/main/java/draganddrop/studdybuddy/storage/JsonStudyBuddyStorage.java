package draganddrop.studdybuddy.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.commons.exceptions.DataConversionException;
import draganddrop.studdybuddy.commons.exceptions.IllegalValueException;
import draganddrop.studdybuddy.commons.util.FileUtil;
import draganddrop.studdybuddy.commons.util.JsonUtil;
import draganddrop.studdybuddy.model.ReadOnlyStudyBuddy;

/**
 * A class to access StudyBuddy data stored as a json file on the hard disk.
 */
public class JsonStudyBuddyStorage implements StudyBuddyStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonStudyBuddyStorage.class);

    private Path filePath;

    public JsonStudyBuddyStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getStudyBuddyFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyStudyBuddy> readStudyBuddy() throws DataConversionException {
        return readStudyBuddy(filePath);
    }

    /**
     * Similar to {@link #readStudyBuddy()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyStudyBuddy> readStudyBuddy(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableStudyBuddy> jsonStudyBuddy = JsonUtil.readJsonFile(
            filePath, JsonSerializableStudyBuddy.class);
        if (!jsonStudyBuddy.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonStudyBuddy.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy) throws IOException {
        saveStudyBuddy(studyBuddy, filePath);
    }

    /**
     * Similar to {@link #saveStudyBuddy(ReadOnlyStudyBuddy)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveStudyBuddy(ReadOnlyStudyBuddy studyBuddy, Path filePath) throws IOException {
        requireNonNull(studyBuddy);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableStudyBuddy(studyBuddy), filePath);
    }

}
