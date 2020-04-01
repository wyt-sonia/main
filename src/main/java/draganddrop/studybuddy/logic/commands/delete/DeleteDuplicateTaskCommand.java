package draganddrop.studybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Deletes all repeated tasks from the list.
 */
public class DeleteDuplicateTaskCommand extends Command {

    public static final String COMMAND_WORD = "delete duplicates";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        for (int i = 0; i < lastShownList.size(); i++) {
            Task task1 = lastShownList.get(i);
            for (int j = 0; j < lastShownList.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (i != j) {
                    Task task2 = lastShownList.get(j);
                    if (task1.isSameTask(task2)) {
                        Task taskToDelete = lastShownList.get(i);
                        model.deleteTask(taskToDelete);
                        lastShownList = model.getFilteredTaskList();
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        return new CommandResult(String.format(Messages.MESSAGE_DELETE_DUPLICATE_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteDuplicateTaskCommand);
    }

}
