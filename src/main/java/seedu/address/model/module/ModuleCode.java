package seedu.address.model.module;

import java.lang.Integer;

import seedu.address.model.module.exceptions.ModuleCodeException;

/**
 * ModuleCode. Ensures that the module code keyed in matches the proper format. XX0000.
 */
public class ModuleCode {
    public String prefix;
    public int number;
    public String postfix; //optional

    public ModuleCode(String fullModuleCode) throws ModuleCodeException {
        try {
            this.prefix = parsePrefix(fullModuleCode);
            this.number = parseNumber(fullModuleCode);
            this.postfix = parsePostfix(fullModuleCode);
        } catch (ModuleCodeException e) {
            System.out.println("ModuleCodeParsing failed");
            throw new ModuleCodeException("ModuleCode is invalid!");
        }
    }


    private String parsePrefix(String input) throws ModuleCodeException {
        String output = "";
        int i = 0;
        while(true) {
            char character = input.charAt(i);
            try {
                Integer.parseInt("" + character);     //if parseInt successful, end operation
                break;
            } catch (NumberFormatException e) {    //if typecasting fail, continue
                output += character;
                i++;
            }
        }

        if (output.equals("")) {
            throw new ModuleCodeException("Module prefix not found!!!");
        } else {
            return output.toUpperCase();
        }
    }

    private int parseNumber(String input) throws ModuleCodeException {
        String outputAsString = "";
        for(int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            try {
                Integer.parseInt("" + character);     //if parseInt successful, add char to outputAsString
                outputAsString += character;
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        if (outputAsString.equals("")) {
            throw new ModuleCodeException("Module number not found!!!");
        } else {
            int output = Integer.parseInt(outputAsString);
            return output;
        }
    }

    private String parsePostfix(String input) {
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

    private int getNumber(){
        return number;
    }

    private String getPostfix() {
        return postfix;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ModuleCode) {
            if(this.getPrefix().equals(((ModuleCode) object).prefix)
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
    public String toString(){
        return prefix + String.format("%04d", number) + postfix;
    }

}
