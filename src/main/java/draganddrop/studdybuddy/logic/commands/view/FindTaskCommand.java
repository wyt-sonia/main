package draganddrop.studdybuddy.logic.commands.view;

import static java.util.Objects.requireNonNull;

import draganddrop.studdybuddy.commons.core.Messages;
import draganddrop.studdybuddy.logic.commands.Command;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Finds and lists all tasks in the tasks list whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindTaskCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
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
