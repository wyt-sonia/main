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
        if (fullModuleCode.length() > 8) {
            throw new ModuleCodeException("The module code should be at most 8 digits, please check.");
        }
        this.prefix = parsePrefix(fullModuleCode);
        this.number = parseNumber(fullModuleCode);
        this.postfix = parsePostfix(fullModuleCode);
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
     * Parses prefix from the input. Also, Ensure at least the first 2 digits are alphabetical.
     *
     * @param input
     * @return
     * @throws ModuleCodeException
     */
    private static String parsePrefix(String input) throws ModuleCodeException {
        String output = "";
        /*
        if (!Character.isLetter(input.charAt(0))) {
            throw new ModuleCodeException("Module code prefix not found.");
        } else {
            for (int i = 0; i < 3; i++) {
                if (i != 2 && !Character.isLetter(input.charAt(i))) {
                    throw new ModuleCodeException("Wrong module code prefix format error.");
                }
                output += Character.isLetter(input.charAt(i)) ? input.charAt(i) : "";
            }
        }
        */
        for (int i = 0; i < input.length(); i++) {
            if(Character.isAlphabetic(input.charAt(i))) {
                output += input.charAt(i);
            } else {
                break;
            }
        }

        if(output.length() < 2 || output.length() > 3) {
            throw new ModuleCodeException("Wrong module code prefix. Either too short/too long");
        } else {
            return output.toUpperCase();
        }
    }

    /**
     * Parse the number part of the module code and make sure the number part is 4 digits.
     *
     * @param input
     * @return
     * @throws ModuleCodeException
     */
    private static int parseNumber(String input) throws ModuleCodeException {
        int result = 0;
        try {
            int startIndex = Character.isDigit(input.charAt(2)) ? 2 : 1;
            if (input.length() - startIndex > 5 || Character.isDigit(startIndex + 5)) {
                throw new ModuleCodeException("Please enter a valid module code.");
            }
            try {
                result = Integer.parseInt(input.substring(startIndex, startIndex + 4));
            } catch (NumberFormatException e) {
                throw new ModuleCodeException("Please enter a valid module code.");
            }
        } catch (StringIndexOutOfBoundsException ex) {
            throw new ModuleCodeException("Please enter a valid module code. either number is too long/too short.");
        }
        return result;
    }

    /**
     * Parses postfix from the input to ensure correctness of moduleCode.
     *
     * @param input
     * @return
     */
    private static String parsePostfix(String input) {
        return Character.isLetter(input.charAt(input.length() - 1))
            ? input.substring(input.length() - 1).toUpperCase() : "";
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
