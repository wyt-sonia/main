package seedu.address.ui.interactiveprompt.edit;

import static seedu.address.ui.interactiveprompt.InteractivePromptType.EDIT_TASK;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskField;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

/**
 * Interactive prompt for editing tasks
 */
public class EditTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Task edited successfully";
    private static final String QUIT_COMMAND_MSG = "Successfully quit from the edit task command";
    private int taskNumber;
    private TaskField taskField;
    private String newValue;

    public EditTaskInteractivePrompt() {
        super();
        this.interactivePromptType = EDIT_TASK;
    }

    @Override
    public String interact(String userInput) {
        if (isQuitOrBack(userInput)) {
            this.reply = handleQuitAndBack(userInput, QUIT_COMMAND_MSG);
        } else {
            this.reply = handleEdit(userInput);
        }
        return this.reply;
    }

    /**
     * handles the sequence of commands for edit
     * @param userInput input from user
     * @return reply to user
     */
    public String handleEdit(String userInput) {
        switch (currentTerm) {
        case INIT:
            this.reply = "Please enter the index of the task that you wish to edit.";
            this.currentTerm = InteractivePromptTerms.TASK_INDEX;
            break;
        case TASK_NUMBER:
            this.taskNumber = parseTaskNumber(userInput);
            break;
        case TASK_FIELD:
            this.taskField = parseTaskFieldNumber(userInput);
            break;
        case NEW_VALUE:
            this.reply = ""; // depends on success or failure.
            this.newValue = userInput;
            break;
        default:
            break;
        }
        return "";
    }

    /**
     * parses task number
     * @param userInput user input for task number
     * @return an int of task number
     */
    public int parseTaskNumber(String userInput) {
        boolean isParseSuccessful = true;
        int taskNum = -1;

        try {
            taskNum = Integer.parseInt(userInput);
            if (taskNum > Task.getCurrentTasks().size() || taskNum < 0) {
                throw new ParseException("task number not in range");
            }
        } catch (NumberFormatException | ParseException ex) {
            isParseSuccessful = false;
        }

        if (isParseSuccessful) {
            this.reply = "Please choose the field that you wish to edit in task number " + taskNumber + ".";
            this.currentTerm = InteractivePromptTerms.TASK_FIELD;
        } else {
            // prompt for a new value
            this.reply = "Please choose a valid task number.";
            this.currentTerm = InteractivePromptTerms.TASK_INDEX;
        }
        return taskNum;
    }

    /**
     * parses the task field
     * @param userInput userInput for task number
     * @return a TaskField
     */
    public TaskField parseTaskFieldNumber(String userInput) {
        boolean isParseSuccessful = true;
        TaskField taskField = null;

        try {
            int taskFieldNumber = Integer.parseInt(userInput);
            if (taskFieldNumber > 3 || taskFieldNumber < 1) {
                throw new ParseException("task field number not in range");
            }
            taskField = TaskField.getTaskFieldFromNumber(taskFieldNumber);
        } catch (NumberFormatException | ParseException ex) {
            isParseSuccessful = false;
        }

        if (isParseSuccessful) {
            assert(taskField != null);
            this.reply = "Please enter the new value for the " + taskField.getLabel() + "field";
            this.currentTerm = InteractivePromptTerms.NEW_VALUE;
        } else {
            // prompt for a new value
            this.reply = "Please choose a valid task field index";
            this.currentTerm = InteractivePromptTerms.TASK_FIELD;
        }
        return taskField;
    }
}
