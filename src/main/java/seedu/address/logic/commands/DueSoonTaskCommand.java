package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Archives an entry in the address book.
 */
public class DueSoonTaskCommand extends Command {

    public static final String MESSAGE_USAGE = "Checks if recently added task is due soon";
    public static final String MESSAGE_DUE_SOON_TASK_SUCCESS = "Task due soon is displayed";

    private final Task checkDueSoon;
    private final DateFormat df;
    private final Date dateObj;

    public DueSoonTaskCommand(Task checkDueSoon) {
        this.checkDueSoon = checkDueSoon;
        df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateObj = new Date();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String timeNow = df.format(dateObj);
        try {
            String taskDeadline = checkDueSoon.getTimeString();
            long difference = df.parse(taskDeadline).getTime() - dateObj.getTime();
            float daysBetween = (difference / (1000 * 60 * 60 * 24));
            if (daysBetween <= 7) {
                model.addDueSoonTask(checkDueSoon);
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
