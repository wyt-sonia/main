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
            throw new ModuleCodeException("ModuleCode is invalid!");
        }
    }


    private String parsePrefix(String input) throws ModuleCodeException {
        String output = "";
        int i = 0;
        while(true) {
            char character = input.charAt(i);
            try {
                int test = (int) character;     //if typecasting successful, end operation
                break;
            } catch (ClassCastException e) {    //if typecasting fail, continue
                output += character;
                System.out.println("getPrefix - output = " + output);
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
                int test = (int) character;     //if typecasting successful, add char to outputAsString
                outputAsString += character;
                System.out.println("getNumber()'s output = " + outputAsString);
            } catch (ClassCastException e) {
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
        char character = input.charAt(input.length());
        try {
            int test = (int) character;
        } catch (ClassCastException e) {
            output += character;
        }
        return output;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getNumber(){
        return number;
    }

    public String getPostfix() {
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
        System.out.println(prefix + number + postfix);
        return prefix + number + postfix;
    }

}
