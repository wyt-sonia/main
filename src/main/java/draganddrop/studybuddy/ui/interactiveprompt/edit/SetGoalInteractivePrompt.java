package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.SET_GOAL;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.statistics.Statistics;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * handles the interaction for goal setting
 */
public class SetGoalInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quit from the set goal command";
    private static final String END_OF_COMMAND_MSG = "Great! Your goal has been set successfully!";
    private static Statistics statistics;

    public SetGoalInteractivePrompt() {
        super();
        this.interactivePromptType = SET_GOAL;
    }

    @Override
    public String interact(String userInput) {
        if (isQuit(userInput)) {
            this.reply = handleQuit(userInput, QUIT_COMMAND_MSG);
        } else {
            this.reply = handleSetGoal(userInput);
        }
        return this.reply;
    }

    public String getEndOfCommandMsg() {
        return END_OF_COMMAND_MSG;
    }

    public static void setStatistics(Statistics statistics) {
        SetGoalInteractivePrompt.statistics = statistics;
    }

    /**
     * handles goal setting for user
     * @param userInput user's input
     * @return a reply for the user
     */
    public String handleSetGoal(String userInput) {
        switch (currentTerm) {
        case INIT:
            reply = "How many tasks do you want to complete today? Please give me a number between 1 to 100";
            currentTerm = InteractivePromptTerms.GOAL;
            break;
        case GOAL:
            this.reply = parseGoalValue(userInput);
            break;
        default:
            break;
        }
        return this.reply;
    }

    /**
     * handles the parsing of goal value
     * @param userInput user's input for goal
     * @return goal value
     */
    public String parseGoalValue(String userInput) {
        int goal = 5;
        try {
            if (userInput.isBlank()) {
                throw new AddOrEditTaskCommandException("emptyInputError");
            }
            goal = Integer.parseInt(userInput.trim());
            if (goal > 100 || goal < 1) {
                throw new AddOrEditTaskCommandException("invalidIndexRangeError");
            }
        } catch (NumberFormatException e) {
            return (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage();
        } catch (AddOrEditTaskCommandException ex) {
            return ex.getErrorMessage();
        }
        // we reach here if parsing is successful
        statistics.setGoal(goal);
        endInteract(END_OF_COMMAND_MSG);
        return END_OF_COMMAND_MSG;
    }

}
