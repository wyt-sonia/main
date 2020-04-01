package draganddrop.studybuddy.logic.commands.view;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Finds and lists all tasks in the tasks list whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindTaskCommand extends Command {

    final public static String COMMAND_WORD = "find";

    final public static String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
        + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
        + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
        + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final TaskNameContainsKeywordsPredicate predicate;

    public FindTaskCommand(TaskNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, model.getFilteredTaskList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof FindTaskCommand // instanceof handles nulls
            && predicate.equals(((FindTaskCommand) other).predicate)); // state check
    }
}
