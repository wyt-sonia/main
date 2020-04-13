package draganddrop.studybuddy.logic.commands.sort;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;

/**
 * Represent a SortTaskCommand that can handle the sorting of tasks according to
 * user's choice.
 */
public class SortTaskCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SORT_TASK_SUCCESS = "Sort Task: %1$s";
    private static final String LOG_TAG = "TaskListPanel";
    private static final Logger logger = LogsCenter.getLogger(SortTaskCommand.class);

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Sort the tasks by keyword chosen by user. \n"
        + "Parameters: Sort Keyword (1 (deadline / task start date), 2 (task name) and 3 creation datetime)\n"
        + "Example: " + COMMAND_WORD + " 1 ";

    private String sortKeyword;

    public SortTaskCommand(String sortKeyword) {
        this.sortKeyword = sortKeyword;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.info(LOG_TAG + ": Start of executing the sort command.");
        model.sortTasks(sortKeyword);
        logger.info(LOG_TAG + ": End of executing the sort command.");
        return new CommandResult(String.format(MESSAGE_SORT_TASK_SUCCESS, sortKeyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof SortTaskCommand // instanceof handles nulls
            && sortKeyword.equals(((SortTaskCommand) other).sortKeyword)); // state check
    }
}
