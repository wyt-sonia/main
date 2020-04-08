package draganddrop.studybuddy.logic.commands.view;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Refreshes the Due Soon list as well as status tags.
 */
public class RefreshCommand extends Command {

    private final Logger logger = LogsCenter.getLogger(RefreshCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        logger.info("Attempting to refresh Due Soon list and tags");
        for (int i = 0; i < lastShownList.size(); i++) {
            Task task = lastShownList.get(i);
            refreshStatus(task, model);
            if (task.isDueSoon()) {
                model.addDueSoonTask(task);
            } else {
                model.deleteDueSoonTask(task);
            }
        }
        return new CommandResult(String.format(Messages.MESSAGE_DUE_SOON_TASK_SUCCESS));
    }

    /**
     * This function refreshes the status tags.
     * @param task
     * @param model
     */
    private void refreshStatus(Task task, Model model) {
        boolean isStatusExpired = task.isStatusExpired();
        if (isStatusExpired) {
            Task temp = task;
            model.deleteTask(task);
            temp.freshStatus();
            model.addTask(temp);
            model.sortTasks("deadline / task start date");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefreshCommand); // state check
    }

}
