package seedu.address.ui.interactiveprompt.add;

import java.text.ParseException;
import java.util.ArrayList;

import seedu.address.logic.commands.add.CreateModCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.AddTaskCommandParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.model.module.Module;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;
import seedu.address.ui.interactiveprompt.InteractivePromptType;

/**
 * An interactive prompt for creating modules.
 */
public class CreateModuleInteractivePrompt extends InteractivePrompt {
    private Module module;

    public CreateModuleInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CREATE_MODULE;
        this.reply = "";
        this.userInput = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
        this.module = new Module();
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
                    reply = "You have reach the start of the command.\n"
                        + "Either continue, or type 'quit' the exit." + reply;
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
            } catch (CommandException | ParseException ex) {
                reply = ex.getMessage();
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
