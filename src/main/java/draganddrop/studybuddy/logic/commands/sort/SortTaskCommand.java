package draganddrop.studybuddy.logic.commands.sort;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;

/**
 * Represent a SortTaskCommand that can handle the sorting of tasks according to
 * user's choice.
 *
 * @@author Wang Yuting
 */
public class SortTaskCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Sort the tasks by keyword chosen by user. \n"
        + "Parameters: Sort Keyword (1 (deadline / task start date), 2 (task name) and 3 creation datetime)\n"
        + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_SORT_TASK_SUCCESS = "Sort Task: %1$s";

    private String sortKeyword;

    public SortTaskCommand(String sortKeyword) {
        this.sortKeyword = sortKeyword;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.sortTasks(sortKeyword);
        return new CommandResult(String.format(MESSAGE_SORT_TASK_SUCCESS, sortKeyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof SortTaskCommand // instanceof handles nulls
            && sortKeyword.equals(((SortTaskCommand) other).sortKeyword)); // state check
    }
}
