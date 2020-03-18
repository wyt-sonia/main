package seedu.address.ui.interactiveprompt;

import static seedu.address.ui.interactiveprompt.InteractivePromptType.FIND_TASK;

import java.text.ParseException;
import java.util.ArrayList;
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

    private String userKeyword;
    private String reply;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;

    public FindTaskInteractivePrompt() {
        super();
        this.interactivePromptType = FIND_TASK;
        this.reply = "";
        this.userInput = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        } else if (userInput.equals("back")) {
            if (lastTerm != null) { //in the beginning it is null
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
            this.reply = "Please type in a keyword that you want to search for.\n";
            currentTerm = InteractivePromptTerms.FIND_KEYWORD;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;

        case FIND_KEYWORD:
            userKeyword = userInput;
            reply = "You are searching for the tasks containing " + userKeyword + ". \n "
                    + "Please click enter again to view the searched list.";
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
