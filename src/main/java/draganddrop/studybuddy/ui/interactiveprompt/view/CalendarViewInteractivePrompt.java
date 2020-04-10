package draganddrop.studybuddy.ui.interactiveprompt.view;

import java.text.ParseException;
import java.time.LocalDate;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.CalendarViewCommand;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.CalendarViewCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.CalendarViewCommandException;
import draganddrop.studybuddy.ui.MainWindow;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType;

/**
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */
public class CalendarViewInteractivePrompt extends InteractivePrompt {

    public static final String QUIT_COMMAND_MSG = "Successfully quited from calendar view command.";
    public static final String REQUIRED_DATE_MSG = "Please input the date which you want to view in this format:"
                                                + "\n\n    dd/MM/yyyy";
    private static final String END_OF_COMMAND_MSG = "Now viewing: ";

    private LocalDate selectedDate;
    private MainWindow mainWindow;

    public CalendarViewInteractivePrompt(MainWindow mainWindow) {
        super();
        this.interactivePromptType = InteractivePromptType.CALENDAR_VIEW;
        this.mainWindow = mainWindow;
    }
    @Override
    public String interact(String userInput) {

        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = REQUIRED_DATE_MSG;
            currentTerm = InteractivePromptTerms.DATE;
            break;

        case DATE:
            try {
                selectedDate = CalendarViewCommandParser.parseDate(userInput);
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                reply = "The tasks with date " + selectedDate.toString()
                        + " will be shown.\nPress enter to continue.";
            } catch (CalendarViewCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                CalendarViewCommand cvCommand = new CalendarViewCommand(selectedDate, mainWindow);
                logic.executeCommand(cvCommand);
                endInteract(END_OF_COMMAND_MSG + selectedDate.toString());
            } catch (CommandException | ParseException e) {
                reply = e.getMessage();
            }
            break;

        default:
        }
        return reply;
    }

    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }
}
