package draganddrop.studdybuddy.model.module.exceptions;

import draganddrop.studdybuddy.logic.parser.exceptions.ParseException;

/**
 * Exception for module code
 */
public class ModuleCodeException extends ParseException {
    public ModuleCodeException(String message) {
        super(message);
    }
}

