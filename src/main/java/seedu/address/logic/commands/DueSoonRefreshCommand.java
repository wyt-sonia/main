package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Archives an entry in the address book.
 */
public class DueSoonRefreshCommand extends Command {

    public static final String MESSAGE_USAGE = "Refreshes the tasks that are due soon";
    public static final String MESSAGE_DUE_SOON_TASK_SUCCESS = "Tasks due soon are displayed";

    private final DateFormat df;
    private final Date dateObj;

    public DueSoonRefreshCommand() {
        df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateObj = new Date();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        String timeNow = df.format(dateObj);
        try {
            for (int i = 0; i < lastShownList.size(); i++) {
                Task task = lastShownList.get(i);
                String taskDeadline = task.getTimeString();
                long difference = df.parse(taskDeadline).getTime() - dateObj.getTime();
                float daysBetween = (difference / (1000 * 60 * 60 * 24));
                System.out.println(" " + daysBetween);
                if (daysBetween <= 7) {
                    model.addDueSoonTask(task);
                }
            }
        } catch (ParseException ex) {
            System.out.println("Problem");
        }
        return new CommandResult(String.format(MESSAGE_DUE_SOON_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DueSoonTaskCommand); // state check
    }

}
