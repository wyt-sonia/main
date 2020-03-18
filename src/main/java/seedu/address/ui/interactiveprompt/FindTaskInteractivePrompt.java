package seedu.address.ui.interactiveprompt;

import static seedu.address.ui.interactiveprompt.InteractivePromptType.FIND_TASK;

import java.text.ParseException;
import java.util.Arrays;

import seedu.address.logic.commands.FindTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

/**
 * A interactive prompt for finding tasks in the list.
 */
public class FindTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Here are the list of tasks matching the keyword:";
    private static final String QUIT_COMMAND_MSG = "Successfully quited from find task command.";
    private static final String KEYWORD_PROMPT = "Please type in a keyword that you want to search for.\n";
    private String userKeyword;

    public FindTaskInteractivePrompt() {
        super();
        this.interactivePromptType = FIND_TASK;
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit") || userInput.equals("back")) {
            reply = handleQuitAndBack(userInput, QUIT_COMMAND_MSG);
        } else {
            reply = handleFind(userInput);
        }
        return reply;
    }

    /**
     * Handles the sequence of interactive find commands.
     * @param userInput the input entered by the user
     * @return the reply to the user
     */
    public String handleFind(String userInput) {
        switch (currentTerm) {
        case INIT:
            this.reply = KEYWORD_PROMPT;
            currentTerm = InteractivePromptTerms.FIND_KEYWORD;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;
        case FIND_KEYWORD:
            userKeyword = userInput;
            reply = getConfirmationPrompt(userKeyword);
            currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            lastTerm = InteractivePromptTerms.FIND_KEYWORD;
            terms.add(lastTerm);
            break;

        case READY_TO_EXECUTE:
            try {
                String trimmedArgs = userKeyword.trim();
                if (trimmedArgs.isEmpty()) {
                    throw new ParseException("Keyword cannot be empty string", 0);
                }
                String[] userKeywords = trimmedArgs.split("\\s+");
                TaskNameContainsKeywordsPredicate pred =
                        new TaskNameContainsKeywordsPredicate(Arrays.asList(userKeywords));
                FindTaskCommand findTaskCommand = new FindTaskCommand(pred);

                logic.executeCommand(findTaskCommand);
                endInteract(END_OF_COMMAND_MSG);
            } catch (CommandException | ParseException ex) {
                reply = ex.getMessage();
            }
            break;
        default:
            break;
        }
        return reply;
    }

    public String getConfirmationPrompt(String userKeyword) {
        return "You are searching for the tasks containing " + userKeyword + ". \n "
                + "Please click enter again to view the searched list.";
    }
    public String getKeywordPrompt() {
        return KEYWORD_PROMPT;
    }

    public String getQuitMessage() {
        return QUIT_COMMAND_MSG;
    }

    @Override
    public void interruptInteract() {
        // empty
    }

    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }

    @Override
    public void back() {
        // empty
    }

    @Override
    public void next() {
        // empty
    }

}
