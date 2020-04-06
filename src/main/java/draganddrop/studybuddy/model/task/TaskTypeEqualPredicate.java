package draganddrop.studybuddy.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code Task}'s {@code Name} matches any of the keywords given.
 */
public class TaskTypeEqualPredicate implements Predicate<Task> {

    private TaskType type;

    public TaskTypeEqualPredicate(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean test(Task task) {
        return task.getTaskType() == type;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskTypeEqualPredicate);
    }

}
