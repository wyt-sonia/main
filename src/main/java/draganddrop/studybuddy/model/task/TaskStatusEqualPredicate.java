package draganddrop.studybuddy.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code Task}'s {@code Name} matches any of the keywords given.
 */
public class TaskStatusEqualPredicate implements Predicate<Task> {

    private TaskStatus status;

    public TaskStatusEqualPredicate(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean test(Task task) {
        return task.getTaskStatus() == status;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskStatusEqualPredicate);
    }

}
