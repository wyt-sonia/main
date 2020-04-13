package draganddrop.studybuddy.logic.commands.view;

import static draganddrop.studybuddy.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;

/**
 * Represent a ListTaskCommand that can List all tasks in the task list to the user.
 */
public class ListTaskCommand extends Command {
    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
