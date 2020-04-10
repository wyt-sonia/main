package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.EDIT_MODULE;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.edit.EditModCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleCode;
import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * Edit module.
 */
public class EditModuleInteractivePrompt extends InteractivePrompt {
    public static final int CHANGE_NAME = 1;
    public static final int CHANGE_CODE = 2;
    public static final int DELETE_MODULE = 3;
    public static final String END_OF_COMMAND_MSG = "Module edited successfully!";
    public static final String QUIT_COMMAND_MSG = "Successfully quited from editing module.";
    public static final String REQUIRED_MODULE_MSG = "Please key in a module code from the list.";
    public static final String REQUIRED_MODULE_NAME_MSG = "Please key in the new name of your module";
    public static final String REQUIRED_MODULE_CODE_MSG = "Please key in the new code of your module";
    public static final String REQUIRED_CONFIRMATION = "Are you sure you want to delete this module?\n"
        + "All tasks in this module will be "
        + "relocated to 'No Module Allocated' tab. \n\nPress enter to proceed";

    public static final String CHOICES = "What would you like to do? Key in the index of the action"
        + "that you wish to perform.\n"
        + "1. Change Name\n"
        + "2. Change Module code\n"
        + "3. Delete Module\n";

    private Module oldModule;
    private Module newModule;

    public EditModuleInteractivePrompt() {
        super();
        this.interactivePromptType = EDIT_MODULE;
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }
        switch (currentTerm) {
        case INIT:
            this.reply = REQUIRED_MODULE_MSG;
            currentTerm = InteractivePromptTerms.CHOICE;
            break;
        case CHOICE:
            try {
                if (userInput.equals("")) {
                    throw new ModuleException("emptyInputError");
                } else if (ModuleCode.isModuleCode(userInput)) {
                    oldModule = new Module(userInput);
                    newModule = new Module(userInput);
                    boolean hasModule = logic.getFilteredModuleList().contains(oldModule);
                    if (!hasModule || oldModule.equals(new EmptyModule())) { //Ensure emptyMod is never touched
                        throw new ModuleException("noSuchModuleError");
                    } else {
                        oldModule = logic.getFilteredModuleList().get(logic.getFilteredModuleList().indexOf(oldModule));
                        newModule.setModuleName(oldModule.getModuleName());
                        reply = "Module retrieved: " + newModule.getModuleName()
                            + " " + newModule.getModuleCode() + "\n\n"
                            + CHOICES;
                        currentTerm = InteractivePromptTerms.PICK;
                    }
                } else {
                    throw new ModuleException("wrongModuleCodeFormatError");
                }
            } catch (ModuleException ex) {
                reply = ex.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG;
            }
            break;
        case PICK:
            try {
                int index = Integer.parseInt(userInput);
                switch (index) {
                case CHANGE_NAME:
                    currentTerm = InteractivePromptTerms.CHANGE_MOD_NAME;
                    reply = REQUIRED_MODULE_NAME_MSG;
                    break;
                case CHANGE_CODE:
                    currentTerm = InteractivePromptTerms.CHANGE_MOD_CODE;
                    reply = REQUIRED_MODULE_CODE_MSG;
                    break;
                case DELETE_MODULE:
                    currentTerm = InteractivePromptTerms.DELETE_MOD;
                    reply = REQUIRED_CONFIRMATION;
                    break;
                default:
                    reply = CHOICES;
                }
            } catch (NumberFormatException ex) {
                reply = CHOICES;
            }
            break;
        case CHANGE_MOD_NAME:
            try {
                if (userInput.equals("")) {
                    throw new ModuleException("emptyInputError");
                } else if (userInput.equalsIgnoreCase(oldModule.getModuleName())) {
                    throw new ModuleException("noChangeFromOriginalModuleNameError");
                } else if (!logic.getFilteredModuleList()
                    .filtered(x -> x.getModuleName().equalsIgnoreCase(userInput)).isEmpty()) {
                    throw new ModuleException("duplicateModuleNameError");
                } else {
                    newModule.setModuleName(userInput);
                    logic.executeCommand(new EditModCommand(oldModule, newModule,
                        InteractivePromptTerms.CHANGE_MOD_NAME));
                    reply = "Your module " + oldModule.getModuleCode().toString() + " is now renamed "
                        + newModule.getModuleName();
                    endInteract(END_OF_COMMAND_MSG + "\n\n" + reply);
                }
            } catch (ModuleException ex) {
                reply = ex.getErrorMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            } catch (CommandException | ParseException ex) {
                reply = ex.getMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            }
            break;
        case CHANGE_MOD_CODE:
            try {
                if (userInput.equals("")) {
                    throw new ModuleException("emptyInputError");
                } else if (userInput.toUpperCase().equals(oldModule.getModuleCode().toString().toUpperCase())) {
                    throw new ModuleException("noChangeFromOriginalModuleCodeError");
                } else if (!logic.getFilteredModuleList().filtered(x -> x.toString()
                    .equalsIgnoreCase(userInput)).isEmpty()) {
                    throw new ModuleException("duplicateModuleCodeError");
                } else {
                    newModule.setModuleCode(userInput);
                    logic.executeCommand(new EditModCommand(oldModule, newModule,
                        InteractivePromptTerms.CHANGE_MOD_CODE));
                    reply = "Your module " + oldModule.getModuleCode().toString() + " is now coded "
                        + newModule.getModuleCode().toString();
                    endInteract(END_OF_COMMAND_MSG + "\n\n" + reply);
                }
            } catch (ModuleException ex) {
                reply = ex.getErrorMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            } catch (CommandException | ParseException ex) {
                reply = ex.getMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            }
            break;
        case DELETE_MOD:
            try {
                logic.executeCommand(new EditModCommand(oldModule, newModule, InteractivePromptTerms.DELETE_MOD));
                reply = "Module deleted successfully!";
                endInteract(reply);
            } catch (ModuleException ex) {
                reply = ex.getErrorMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            } catch (CommandException | ParseException ex) {
                reply = ex.getMessage() + "\n\n" + REQUIRED_MODULE_NAME_MSG;
            }
            break;
        default:
        }
        return reply;
    }
}
