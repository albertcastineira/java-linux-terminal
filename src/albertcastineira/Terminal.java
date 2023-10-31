package albertcastineira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Terminal {

    private String currentPath = "~";
    private final String USER = "user";
    private final int DEFAULT_CLEAR_LINES = 50;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BOLD = "\u001B[1m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final Scanner scanner = new Scanner(System.in);
    private static final Runtime.Version JAVA_VERSION = Runtime.version();

    private enum Command {
        CLEAR("clear","Cleans all the text from the terminal."),
        LS("ls","Check files inside the current directory."),
        MKDIR("mkdir","Create a new directory."),
        RM_R("rm -r","Remove a specific folder."),
        CD("cd", "Go to a specific directory."),
        PWD("pwd","Current directory"),
        LS_LA("ls -la","Show a detailed list of the files inside the current directory"),
        WHOAMI("whoami","Check the current session"),
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
    private Folder rootFolder = new Folder("bin", currentPath,null,"1");

    Terminal() {
        initialize();
    }

    private void initialize() {
        loadRootFolders();
        about();
        waitForCommand();
    }

    private void loadRootFolders() {
        createFolder("bin", currentPath,null,"1");
        createFolder("boot", currentPath,null,"1");
        createFolder("dev", currentPath,null,"1");
        createFolder("etc", currentPath,null,"1");
        createFolder("home", currentPath,null,"1");
        createFolder("lib", currentPath,null,"1");
        createFolder("media", currentPath,null,"1");
        createFolder("usr", currentPath,null,"1");
        createFolder("var", currentPath,null,"1");
        createFolder(USER,"~/usr",null,"2");
    }

    private void waitForCommand() {
        System.out.print(ANSI_RESET + ANSI_BOLD +  USER + ":" + ANSI_BLUE + currentPath + "$ ");
        String commandInput = scanner.nextLine();
        executeCommand(commandInput);
        scanner.close();
    }

    private void executeCommand(String commandInput) {
        Command userCommand = null;
        String cdCommand = "cd";
        String mkdirCommand = "mkdir";
        String rm_rCommand = "rm -r";

        if((commandSpaceCount(commandInput) >= 1)) {
            if(commandInput.contains(cdCommand)) {
                goToFolder(commandInput);
                waitForCommand();
            } else if(commandInput.contains(mkdirCommand)) {
                createFolder(
                        splitBy(commandInput," ")[1],
                        currentPath,USER,"1");
                waitForCommand();
            } else if(commandInput.contains(rm_rCommand)) {
                removeFolder(splitBy(commandInput," ")[2]);
                waitForCommand();
            }
        }


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
                case WHOAMI:
                    showCurrentUser();
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

    private void showCurrentUser() {
        print(USER);
    }

    private void goToRoot() {
        currentPath = "~";
    }

    private void showCurrentDIR() {
        print(currentPath);
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
            currentPath += "/" + folder.getName();
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
            if(folder.getPath().equals(currentPath)) {
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

    private String[] splitBy(String command, String splitString) {
        return command.split(splitString);
    }

    private void listDetailedFiles() {
        for(Folder folder : FOLDERS) {
            if(folder.getPath().equals(currentPath)) {
                print(
                        folder.getPermissions() + "\t\t" + folder.getHardLinksCount() + "\t\t" +
                                folder.getAuthor() + "\t\t " + folder.getDiskBlockCount() + "\t\t" + folder.getLastModifiedTime() + "\t\t" +
                                folder.getName()
                );
            }
        }
    }

    private void clearTerminal() {
        for (int i = 0; i < DEFAULT_CLEAR_LINES; i++) {
            System.out.println();
        }
    }

    private String truncateString(String string, int maxLength) {
        String newString;
        if (string.length() > maxLength) {
            newString = string.substring(0, maxLength);
            return (newString + "...");
        }
        return string;
    }

    private void createFolder(String folderName, String folderPath, String author, String hardLinksCount) {
        Folder folder = new Folder(folderName, folderPath, author, hardLinksCount);
        FOLDERS.add(folder);
    }

    private void removeFolder(String folderName) {
        boolean foundFolder = false;
        foundFolder = true;
        Iterator<Folder> iterator = FOLDERS.iterator();
        while (iterator.hasNext()) {
            Folder folder = iterator.next();
            if (folder.getName().equals(folderName)) {
                print("Are you sure you want to delete " + ANSI_BOLD + folder.getName() + ANSI_RESET +
                " folder ?" + ANSI_GREEN + " y" + ANSI_RESET + " or " + ANSI_RED + "n");
                String option = scanner.nextLine();
                switch (option) {
                    case "y":
                        iterator.remove();
                        break;
                    case "n":
                        break;
                    default:
                        print(ANSI_RED + "Please type y or n");
                        break;
                }
            }
        }
        if(!foundFolder) {
            print(ANSI_RED + "This folder does not exist!");
        }
    }

    private void createFile() {
        // TODO: Create basic file functionality
    }
}
