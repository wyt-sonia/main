package draganddrop.studybuddy.ui.interactiveprompt.add;

/**
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import draganddrop.studybuddy.logic.commands.add.AddTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.AddTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import javafx.collections.ObservableList;

/**
 * A interactive prompt for adding new task.
 */
public class AddTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Task added successfully!";
    static final String END_OF_COMMAND_DUPLICATE_MSG = "Task will not be added! Key in your next command :)";
    static final String QUIT_COMMAND_MSG = "Successfully quited from add task command.";
    String moduleListString = "";
    protected Task task;
    protected boolean isModuleTried = false;
    protected ObservableList<Module> modules;

    public AddTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ADD_TASK;
        this.task = new Task();
        this.modules = null;
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = "Please choose a Module for this task or press enter to skip.\n"
                + "Index number and module code are both acceptable.\n"
                + "The Modules available are: \n";
            this.modules = logic.getFilteredModuleList();
            AtomicInteger counter = new AtomicInteger();
            modules.forEach(m -> {
                counter.getAndAdd(1);
                reply += counter + "." + m.getModuleCode() + " " + m.getModuleName() + "\n";
            });
            moduleListString = this.reply;
            currentTerm = InteractivePromptTerms.TASK_MODULE;
            /**
             * TEMPORARY PLACEHOLDER TO ENABLE FILE SAVE.
             * REMOVE task.setAttribute once you've create methods to handle these....
             * By default, Task will go to Module code AA0000. To add to a specific module, use other commands.
             */
            task.setStatus("pending");
            task.setTaskDescription("No Description");
            task.setWeight(0.0);
            task.setEstimatedTimeCost(0);
            break;

        case TASK_MODULE:
            try {
                Module module = null;
                if (userInput.isBlank() && !isModuleTried) {
                    module = new EmptyModule();
                } else if (userInput.isBlank() && isModuleTried) {
                    throw new AddTaskCommandException("emptyInputError");
                } else {
                    isModuleTried = true;
                    module = AddTaskCommandParser.parseModule(userInput, modules);
                }
                task.setModule(module);
                currentTerm = InteractivePromptTerms.TASK_NAME;
                this.reply = "The module has been set as: " + module.getModuleCode() + " "
                    + module.getModuleName() + "\n\n"
                    + "Please enter the task name.";
            } catch (AddTaskCommandException e) {
                isModuleTried = false;
                reply = e.getErrorMessage() + "\n\n" + moduleListString;
            }
            break;

        case TASK_NAME:
            try {
                userInput = AddTaskCommandParser.parseName(userInput);
                this.reply = "The name of task is set to: " + userInput + ".\n"
                    + "Please choose the task type:\n"
                    + TaskType.getTypeString();
                task.setTaskName(userInput);
                currentTerm = InteractivePromptTerms.TASK_TYPE;
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage() + "\n\n"
                    + "Please enter the task name.";
            }
            break;

        case TASK_TYPE:
            try {
                TaskType taskType = AddTaskCommandParser.parseType(userInput, TaskType.getTaskTypes().length);
                task.setTaskType(taskType);

                this.reply = "The type of task has been set to: " + taskType.toString() + ".\n"
                    + "Please enter the deadline with format: ";
                if (taskType.equals(TaskType.Assignment)) {
                    this.reply += "HH:mm dd/MM/yyyy";
                } else {
                    this.reply += "HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy";
                }

                currentTerm = InteractivePromptTerms.TASK_DATETIME;
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

                reply = "The date and time is set to: " + userInput + "\n\n"
                    + "Press enter again to add the task:\n\n"
                    + "=========== TASK INFO ===========\n"
                    + task.toString();

                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
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
