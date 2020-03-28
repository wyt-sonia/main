package draganddrop.studybuddy.model;

import java.nio.file.Path;

import draganddrop.studybuddy.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getStudyBuddyFilePath();

}
