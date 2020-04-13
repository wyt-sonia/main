package draganddrop.studybuddy.model;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.testutil.Assert;

public class UserPrefsTest {

    @Test
    public void setGuiSettingsNullGuiSettingsThrowsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        Assert.assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setStudyBuddyFilePathNullPathThrowsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        Assert.assertThrows(NullPointerException.class, () -> userPrefs.setStudyBuddyFilePath(null));
    }

}
