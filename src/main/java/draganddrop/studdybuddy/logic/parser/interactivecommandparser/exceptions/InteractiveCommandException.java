package draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * pending.
 */
public class InteractiveCommandException extends RuntimeException {
    protected String errorType;
    protected String errorMessage;

    public InteractiveCommandException(String errorType) {
        this.errorType = errorType;

        switch (errorType) {
        case "emptyInputError":
            errorMessage = "The input is empty, please check again.";
            break;

        case "invalidIndexRangeError":
            errorMessage = "The index entered is out of range, please check again.";
            break;

        case "invalidOptionRangeError":
            errorMessage = "The option index entered is out of range, please check again.";
            break;

        case "wrongIndexFormatError":
            errorMessage = "The format of index entered is invalid, please enter an integer.";
            break;

        case "wrongOptionFormatError":
            errorMessage = "The format of option index entered is invalid, please enter an integer.";
            break;

        case "dataTimeFormatError":
            errorMessage = "Invalid date time format, please follow the format below:\n  "
                + "Event: HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy\n"
                + "e.g. 12:00 01/01/2020-14:00 01/01/2020\n"
                + "Rest : HH:mm dd/MM/yyyy   e.g. 12:00 01/01/2020\n";
            break;

        case "pastDateTime":
            errorMessage = "Invalid date time, please enter a time in the future.";
            break;

        case "eventEndBeforeStartError":
            errorMessage = "Invalid date time, the end date you entered is before the start date, please check.";
            break;
        default:
        }
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }
}
