package draganddrop.studybuddy.commons.util;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class AppUtilTest {

    @Test
    public void getImageExitingImage() {
        assertNotNull(AppUtil.getImage("/images/study_buddy.png"));
    }

    @Test
    public void getImageNullGivenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> AppUtil.getImage(null));
    }

    @Test
    public void checkArgumentTrueNothingHappens() {
        AppUtil.checkArgument(true);
        AppUtil.checkArgument(true, "");
    }

    @Test
    public void checkArgumentFalseWithoutErrorMessageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AppUtil.checkArgument(false));
    }

    @Test
    public void checkArgumentFalseWithErrorMessageThrowsIllegalArgumentException() {
        String errorMessage = "error message";
        assertThrows(IllegalArgumentException.class, errorMessage, () -> AppUtil.checkArgument(false, errorMessage));
    }
}
