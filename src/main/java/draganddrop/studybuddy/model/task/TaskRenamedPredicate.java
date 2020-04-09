package draganddrop.studybuddy.model.task;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests that a {@code Task}'s {@code Name} matches any of the keywords given.
 */
public class TaskRenamedPredicate implements Predicate<Task> {
    @Override
    public boolean test(Task task) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(task.getTaskName());
        return m.find();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskStatusEqualPredicate);
    }

}
