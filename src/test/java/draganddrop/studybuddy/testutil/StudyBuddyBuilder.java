package draganddrop.studybuddy.testutil;

import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.task.Task;

/**
 * A utility class to help with building StudyBuddy objects.
 * Example usage: <br>
 * {@code StudyBuddy ab = new StudyBuddyBuilder().withPerson("John", "Doe").build();}
 */
public class StudyBuddyBuilder {

    private StudyBuddy studyBuddy;

    public StudyBuddyBuilder() {
        studyBuddy = new StudyBuddy();
    }

    public StudyBuddyBuilder(StudyBuddy studyBuddy) {
        this.studyBuddy = studyBuddy;
    }

    /**
     * Adds a new {@code Person} to the {@code StudyBuddy} that we are building.
     */
    public StudyBuddyBuilder withTask(Task task) {
        studyBuddy.addTask(task);
        return this;
    }

    public StudyBuddy build() {
        return studyBuddy;
    }
}
