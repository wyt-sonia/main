package draganddrop.studdybuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import draganddrop.studdybuddy.commons.exceptions.DataConversionException;
import draganddrop.studdybuddy.commons.util.JsonUtil;
import draganddrop.studdybuddy.model.ReadOnlyUserPrefs;
import draganddrop.studdybuddy.model.UserPrefs;

/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class JsonUserPrefsStorage implements UserPrefsStorage {

    private Path filePath;

    public JsonUserPrefsStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getUserPrefsFilePath() {
        return filePath;
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        JsonUtil.saveJsonFile(userPrefs, filePath);
    }

    /**
     * Similar to {@link #readUserPrefs()}
     *
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<UserPrefs> readUserPrefs(Path prefsFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(prefsFilePath, UserPrefs.class);
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException {
        return readUserPrefs(filePath);
    }
}
