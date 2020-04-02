package draganddrop.studybuddy.model;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.statistics.GeneralStats;
import draganddrop.studybuddy.model.statistics.ScoreStats;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.testutil.TypicalTasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudyBuddyTest {

    private final StudyBuddy studyBuddy = new StudyBuddy();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), studyBuddy.getTaskList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> studyBuddy.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyStudyBuddy_replacesData() {
        StudyBuddy newData = TypicalTasks.getTypicalTaskList();
        studyBuddy.resetData(newData);
        assertEquals(newData, studyBuddy);
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> studyBuddy.hasTask(null));
    }

    @Test
    public void hasTask_taskNotInStudyBuddy_returnsFalse() {
        assertFalse(studyBuddy.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void hasTask_taskInStudyBuddy_returnsTrue() {
        studyBuddy.addTask(TypicalTasks.getSampleTasks()[0]);
        assertTrue(studyBuddy.hasTask(TypicalTasks.getSampleTasks()[0]));
    }

    @Test
    public void getTaskList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> studyBuddy.getTaskList().remove(0));
    }

    /**
     * A stub ReadOnlyStudyBuddy whose tasks list can violate interface constraints.
     */
    private static class StudyBuddyStub implements ReadOnlyStudyBuddy {
        private final ObservableList<Task> tasks = FXCollections.observableArrayList();

        @Override
        public ObservableList<Task> getTaskList() {
            return tasks;
        }

        @Override
        public List<Integer> getCompleteCountList() {
            return null;
        }

        @Override
        public List<Integer> getOverdueCountList() {
            return null;
        }

        @Override
        public ScoreStats getScoreStats() {
            return null;
        }

        @Override
        public GeneralStats getGeneralStats() {
            return null;
        }

        @Override
        public ObservableList<Task> getArchivedList() {
            return null;
        }

        @Override
        public ObservableList<Task> getDueSoonList() {
            return null;
        }

        public ObservableList<Module> getModuleList() {
            return null;
        }
    }
}
