package com.presentation;

import com.business.Validator;
import java.util.List;

/**
 * This class is used to parse the input commands.
 */
public class CommandParser {
    /**
     * The list of commands.
     */
    private List<String> commands;
    /**
     * An instance of the Validator class.
     */
    private Validator validator = new Validator();

    public CommandParser(List<String> commands) {
        this.commands = commands;
    }

    //The operations that can be performed:
    //1. Insert client
    //2. Delete client
    //3. Insert product
    //4. Delete product
    //5. Order
    //6. Report client
    //7. Report order
    //8. Report product

    /**
     * Identifies the commands and calls the specific validation methods based on the found command.
     */
    public void parseCommands() {
        for(String command : commands) {
            if(command.equals("Report client"))
                validator.validateClientReport();
            else if(command.equals("Report product"))
                validator.validateProductReport();
            else if(command.equals("Report order"))
                validator.validateOrderReport();
            else if(containsIgnoreCase(command, "Insert client: "))
                validator.validateInsertOrDeleteClient(command, true);
            else if(containsIgnoreCase(command, "Delete client: "))
                validator.validateInsertOrDeleteClient(command, false);
            else if(containsIgnoreCase(command, "Insert product: "))
                validator.validateInsertProduct(command);
            else if(containsIgnoreCase(command, "Delete product: "))
                validator.validateDeleteProduct(command);
            else if(containsIgnoreCase(command, "Order"))
                validator.validateOrder(command);
            else
                System.out.println("Invalid command found on line " + (commands.indexOf(command) + 1));
        }
    }

    /**
     * Checks if a string contains a substring.
     * @param str The whole string.
     * @param subString The substring.
     * @return True if the substring is found in the given string otherwise false.
     */
    private boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}
