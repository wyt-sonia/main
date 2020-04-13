package draganddrop.studybuddy.model;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.testutil.TypicalTasks;

/**
 * Test class for Study Buddy
 *
 * @@author Souwmyaa Sabarinathann
 */
public class StudyBuddyTest {

    private final StudyBuddy studyBuddy = new StudyBuddy();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), studyBuddy.getTaskList());
    }

    @Test
    public void resetDatanullthrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> studyBuddy.resetData(null));
    }

    @Test
    public void resetDatawithValidReadOnlyStudyBuddyreplacesData() {
        StudyBuddy newData = TypicalTasks.getTypicalTaskList();
        studyBuddy.resetData(newData);
        assertEquals(newData, studyBuddy);
    }

    @Test
    public void hasTasknullTaskthrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> studyBuddy.hasTask(null));
    }

    @Test
    public void hasTasktaskNotInStudyBuddyreturnsFalse() {
        assertFalse(studyBuddy.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void hasTasktaskInStudyBuddyreturnsTrue() {
        studyBuddy.addTask(TypicalTasks.getSampleTasks()[0]);
        assertTrue(studyBuddy.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void getTaskListmodifyListthrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> studyBuddy.getTaskList().remove(0));
    }

}
