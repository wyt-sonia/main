package draganddrop.studybuddy.testutil;

import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.task.Task;

/**
 * A utility class to help with building StudyBuddy objects.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class StudyBuddyBuilder {

    private StudyBuddy studyBuddy;

    public StudyBuddyBuilder() {
        studyBuddy = new StudyBuddy();
    }

    /**
     * Adds a new {@code Task} to the {@code StudyBuddy} that we are building.
     */
    public StudyBuddyBuilder withTask(Task task) {
        studyBuddy.addTask(task);
        return this;
    }

    public StudyBuddy build() {
        return studyBuddy;
    }
}
