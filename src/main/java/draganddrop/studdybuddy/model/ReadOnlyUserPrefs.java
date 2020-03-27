package draganddrop.studdybuddy.model;

import java.nio.file.Path;

import draganddrop.studdybuddy.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getAddressBookFilePath();

}
