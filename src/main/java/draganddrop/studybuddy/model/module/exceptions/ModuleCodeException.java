package draganddrop.studybuddy.model.module.exceptions;

import draganddrop.studybuddy.logic.parser.exceptions.ParseException;

/**
 * Exception for module code
 */
public class ModuleCodeException extends ParseException {
    public ModuleCodeException(String message) {
        super(message);
    }
}

