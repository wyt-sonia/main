package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Archives an entry in the address book.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives the selected entry";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException("Archive not implemented yet");
    }
}
