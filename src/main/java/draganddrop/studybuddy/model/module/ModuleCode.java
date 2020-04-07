package draganddrop.studybuddy.model.module;

import java.util.stream.IntStream;

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
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, input.toCharArray().length)
                .mapToObj(i -> input.toCharArray()[i]).takeWhile(x -> Character.isAlphabetic(x))
                .forEach(x -> builder.append(x));
        if (builder.toString().length() < 2 || builder.toString().length() > 3) {
            throw new ModuleCodeException("Please enter a valid module code.");
        } else {
            return builder.toString().toUpperCase();
        }
    }

    /**
     * Parse the number part of the module code and make sure the number part is at most 4 digits.
     *
     * @param input
     * @return
     * @throws ModuleCodeException
     */
    private static int parseNumber(String input) throws ModuleCodeException {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, input.toCharArray().length)
                .mapToObj(i -> input.toCharArray()[i]).filter(x -> Character.isDigit(x))
                .forEach(x -> builder.append(x));
        if (builder.toString().length() == 4) {
            return Integer.parseInt(builder.toString());
        } else {
            throw new ModuleCodeException("Please enter a valid module code.");
        }
    }

    /**
     * Parses postfix from the input to ensure correctness of moduleCode.
     *
     * @param input
     * @return
     */
    private static String parsePostfix(String input) throws ModuleCodeException {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, input.toCharArray().length)
                .mapToObj(i -> input.toCharArray()[i]).dropWhile(x -> Character.isAlphabetic(x))
                .dropWhile(x -> Character.isDigit(x)).forEach(x -> builder.append(x));
        if (builder.toString().length() < 2) {
            return builder.toString();
        } else {
            throw new ModuleCodeException("Please enter a valid module code.");
        }


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
        } else if (object instanceof String) {
            if (this.toString().equals(((String) object).toUpperCase())) {
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
