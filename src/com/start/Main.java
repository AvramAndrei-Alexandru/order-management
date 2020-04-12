package com.start;
import com.data_access.ClientsDAO;
import com.data_access.OrderItemDAO;
import com.data_access.OrdersDAO;
import com.data_access.ProductsDAO;
import com.presentation.CommandParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    /**
     * The main method for the application.
     * @param args The program arguments.
     */
    public static void main(String[] args) {
        new ClientsDAO().deleteAllData();
        new ProductsDAO().deleteAllData();
        new OrderItemDAO().deleteAllData();
        new OrdersDAO().deleteAllData();
       List<String> commands = getCommands(args[0]);
       if(commands == null) {
           System.out.println("Error at reading the commands");
           return;
       }
       if(commands.size() == 0) {
           System.out.println("Error at reading the commands");
           return;
       }
        CommandParser commandParser = new CommandParser(commands);
        commandParser.parseCommands();
    }

    /**
     *
     * @param path The path to the input file.
     * @return A list of commands
     */
    private static List<String> getCommands(String path) {
        File inputFile;
        Scanner fileReader;
        try {
            inputFile = new File(path);
            fileReader = new Scanner(inputFile);
        }catch (FileNotFoundException e) {
            System.out.println("ERROR at file level");
            e.printStackTrace();
            return null;
        }
        List<String> commands = new ArrayList<>();
        while (fileReader.hasNext()) {
            commands.add(fileReader.nextLine());
        }
        return commands;
    }
}

