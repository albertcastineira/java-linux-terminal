package albertcastineira;

import java.util.ArrayList;
import java.util.Scanner;

public class Terminal {

    private String CURRENT_PATH = "~";
    private String USER = "defaultUser";

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BOLD = "\u001B[1m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private Scanner SCANNER = new Scanner(System.in);

    private enum Command {
        HELP("help","Display all the available commands."),
        EXIT("exit", "Exit the terminal."),
        ABOUT("about","Basic description & Author.");

        private final String commandValue;
        private final String description;
        Command(String commandValue, String description) {
            this.commandValue = commandValue;
            this.description = description;
        }

        public String getCommandValue() {
            return commandValue;
        }

        public String getDescription() {
            return description;
        }
    }
    private enum CommandDialog {
        HELP("You entered the 'help' command.\n"),
        NOT_FOUND("Command not found");

        private final String dialogValue;
        CommandDialog(String dialogValue) {
            this.dialogValue = dialogValue;
        }
    }
    private enum About {
        NAME("[ Linux-Lite-Terminal ]"),
        AUTHOR("Made by Albert Castiñeira\n");
        private final String text;
        About(String text) {
            this.text = text;
        }
    }
    private ArrayList<Folder> FOLDERS = new ArrayList<Folder>();


    Terminal() {
        initialize();
    }

    private void initialize() {
        waitForCommand();
        Folder testFolder = new Folder("test");
        print(testFolder.getName() + " " + testFolder.getLastModifiedTime());
    }

    private void waitForCommand() {
        System.out.print(ANSI_RESET + ANSI_BOLD +  USER + ":" + ANSI_BLUE + CURRENT_PATH + "$ ");
        String commandInput = SCANNER.nextLine();
        executeCommand(commandInput);
        SCANNER.close();
    }

    private void executeCommand(String commandInput) {

        Command userCommand = null;
        for (Command c : Command.values()) {
            if (c.getCommandValue().equals(commandInput)) {
                userCommand = c;
                break;
            }
        }

        if (userCommand != null) {
            switch (userCommand) {
                case ABOUT:
                    about();
                    break;
                case HELP:
                    print(ANSI_RESET + ANSI_BOLD + CommandDialog.HELP.dialogValue);
                    printAllCommands();
                    break;
                case EXIT:
                    System.exit(0);
                    break;
            }
        } else {
            print(ANSI_RED + CommandDialog.NOT_FOUND.dialogValue);
        }
        waitForCommand();
    }

    private void print(String line) {
        System.out.println(line);
    }

    private void about() {
        print(ANSI_BOLD + About.NAME.text + ANSI_RESET);
        print(About.AUTHOR.text);
    }

    private void printAllCommands() {
        for (Command command : Command.values()) {
            print(ANSI_RESET + ANSI_BOLD + command.getCommandValue() + ": " + ANSI_RESET + command.getDescription());
        }
    }
}
