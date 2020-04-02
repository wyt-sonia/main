package draganddrop.studybuddy.ui.interactiveprompt.add;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.add.CreateModCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.AddTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType;

/**
 * An interactive prompt for creating modules.
 */
public class CreateModuleInteractivePrompt extends InteractivePrompt {
    final public static String QUIT_COMMAND_MSG = "Successfully quited from create mod command.";
    private Module module;

    public CreateModuleInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CREATE_MODULE;
        this.module = new Module();
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {

        case INIT:
            this.reply = "Please key in the name of the module that you want to create";
            currentTerm = InteractivePromptTerms.MODULE_NAME;
            break;
        case MODULE_NAME:
            try {
                userInput = AddTaskCommandParser.parseName(userInput);
                this.reply = "The name of module is set to: " + userInput + ".\n"
                    + "Now key in your module code";
                module.setModuleName(userInput);
                currentTerm = InteractivePromptTerms.MODULE_CODE;
            } catch (InteractiveCommandException ex) {
                reply = "Please write something as the name of your module.";
            }
            break;
        case MODULE_CODE:
            try {
                module.setModuleCode(userInput);
                this.reply = "Module Code: " + module.toString() + "\n"
                    + "Click 'Enter' again to confirm your changes";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (ModuleCodeException ex) {
                reply = "Please key in your module code to include a 2-3 letter prefix, a 4-digit number,"
                        + "then a postfix (Optional).\n"
                        + "E.g. BA10001\n        CS2030     \n        CS2040C";
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
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }
}
