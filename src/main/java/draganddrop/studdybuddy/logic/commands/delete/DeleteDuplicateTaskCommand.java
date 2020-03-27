package draganddrop.studdybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studdybuddy.logic.commands.Command;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.task.Task;

/**
 * Deletes all repeated tasks from the list.
 */
public class DeleteDuplicateTaskCommand extends Command {

    public static final String COMMAND_WORD = "delete duplicates";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes any duplicate tasks in the displayed task list.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DELETE_DUPLICATE_TASK_SUCCESS = "Deleted duplicate tasks";


    public DeleteDuplicateTaskCommand() {

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        for (int i = 0; i < lastShownList.size(); i++) {
            Task task1 = lastShownList.get(i);
            for (int j = 0; j < lastShownList.size(); j++) {
                if (i == j) {
                    continue;
                } else {
                    Task task2 = lastShownList.get(j);
                    if (task1.getTaskType() != task2.getTaskType()) {
                        continue;
                    } else {
                        if (!task1.getTaskName().equals(task2.getTaskName())) {
                            continue;
                        } else {
                            if (!task1.getTimeString().equals(task2.getTimeString())) {
                                continue;
                            } else {
                                Task taskToDelete = lastShownList.get(i);
                                model.deleteTask(taskToDelete);

                                lastShownList = model.getFilteredTaskList();
                                break;
                            }
                        }
                    }
                }
            }
        }

        return new CommandResult(String.format(MESSAGE_DELETE_DUPLICATE_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteDuplicateTaskCommand);
    }

}
