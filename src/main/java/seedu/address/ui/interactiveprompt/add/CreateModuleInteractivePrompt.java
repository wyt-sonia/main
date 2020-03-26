package seedu.address.ui.interactiveprompt.add;

import java.text.ParseException;
import java.util.ArrayList;

import seedu.address.logic.commands.add.CreateModCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.AddTaskCommandParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.model.module.Module;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;
import seedu.address.ui.interactiveprompt.InteractivePromptType;

/**
 * An interactive prompt for creating modules.
 */
public class CreateModuleInteractivePrompt extends InteractivePrompt {
    private Module module;
    static final String QUIT_COMMAND_MSG = "Successfully quited from  create mod command.";

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
        System.out.println("interact(" + userInput +")" + "\ncurrentTerm(" + currentTerm + ")" );
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        } else if (userInput.equals("back")) {
            if (lastTerm != null) {
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
                System.out.println("INIT " + userInput);
                this.reply = "Please key in the name of the module that you want to create";
                currentTerm = InteractivePromptTerms.MODULE_NAME;
                lastTerm = InteractivePromptTerms.INIT;
                terms.add(lastTerm);
                break;
            case MODULE_NAME:
                System.out.println("MODULE_NAME " + userInput);
                try {
                    userInput = AddTaskCommandParser.parseName(userInput);
                    this.reply = "The name of module is set to: " + userInput + "\n"
                            + "Now key in your module code";
                    module.setModuleName(userInput);
                    currentTerm = InteractivePromptTerms.MODULE_CODE;
                    lastTerm = InteractivePromptTerms.MODULE_NAME;
                    terms.add(lastTerm);
                } catch (AddTaskCommandException e) {
                    reply = "Please add something as your module name.";
                }
                break;
            case MODULE_CODE:
                System.out.println("CODE " + userInput);
                try {
                    module.setModuleCode(userInput);
                    this.reply = "Module Code: " + module.toString() + "\n"
                            + "Click 'Enter' again to confirm your changes";
                    currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                    lastTerm = InteractivePromptTerms.MODULE_CODE;
                    terms.add(lastTerm);
                } catch (ModuleCodeException e) {
                    System.out.print("code fail\n");
                    reply = "Please type your module code with a prefix, some numbers then postfix (optional) in the format A00X" + "\n"
                            + "E.g. A1234\n     B29\n     CES100000X";
                }
                break;

            case READY_TO_EXECUTE:
                System.out.println("READY_TO_EXECUTE " + userInput);
                try {
                    CreateModCommand createModCommand = new CreateModCommand(module);
                    logic.executeCommand(createModCommand);
                    reply = "Module created! Key in your next command :)";
                    endInteract(reply);
                } catch (CommandException | ParseException | DuplicateTaskException ex) {
                    reply = ex.getMessage();
                    currentTerm = InteractivePromptTerms.MODULE_CODE;
                    lastTerm = InteractivePromptTerms.MODULE_NAME;
                    terms.add(lastTerm);
                }
                break;
            default:
                System.out.println("Nothing");
                break;
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
