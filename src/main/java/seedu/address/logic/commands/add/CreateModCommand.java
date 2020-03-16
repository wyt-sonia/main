package seedu.address.logic.commands.add;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;


public class CreateModCommand extends Command{
    public static final String COMMAND_WORD = "create mod";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new mod. "
            + "Parameters: Name & Code";

    public static final String MESSAGE_SUCCESS = "New Mod added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This module already exists in the address book";

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
