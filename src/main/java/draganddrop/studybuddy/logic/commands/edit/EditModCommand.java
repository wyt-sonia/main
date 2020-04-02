package draganddrop.studybuddy.logic.commands.edit;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * Command for editing mod.
 */
public class EditModCommand extends Command {
    static final String MOD_DOES_NOT_EXIST = "Module does not exist in Study Buddy!";
    private Module oldModule;
    private Module newModule;
    private InteractivePromptTerms term;

    public EditModCommand(Module oldModule, Module newModule, InteractivePromptTerms term) {
        requireNonNull(oldModule);
        requireNonNull(newModule);
        this.oldModule = oldModule;
        this.newModule = newModule;
        this.term = term;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasMod(oldModule)) {
            return new CommandResult(MOD_DOES_NOT_EXIST);
        } else {
            try {
                switch (term) {
                case CHANGE_MOD_NAME:
                    model.changeModName(oldModule, newModule);
                    return new CommandResult("Module name Change successful!");
                case CHANGE_MOD_CODE:
                    model.changeModCode(oldModule, newModule);
                    return new CommandResult("Module code change successful!");
                case DELETE_MOD:
                    model.deleteMod(oldModule);
                    return new CommandResult("Module deleted!");
                default:
                    return new CommandResult("Did nothing! from EditModCommand.");
                }
            } catch (ModuleCodeException ex) {
                throw new CommandException(ex.getMessage());
            }
        }
    }
}
