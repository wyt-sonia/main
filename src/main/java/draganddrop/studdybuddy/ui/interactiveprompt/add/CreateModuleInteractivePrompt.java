package draganddrop.studdybuddy.ui.interactiveprompt.add;

import java.text.ParseException;

<<<<<<< HEAD:src/main/java/seedu/address/ui/interactiveprompt/add/CreateModuleInteractivePrompt.java
import seedu.address.logic.commands.add.CreateModCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.AddTaskCommandParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.model.module.Module;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;
import seedu.address.ui.interactiveprompt.InteractivePromptType;
=======
import draganddrop.studdybuddy.logic.commands.add.CreateModCommand;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.logic.parser.interactivecommandparser.AddTaskCommandParser;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptTerms;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptType;
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/add/CreateModuleInteractivePrompt.java

/**
 * An interactive prompt for creating modules.
 */
public class CreateModuleInteractivePrompt extends InteractivePrompt {
    static final String QUIT_COMMAND_MSG = "Successfully quited from create mod command.";
    private Module module;

    public CreateModuleInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CREATE_MODULE;
        this.module = new Module();
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
            this.reply = "Please key in the name of the module that you want to create";
            currentTerm = InteractivePromptTerms.MODULE_NAME;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;
        case MODULE_NAME:
            try {
                userInput = AddTaskCommandParser.parseName(userInput);
                this.reply = "The name of module is set to: " + userInput + ".\n"
                        + "Now key in your module code";
                module.setModuleName(userInput);
                currentTerm = InteractivePromptTerms.MODULE_CODE;
                lastTerm = InteractivePromptTerms.MODULE_NAME;
                terms.add(lastTerm);
            } catch (AddTaskCommandException ex) {
                reply = "Please write something as the name of your module.";
            }
            break;
        case MODULE_CODE:
            try {
                module.setModuleCode(userInput);
                this.reply = "Module Code: " + module.toString() + "\n"
                        + "Click 'Enter' again to confirm your changes";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.MODULE_CODE;
                terms.add(lastTerm);
            } catch (ModuleCodeException ex) {
                reply = "Please key in your module code to include a prefix, a number, then a postfix (Optional).\n"
                        + "E.g. A1\n        BT102     \n        CS77777X";
            }
            break;
        case READY_TO_EXECUTE:
            try {
                CreateModCommand createModCommand = new CreateModCommand(module);
                logic.executeCommand(createModCommand);
                reply = "Module created! Key in your next command :)";
                endInteract(reply);
            } catch (ParseException ex) {
                reply = ex.getMessage();
            } catch (CommandException ex) {
                reply = ex.getLocalizedMessage();
            }
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
}
