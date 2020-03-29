package draganddrop.studybuddy.model.module;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;

/**
 * ModuleCode. Ensures that the module code keyed in matches the proper format. XX0000.
 */
public class ModuleCode {
    private String prefix;
    private int number;
    private String postfix;

    public ModuleCode(String fullModuleCode) throws ModuleCodeException {
        try {
            this.prefix = parsePrefix(fullModuleCode);
            this.number = parseNumber(fullModuleCode);
            this.postfix = parsePostfix(fullModuleCode);
        } catch (ModuleCodeException e) {
            throw new ModuleCodeException("ModuleCode is invalid!");
        }
    }

    /**
     * Checks whether the {@code userInput} is module code.
     *
     * @param userInput
     * @return
     */
    public static boolean isModuleCode(String userInput) {
        boolean result = true;
        try {
            parsePrefix(userInput);
            parseNumber(userInput);
            parsePostfix(userInput);
        } catch (ModuleCodeException e) {
            result = false;
        }
        return result;
    }

    /**
     * method to parse prefix from the input to ensure correctness of moduleCode.
     *
     * @param input
     * @return
     * @throws ModuleCodeException
     */
    private static String parsePrefix(String input) throws ModuleCodeException {
        String output = "";
        if (!Character.isLetter(input.charAt(0))) {
            throw new ModuleCodeException("Module prefix not found.");
        } else {
            int i = 0;
            while (Character.isLetter(input.charAt(i))) {
                output += input.charAt(i);
                i++;
            }
        }
        return output.toUpperCase();
    }

    /**
     * method to parse number from the input to ensure correctness of moduleCode.
     *
     * @param input
     * @return
     * @throws ModuleCodeException
     */
    private static int parseNumber(String input) throws ModuleCodeException {
        String outputAsString = "";
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            try {
                Integer.parseInt("" + character);
                outputAsString += character;
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        if (outputAsString.equals("")) {
            throw new ModuleCodeException("Module number not found!");
        } else {
            int output = Integer.parseInt(outputAsString);
            return output;
        }
    }

    /**
     * method to parse postfix from the input to ensure correctness of moduleCode.
     *
     * @param input
     * @return
     */
    private static String parsePostfix(String input) {
        String output = "";
        char character = input.charAt(input.length() - 1);
        try {
            Integer.parseInt("" + character);
        } catch (NumberFormatException e) {
            output += character;
        }
        return output.toUpperCase();
    }

    private String getPrefix() {
        return prefix;
    }

    private int getNumber() {
        return number;
    }

    private String getPostfix() {
        return postfix;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ModuleCode) {
            if (this.getPrefix().equals(((ModuleCode) object).prefix)
                && (this.getNumber() == ((ModuleCode) object).number)
                && this.getPostfix().equals(((ModuleCode) object).postfix)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return prefix + String.format("%04d", number) + postfix;
    }

}
