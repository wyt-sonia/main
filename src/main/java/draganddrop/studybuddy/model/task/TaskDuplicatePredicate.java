package draganddrop.studybuddy.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code Task}'s {@code Name} matches any of the keywords given.
 */
public class TaskDuplicatePredicate implements Predicate<Task> {

    @Override
    public boolean test(Task task) {
        return task.isDuplicate();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskStatusEqualPredicate);
    }

}
