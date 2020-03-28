package draganddrop.studdybuddy.logic.commands.view;

import static draganddrop.studdybuddy.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static java.util.Objects.requireNonNull;

import draganddrop.studdybuddy.logic.commands.Command;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.model.Model;

/**
 * Represent a ListTaskCommand that can List all persons in the task list to the user.
 */
public class ListTaskCommand extends Command {
    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
