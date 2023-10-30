package albertcastineira;

import java.util.ArrayList;
import java.util.Scanner;

public class Terminal {

    private String CURRENT_PATH = "~";
    private final String USER = "defaultUser";
    private final int DEFAULT_CLEAR_LINES = 50;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BOLD = "\u001B[1m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final Scanner SCANNER = new Scanner(System.in);
    private static final Runtime.Version JAVA_VERSION = Runtime.version();

    private enum Command {
        CLEAR("clear","Cleans all the text from the terminal."),
        LS("ls","Check files inside the current directory."),
        CD("cd", "Go to a specific directory."),
        PWD("pwd","Current directory"),
        LS_LA("ls -la","Show a detailed list of the files inside the current directory"),
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
    private enum FolderDialog {
        NOT_FOUND("Folder not found");

        private final String dialogValue;
        FolderDialog(String dialogValue) {
            this.dialogValue = dialogValue;
        }
    }
    private enum About {
        NAME("-+-+-+-+-+-+-+-+-+-+-+-\nJAVA LINUX TERMINAL v0.1\n-+-+-+-+-+-+-+-+-+-+-+-\n"),
        RUNNING_VERSION("Java Version: " + JAVA_VERSION),
        AUTHOR("Made by Albert Castiñeira");
        private final String text;
        About(String text) {
            this.text = text;
        }
    }
    private final ArrayList<Folder> FOLDERS = new ArrayList<Folder>();
    private Folder rootFolder = new Folder("bin",CURRENT_PATH,null,"1");

    Terminal() {
        initialize();
    }

    private void initialize() {
        loadDefaultFolders();
        about();
        waitForCommand();
    }

    private void loadDefaultFolders() {
        createFolder("bin",CURRENT_PATH,null,"1");
        createFolder("boot",CURRENT_PATH,null,"1");
        createFolder("dev",CURRENT_PATH,null,"1");
        createFolder("etc",CURRENT_PATH,null,"1");
        createFolder("home",CURRENT_PATH,null,"1");
        createFolder("lib",CURRENT_PATH,null,"1");
        createFolder("media",CURRENT_PATH,null,"1");
        createFolder("usr",CURRENT_PATH,null,"1");
        createFolder("var",CURRENT_PATH,null,"1");
        createFolder(USER,"~/usr",null,"1");
    }

    private void waitForCommand() {
        System.out.print(ANSI_RESET + ANSI_BOLD +  USER + ":" + ANSI_BLUE + CURRENT_PATH + "$ ");
        String commandInput = SCANNER.nextLine();
        executeCommand(commandInput);
        SCANNER.close();
    }

    private void executeCommand(String commandInput) {

        if(commandInput.contains("cd") && (commandSpaceCount(commandInput) == 1)) {
            goToFolder(commandInput);
            waitForCommand();
        }

        Command userCommand = null;
        for (Command c : Command.values()) {
            if (c.getCommandValue().equals(commandInput)) {
                userCommand = c;
                break;
            }
        }

        if (userCommand != null) {
            switch (userCommand) {
                case CLEAR:
                    clearTerminal();
                    break;
                case ABOUT:
                    about();
                    break;
                case HELP:
                    print(ANSI_RESET + ANSI_BOLD + CommandDialog.HELP.dialogValue);
                    printAllCommands();
                    break;
                case PWD:
                    showCurrentDIR();
                    break;
                case CD:
                    goToRoot();
                    break;
                case LS:
                    listFiles();
                    break;
                case LS_LA:
                    listDetailedFiles();
                    break;
                case EXIT:
                    System.exit(0);
                    break;
                default:
                    print(ANSI_RED + CommandDialog.NOT_FOUND.dialogValue);
                    break;
            }
        } else {
            print(ANSI_RED + CommandDialog.NOT_FOUND.dialogValue);
        }
        waitForCommand();
    }

    private void goToRoot() {
        CURRENT_PATH = "~";
    }

    private void showCurrentDIR() {
        print(CURRENT_PATH);
    }

    private void goToFolder(String folderCommand) {
        String[] split = folderCommand.split(" ");
        String folderName = split[1];
        Folder folder = null;
        for (Folder folderToFind : FOLDERS) {
            if (folderToFind.getName().equals(folderName)) {
                folder = folderToFind;
                break;
            }
        }
        if (folder != null) {
            CURRENT_PATH += "/" + folder.getName();
        } else {
            print(FolderDialog.NOT_FOUND.dialogValue);
        }

    }

    private void print(String line) {
        System.out.println(ANSI_RESET + line);
    }

    private void about() {
        print(ANSI_BOLD + About.NAME.text + ANSI_RESET);
        print(ANSI_BOLD + About.RUNNING_VERSION.text);
        print(About.AUTHOR.text);
    }

    private void printAllCommands() {
        for (Command command : Command.values()) {
            print(ANSI_BOLD + command.getCommandValue() + ": " + ANSI_RESET + command.getDescription());
        }
    }

    private void listFiles() {
        for(Folder folder : FOLDERS) {
            if(folder.getPath().equals(CURRENT_PATH)) {
                print(folder.getName());
            }
        }
    }

    private int commandSpaceCount(String command) {
        int spaceCount = 0;

        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

    private void listDetailedFiles() {
        print("total " + FOLDERS.size());
        for(Folder folder : FOLDERS) {
            print(
                folder.getPermissions() + "\t" + folder.getHardLinksCount() + "\t" +
                folder.getAuthor() + "\t" + folder.getDiskBlockCount() + "\t" + folder.getLastModifiedTime() + "\t" +
                folder.getName()
            );
        }
    }

    private void clearTerminal() {
        for (int i = 0; i < DEFAULT_CLEAR_LINES; i++) {
            System.out.println();
        }
    }

    private void createFolder(String folderName, String folderPath, String author, String hardLinksCount) {
        Folder folder = new Folder(folderName, folderPath, author, hardLinksCount);
        FOLDERS.add(folder);
    }

    private void createFile() {
        // TODO: Create basic file functionality
    }
}
