package seedu.address.ui.interactiveprompt;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static seedu.address.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.address.logic.commands.AddTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.TimeParser;
import seedu.address.logic.parser.interactivecommandparser.AddTaskCommandParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskType;

/**
 * pending.
 */
public class AddTaskInteractivePrompt extends InteractivePrompt {

    private String reply;
    private String userInput;
    private Task task;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;

    public AddTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ADD_TASK;
        this.reply = "";
        this.userInput = "";
        this.task = new Task();
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            // exit the command
            super.setQuit(true);
        } else if (userInput.equals("back")) {
            if (lastTerm != null) {
                terms.remove(terms.size() - 1);
                currentTerm = lastTerm;
                if (lastTerm.equals(InteractivePromptTerms.INIT)) {
                    lastTerm = null;
                } else {
                    lastTerm = terms.get(terms.size() - 1);
                }
                userInput = "";
            } else {
                this.reply = "Please type quit to exit from this command.";
            }
        }

        switch (currentTerm) {

        case INIT:
            this.reply = "Please enter the task name.";
            currentTerm = InteractivePromptTerms.TASK_NAME;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;

        case TASK_NAME:
            try {
                userInput = AddTaskCommandParser.parseName(userInput);

                this.reply = "The name of task is set to: " + userInput + ".\n"
                    + "Please choose the task type:\n"
                    + TaskType.getTypeString();

                task.setTaskName(userInput);
                currentTerm = InteractivePromptTerms.TASK_TYPE;
                lastTerm = InteractivePromptTerms.TASK_NAME;
                terms.add(lastTerm);
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case TASK_TYPE:
            try {
                TaskType taskType = AddTaskCommandParser.parseType(userInput, TaskType.getTaskTypes().length);
                task.setTaskType(taskType);
                task.setStatus("pending");
                userInput = taskType.toString();

                this.reply = "The type of task is set to: " + userInput + ".\n"
                    + "Please enter the deadline with format: ";
                if (taskType.equals(TaskType.Assignment)) {
                    this.reply += "HH:mm dd/MM/yyyy";
                } else {
                    this.reply += "HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy";
                }

                currentTerm = InteractivePromptTerms.TASK_DATETIME;
                lastTerm = InteractivePromptTerms.TASK_TYPE;
                terms.add(lastTerm);
            } catch (NumberFormatException ex) {
                reply = (new AddTaskCommandException("wrongIndexFormat")).getErrorMessage();
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case TASK_DATETIME:
            try {
                LocalDateTime[] dateTimes = AddTaskCommandParser.parseDateTime(userInput, task.getTaskType());
                task.setDateTimes(dateTimes);

                if (dateTimes.length == 1) {
                    userInput = TimeParser.getDateTimeString(dateTimes[0]);
                } else {
                    userInput = TimeParser.getDateTimeString(dateTimes[0])
                        + "-" + TimeParser.getDateTimeString(dateTimes[1]);
                }

                reply = "The date and time is set to: " + userInput + "\n"
                    + "Press enter again to add the task:\n"
                    + task.getTaskName() + " " + task.getTaskType().toString() + " " + task.getTimeString();

                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.TASK_DATETIME;
                terms.add(lastTerm);
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                AddTaskCommand addTaskCommand = new AddTaskCommand(task);
                //logic.executeCommand(addTaskCommand);
                logic.executeCommand(addTaskCommand);
                reply = "Task Added!";
                super.setEndOfCommand(true);
            } catch (CommandException ex) {
                reply = ex.getMessage();
            }
            break;

        case TASK_MODULE:
            //TASK_MODULE under construction
            break;

        case TASK_WEIGHT:
            //TASK_WEIGHT under construction
            break;

        case TASK_DESCRIPTION:
            //TASK_DESCRIPTION under construction
            break;

        case TASK_ESTIMATED_TIME_COST:
            //TASK_ESTIMATED_TIME_COST under construction
            break;

        default:
        }
        return reply;
    }


    @Override
    public void interruptInteract() {

    }

    @Override
    public void endInteract() {

    }

    @Override
    public void back() {

    }

    @Override
    public void next() {

    }

    /**
     * pending.
     */
    private String dateTime() {


        return "";
    }
}
