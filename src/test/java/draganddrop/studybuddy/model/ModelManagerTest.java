package draganddrop.studybuddy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.StudyBuddyBuilder;
import draganddrop.studybuddy.testutil.TypicalTasks;

/**
 * Test class for ModelManager.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        Assertions.assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new StudyBuddy(), new StudyBuddy(modelManager.getStudyBuddy()));
    }

    @Test
    public void setUserPrefsnullUserPrefsthrowsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefsvalidUserPrefscopiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setStudyBuddyFilePath(Paths.get("studyBuddy/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setStudyBuddyFilePath(Paths.get("new/studyBuddy/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettingsnullGuiSettingsthrowsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettingsvalidGuiSettingssetsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setStudyBuddyFilePathnullPaththrowsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setStudyBuddyFilePath(null));
    }

    @Test
    public void setStudyBuddyFilePathvalidPathsetsStudyBuddyFilePath() {
        Path path = Paths.get("studyBuddy/file/path");
        modelManager.setStudyBuddyFilePath(path);
        assertEquals(path, modelManager.getStudyBuddyFilePath());
    }

    @Test
    public void hasTasknullTaskthrowsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.hasTask(null));
    }

    @Test
    public void hasTasktaskNotInStudyBuddyreturnsFalse() {
        assertFalse(modelManager.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void hasTasktaskInStudyBuddyreturnsTrue() {
        modelManager.addTask(TypicalTasks.getSampleTasks()[0]);
        assertTrue(modelManager.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void getFilteredTaskListmodifyListthrowsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredTaskList().remove(0));
    }

    @Test
    public void equals() {
        StudyBuddy studyBuddy = new StudyBuddyBuilder().withTask(TypicalTasks.getSampleTasks()[0])
                .withTask(TypicalTasks.getSampleTasks()[1]).build();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(studyBuddy, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(studyBuddy, userPrefs);
        assertEquals(modelManager, modelManagerCopy);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotNull(modelManager);

        // different types -> returns false
        assertNotEquals(5, modelManager);

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(Model.PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setStudyBuddyFilePath(Paths.get("differentFilePath"));
        assertNotEquals(modelManager, new ModelManager(studyBuddy, differentUserPrefs));
    }
}
