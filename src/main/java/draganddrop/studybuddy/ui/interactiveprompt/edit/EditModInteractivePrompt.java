package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.EDIT_MODULE;
import static draganddrop.studybuddy.ui.interactiveprompt.add.CreateModuleInteractivePrompt.MODULE_CODE_FORMAT;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.edit.EditModCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * Edit module.
 */
public class EditModInteractivePrompt extends InteractivePrompt {
    static final int CHANGE_NAME = 1;
    static final int CHANGE_CODE = 2;
    static final int DELETE_MODULE = 3;
    static final String END_OF_COMMAND_MSG = "Module edited successfully!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from editing module.";
    static final String REQUIRED_MODULE_MSG = "Please key in a module code from the list.\n";
    static final String CHOICES = "What would you like to do? Key in the index of the action"
            + "that you wish to perform.\n"
            + "1. Change Name\n"
            + "2. Change Module code\n"
            + "3. Delete Module\n";

    private Module oldModule;
    private Module newModule;

    public EditModInteractivePrompt() {
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
                oldModule = new Module(userInput);
                newModule = new Module(userInput);
                boolean hasModule = logic.getFilteredModuleList().contains(oldModule);
                if (!hasModule || oldModule.equals(new EmptyModule())) { //Ensure emptyMod is never touched
                    reply = "Module does not exist in Study Buddy! Key in another module, or click 'Modules/"
                            + "Create' in the menu bar.";
                } else {
                    oldModule = logic.getFilteredModuleList().get(logic.getFilteredModuleList().indexOf(oldModule));
                    newModule.setModuleName(oldModule.getModuleName());
                    reply = "Module retrieved\n" + newModule.getModuleName() + ": " + newModule.getModuleCode() + "\n"
                            + CHOICES;
                    currentTerm = InteractivePromptTerms.PICK;
                }
            } catch (ModuleCodeException ex) {
                reply = "Invalid module code. Please key in module in the correct format.";
            }
            break;
        case PICK:
            try {
                int index = Integer.parseInt(userInput);
                switch (index) {
                case CHANGE_NAME:
                    currentTerm = InteractivePromptTerms.CHANGE_MOD_NAME;
                    reply = "Please key in the new name of your module";
                    break;
                case CHANGE_CODE:
                    currentTerm = InteractivePromptTerms.CHANGE_MOD_CODE;
                    reply = "Please key in the new code of your module";
                    break;
                case DELETE_MODULE:
                    currentTerm = InteractivePromptTerms.DELETE_MOD;
                    reply = "Are you sure you want to delete this module? All task in this module will be "
                            + "relocated to 'No Module Allocated' tab. Press enter to proceed";
                    break;
                default:
                    reply = CHOICES;
                }
            } catch (NumberFormatException ex) {
                reply = CHOICES;
            }
            break;
        case CHANGE_MOD_NAME:
            if (userInput.equals("")) {
                reply = "Please key in something as your module name";
            } else {
                try {
                    newModule.setModuleName(userInput);
                    logic.executeCommand(new EditModCommand(oldModule, newModule,
                            InteractivePromptTerms.CHANGE_MOD_NAME));
                    reply = "Your module " + oldModule.getModuleCode().toString() + " is now renamed "
                            + newModule.getModuleName();
                    endInteract(END_OF_COMMAND_MSG + reply);
                } catch (CommandException | ParseException ex) {
                    reply = ex.getMessage();
                }
            }
            break;
        case CHANGE_MOD_CODE:
            if (userInput.equals("")) {
                reply = MODULE_CODE_FORMAT;
            } else {
                try {
                    newModule.setModuleCode(userInput);
                    logic.executeCommand(new EditModCommand(oldModule, newModule,
                            InteractivePromptTerms.CHANGE_MOD_CODE));
                    reply = "Your module " + oldModule.getModuleCode().toString() + " is now coded "
                            + newModule.getModuleCode().toString();
                    endInteract(END_OF_COMMAND_MSG + reply);
                } catch (ModuleCodeException | CommandException | ParseException ex) {
                    reply = ex.getMessage();
                }
            }
            break;
        case DELETE_MOD:
            try {
                logic.executeCommand(new EditModCommand(oldModule, newModule, InteractivePromptTerms.DELETE_MOD));
                reply = "Module deleted successfully!";
                endInteract(reply);
            } catch (CommandException | ParseException ex) {
                reply = "Module to be deleted does not exist!";
            }
            break;
        default:
        }

        return reply;
    }
}
