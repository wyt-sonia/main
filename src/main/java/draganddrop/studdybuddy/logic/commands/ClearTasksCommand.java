<<<<<<< HEAD:src/main/java/draganddrop/studdybuddy/logic/commands/delete/ClearTasksCommand.java
package seedu.address.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
=======
package draganddrop.studdybuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.StudyBuddy;
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/logic/commands/ClearTasksCommand.java

/**
 * Clears the address book.
 */
public class ClearTasksCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Study Buddy has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setStudyBuddy(new StudyBuddy());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
