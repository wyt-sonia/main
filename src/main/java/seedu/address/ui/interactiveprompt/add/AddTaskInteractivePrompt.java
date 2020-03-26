package seedu.address.ui.interactiveprompt.add;

/**
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import static seedu.address.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.address.logic.commands.add.AddTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.TimeParser;
import seedu.address.logic.parser.interactivecommandparser.AddTaskCommandParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.model.module.EmptyModule;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskType;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

/**
 * A interactive prompt for adding new task.
 */
public class AddTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Task added successfully!";
    static final String END_OF_COMMAND_DUPLICATE_MSG = "Task will not be added! Key in your next command :)";
    static final String QUIT_COMMAND_MSG = "Successfully quited from add task command.";

    protected Task task;

    public AddTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ADD_TASK;
        this.task = new Task();
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        } else {
            userInput = checkForBackInput(userInput);
        }

        switch (currentTerm) {
        case INIT:
            this.reply = "Please enter the task name.";
            currentTerm = InteractivePromptTerms.TASK_NAME;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            /**
             * TEMPORARY PLACEHOLDER TO ENABLE FILE SAVE.
             * REMOVE task.setAttribute once you've create methods to handle these....
             * By default, Task will go to Module code AA0000. To add to a specific module, use other commands.
             */

            task.setModule(new EmptyModule());
            task.setStatus("pending");

            task.setTaskDescription("No Description Available");
            task.setWeight(0.0);
            task.setEstimatedTimeCost(0);
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
                task.setCreationDateTime(LocalDateTime.now());
                AddTaskCommand addTaskCommand = new AddTaskCommand(task);
                logic.executeCommand(addTaskCommand);
                if (task.isDuplicate()) {
                    reply = "This is a duplicate task. Are you sure you would like to proceed?\n"
                            + "Please enter yes or no.";
                    currentTerm = InteractivePromptTerms.ADD_DUPLICATE;
                } else {
                    endInteract(END_OF_COMMAND_MSG);
                }
            } catch (ParseException | CommandException e) {
                e.printStackTrace();
            }

            break;

        case ADD_DUPLICATE:
            if (userInput.equalsIgnoreCase("yes")) {
                AddTaskCommand addDuplicateTaskCommand = new AddTaskCommand(task);
                try {
                    logic.executeCommand(addDuplicateTaskCommand);
                    endInteract(END_OF_COMMAND_MSG);
                } catch (CommandException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (userInput.equalsIgnoreCase("no")) {
                endInteract(END_OF_COMMAND_DUPLICATE_MSG);
            } else {
                reply = (new AddTaskCommandException("wrongDuplicateFormat")).getErrorMessage();
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
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
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
