package draganddrop.studybuddy.logic.commands.view;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.ui.MainWindow;

/**
 * brings up calendar immediately with task from input date shown.
 * Might not need to have this class since model is not changed.
 */
public class CalendarViewCommand extends Command {

    public static final String CALENDAR_VIEW_SUCCESS = "Now showing tasks for date:  %1$s";

    private final Logger logger = LogsCenter.getLogger(CalendarViewCommand.class);

    private LocalDate selectedDate;

    /**
     *
     */
    public CalendarViewCommand(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        logger.info("Selected date: " + selectedDate.toString());
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        MainWindow.getInstance().handleShowCalendar();
        MainWindow.getInstance().getCalendarBox().generateCalendar(selectedDate.getYear(), selectedDate.getMonth());
        MainWindow.getInstance().getCalendarBox().viewTaskByDate(selectedDate);
        return new CommandResult(String.format(CALENDAR_VIEW_SUCCESS, selectedDate.toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CalendarViewCommand
                && ((CalendarViewCommand) other).getSelectedDate().equals(this.getSelectedDate()));
    }

}

