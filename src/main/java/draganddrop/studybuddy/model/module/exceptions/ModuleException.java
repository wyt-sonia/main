package draganddrop.studybuddy.model.module.exceptions;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;

/**
 * Exception for module code
 */
public class ModuleException extends InteractiveCommandException {
    public ModuleException(String message) {
        super(message);
    }
}

