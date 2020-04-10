package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents an InteractiveCommandException.
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

        case "moduleWeightOverloadError":
            errorMessage = "The maximum sum of task's weights under the same module is 100.0, please check.";
            break;

        case "taskCompletedError":
            errorMessage = "The task you chose is completed already";
            break;

        case "specialCharInputError":
            errorMessage = "The task name contains special character, please check.";
            break;

        case "taskNameLengthError":
            errorMessage = "The maximum length of task name is 20 characters, please check.";
            break;

        case "taskDescriptionLengthError":
            errorMessage = "The maximum length of task description is 300 characters, please check.";
            break;

        case "wrongWeightFormatError":
            errorMessage = "Please enter decimal number for task task weight.";
            break;

        case "wrongWeightRangeError":
            errorMessage = "The weight should be from 0.0 to 100.0.";
            break;

        case "wrongEstimatedTimeFormatError":
            errorMessage = "Please enter decimal number for the number of hours the task might take.";
            break;

        case "wrongEstimatedTimeRangeError":
            errorMessage = "Please enter a positive number for the number of hours the task might take.";
            break;

        case "noSuchModuleError":
            errorMessage = "Could not find the module based on the module code entered, please check and re-enter.";
            break;

        case "duplicateModuleNameError":
            errorMessage = "Detected Duplicate Module name. Please key in another module name.";
            break;

        case "duplicateModuleCodeError":
            errorMessage = "Detected Duplicate Module code. Please key in another module code.";
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
            errorMessage = "Invalid date time format, please follow the format below:\n\n"
                + "Assignment : HH:mm dd/MM/yyyy   e.g. 12:00 01/01/2020\n\n"
                + "Rest: HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy\n"
                + "e.g. 12:00 01/01/2020-14:00 01/01/2020\n";
            break;

        case "pastDateTime":
            errorMessage = "Invalid date time, please enter a time in the future.";
            break;

        case "eventEndBeforeStartError":
            errorMessage = "Invalid date time, the end date you entered is before the start date, please check.";
            break;

        case "dateFormatError":
            errorMessage = "Invalid date format, please follow the format below:\n\n"
                            + "dd/MM/yyyy";
            break;

        case "tooLongAway":
            errorMessage = "Date too far apart from current date, please input a more realistic date";
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
