package draganddrop.studybuddy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.StudyBuddyBuilder;
import draganddrop.studybuddy.testutil.TypicalTasks;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new StudyBuddy(), new StudyBuddy(modelManager.getStudyBuddy()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
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
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setStudyBuddyFilePath_nullPath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.setStudyBuddyFilePath(null));
    }

    @Test
    public void setStudyBuddyFilePath_validPath_setsStudyBuddyFilePath() {
        Path path = Paths.get("studyBuddy/file/path");
        modelManager.setStudyBuddyFilePath(path);
        assertEquals(path, modelManager.getStudyBuddyFilePath());
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> modelManager.hasTask(null));
    }

    @Test
    public void hasTask_taskNotInStudyBuddy_returnsFalse() {
        assertFalse(modelManager.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void hasTask_taskInStudyBuddy_returnsTrue() {
        modelManager.addTask(TypicalTasks.getSampleTasks()[0]);
        assertTrue(modelManager.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
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
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager == null);

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(Model.PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setStudyBuddyFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(studyBuddy, differentUserPrefs)));
    }
}
