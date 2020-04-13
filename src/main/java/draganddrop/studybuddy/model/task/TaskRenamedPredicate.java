package draganddrop.studybuddy.model.task;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests that a {@code Task} has been renamed.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class TaskRenamedPredicate implements Predicate<Task> {
    @Override
    public boolean test(Task task) {
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(task.getTaskName());
        return matcher.find();

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskStatusEqualPredicate);
    }

}
