import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private Scanner scanner = new Scanner(System.in); // Creates Scanner for inputs
    private JFileChooser fileChooser = new JFileChooser(); // Creates a new JFileChooser to let the user select files in a gui window
    private ArrayList<String> files = new ArrayList<>(); // Creates a new ArrayList, that contains all the added file paths

    public static void main(String[] args) { // Main Method called at program start
        Main main = new Main(); // Creates a new Instance of the Main class
        main.mainConsole(); // Runs the mainConsole() Method in the Main class
    }

    public Main(){} // Default Constructor

    public void mainConsole(){ // The main console that handles all the user commands
        while(true){
            printFiles();
            System.out.print("\nPlease enter a command (add, delete, done, exit): ");
            String input = stringInput();

            switch(input.toLowerCase()){ // Checks if a valid command has been entered and if so, runs the corresponding method
                case "add":
                    selectFile();
                    break;
                case "done":
                    if(files.size() > 0){
                        createFile();
                    } else {
                        System.out.println("\nMust at least add 1 file to the list.");
                    }
                    break;
                case "delete":
                    if(files.size() > 0){
                        deleteItem();
                    } else {
                        System.out.println("\nThere are no elements in the list");
                    }
                    break;
                case "exit":
                    System.exit(0);
                default:
                    if(input != "") {
                        System.out.println("\n" + input + " is not a valid command.");
                    }
                    break;
            }
        }
    }

    public void selectFile(){ // Lets the user select a file to add to the files list
        fileChooser.resetChoosableFileFilters(); // Resets the filetype filters
        fileChooser.setAcceptAllFileFilterUsed(true); // Activates the "All files" filetype in the file dialog

        int responseValue = fileChooser.showOpenDialog(null); // Returns 0 if the user has selected a file, returns 1 if not

        if(responseValue == JFileChooser.APPROVE_OPTION){ // If the user has selected a file
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath()); // Creates a new File object and stores the path to the previously selected file
            files.add(file.toString()); // Converts the File Object to a String, and adds the file path to the files ArrayList
        }
    }

    public void printFiles(){ // Prints all the file paths currently stored in the files ArrayList
        if(files.size() != 0){
            System.out.println("\nCurrent files added: ");
        } else {
            System.out.println("\nNo files added yet.");
        }

        for(String file : files){
            System.out.println(file);
        }
    }

    public void createFile(){ // Lets the user save the finished .bat file
        Path path; // Initializes a new Path object, that will later store the path where the file will be saved

        //Creates a FileNameExtensionsFilter object for the .bat file type
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".bat file", "bat");
        fileChooser.setAcceptAllFileFilterUsed(false); // Deactivates the "All files" filetype in the file dialog

        fileChooser.setFileFilter(filter); // Applies the previously created filter object to the fileChooser

        int responseValue = fileChooser.showSaveDialog(null); // Returns 0 if the user has selected a file, returns 1 if not

        if(responseValue == JFileChooser.APPROVE_OPTION){ // If the user has selected a file

            File file = new File(fileChooser.getSelectedFile().getAbsolutePath()); // Creates a new File object and stores the path to the previously selected file

            if(file.toString().endsWith(".bat")){ // Checks if the filename entered by the user ends with .bat
                path = Paths.get(file.toString()); // If yes, only stores the filepath in the path object
            } else {
                path = Paths.get(file + ".bat"); // If no, adds a ".bat" at the end of the filename entered by the user
            }

            try{ // Checks if the file can be created (name isn't already used)
                Files.createFile(path);
            } catch(IOException e){ // If it's already used gives an error message and returns to the mainConsole
                System.out.println("\nError, filename is already used.");
                return;
            }

            for(String fileItem : files){ // Appends every file path in the files ArrayList to the previously created file
                try{
                    if(Files.size(path) == 0){
                        Files.writeString(path, "start \"\" \"" + fileItem + "\"", StandardOpenOption.APPEND);
                    } else {
                        Files.writeString(path, "\nstart \"\" \"" + fileItem + "\"", StandardOpenOption.APPEND);
                    }
                } catch(IOException e){}
            }
            System.out.println("\nFile successfully created in " + path);
        }
    }

    public void deleteItem(){ // Lets the user delete a file path in the files ArrayList
        System.out.println();
        for(int x = 1; x <= files.size(); x++){ // Prints out all the file paths in the files ArrayList and gives them an index number
            System.out.println("[" + x + "] " + files.get(x - 1));
        }

        while(true){ // Loops while the user hasn't entered a valid index number or has canceled by entering -1
            System.out.print("\nPlease enter the index of the item you want to remove (-1 to cancel): ");
            int index = intInput();

            if(index >= 1 && index <= files.size()){ // If the index number is valid the corresponding file path gets removed
                files.remove(index - 1);
                System.out.println("\nSuccessfully removed the item at index " + index);
                return;
            } else if (index == -1) { // If the user enters -1 the deletion process gets canceled
                System.out.println("\nRemoval has been canceled.");
                return;
            } else { // If the user hasn't made a valid input
                System.out.println("\nThe index " + index + " is not a valid index.");
            }
        }
    }

    public String stringInput(){ // Handles the String input
        return scanner.nextLine();
    }

    public int intInput(){ // Handles the int input
        while(true){
            if(scanner.hasNextInt()){
                int number = scanner.nextInt();
                scanner.nextLine();
                return number;
            } else {
                System.out.println("\n\"" + scanner.nextLine() + "\" is not a valid number. Please enter again: ");
            }
        }
    }
}