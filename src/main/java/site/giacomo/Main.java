package site.giacomo;

import java.util.*;

public class Main {
    public static void main(String[] args){
        // Creating our parser object.
        PropParser parser = new PropParser();
        // Generating our file properties.
        parser.generateProperties();
        boolean loopCheck = true;
        System.out.println("Bot initiated. Enter start to start the bot or help to view a list of available commands");
        Scanner scanner = new Scanner(System.in);
        while (loopCheck){
            String command = scanner.nextLine();
            if (command.equals("start")){
                Bot.main(parser.getAuthToken()); // this will be replaced with a config file read later on.
            }else if (command.equals("help")) {
                System.out.println("Currently our set of user commands include:\n> start - start the bot. \n> exit - exit the bot. \n> help - view this list of commands. \n> To edit the bots information, please refer to the settings file. ");
            }else if(command.equals("exit")){
                System.exit(0);
            }else{
                System.out.println("The command you have entered is unrecognised. Type 'help' to preview a list of available commands.");
            }
        }

    }
}
