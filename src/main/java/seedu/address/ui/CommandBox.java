package seedu.address.ui;

import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.interactiveprompt.AddTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.ArchiveTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.CompleteTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.DeleteDuplicateTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.DeleteTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.ExitTaskInteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.SortTaskInteractivePrompt;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final String[] interactiveCommandTypes =
        {"add", "edit", "delete", "archive", "done", "delete duplicates", "bye", "sort"};
    private InteractivePrompt currentInteractivePrompt;
    private final CommandExecutor commandExecutor;
    @FXML
    private TextField commandTextField;

    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        currentInteractivePrompt = null;
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        try {
            // allow InteractivePrompt type with different case and space in front or behind
            String userInput = commandTextField.getText().toLowerCase().trim();
            if (currentInteractivePrompt == null) {
                boolean isValidType = Arrays.stream(interactiveCommandTypes)
                    .filter(x -> x.equals(userInput)).count() > 0;
                if (!isValidType) {
                    commandTextField.setText("Please enter valid command type.");
                } else {
                    switch (userInput) {
                    case "add":
                        currentInteractivePrompt = new AddTaskInteractivePrompt();
                        break;
                    case "edit":
                        //currentInteractivePrompt = new EditTaskInteractivePrompt();
                        break;
                    case "delete":
                        currentInteractivePrompt = new DeleteTaskInteractivePrompt();
                        break;
                    case "archive":
                        currentInteractivePrompt = new ArchiveTaskInteractivePrompt();
                        break;
                    case "delete duplicates":
                        currentInteractivePrompt = new DeleteDuplicateTaskInteractivePrompt();
                        break;
                    case "done":
                        currentInteractivePrompt = new CompleteTaskInteractivePrompt();
                        break;
                    case "sort":
                        currentInteractivePrompt = new SortTaskInteractivePrompt();
                        break;
                    case "bye":
                        currentInteractivePrompt = new ExitTaskInteractivePrompt();
                        break;

                    default:
                    }
                }
            }

            //currentInteractivePrompt could be null. Might need to create an ErrorInteractivePrompt to handle this.
            //inserted NullPointerException e at the catch section
            CommandResult commandResult = commandExecutor.execute(currentInteractivePrompt, commandTextField.getText());
            if (commandResult != null) {
                currentInteractivePrompt = null;
            }
            commandTextField.setText("");
        } catch (CommandException | ParseException | NullPointerException e) {
            commandTextField.setText("");
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(InteractivePrompt currentInteractivePrompt, String commandText)
            throws CommandException, ParseException;
    }
}
