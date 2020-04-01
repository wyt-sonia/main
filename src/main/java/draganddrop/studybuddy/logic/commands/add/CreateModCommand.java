package draganddrop.studybuddy.logic.commands.add;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.module.Module;

/**
 * A command to create modules.
 */
public class CreateModCommand extends Command {
    public static final String COMMAND_WORD = "create mod";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new mod. "
        + "Parameters: Name & Code";

    public static final String MESSAGE_SUCCESS = "New Mod added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This module already exists in the study buddy";

    private final Module mod;

    public CreateModCommand(Module mod) {
        requireNonNull(mod);
        this.mod = mod;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasMod(mod)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        model.addMod(mod);
        return new CommandResult(String.format(MESSAGE_SUCCESS, mod));
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof CreateModCommand // instanceof handles nulls
            && mod.equals(((CreateModCommand) other).mod));
    }
}
