/**
 * TextBuddy is used to manipulate text in a file. User can specify
 * the name of the file upon starting the program. The User can add, 
 * delete, display and clear the content. To end the program, user
 * can simply enter "exit" as the command.
 * 
 * Example command format:
 * 		c:> TextBuddy.exe mytextfile.txt
 * 		(OR c:> java TextBuddy mytextfile.txt)
 * 		Welcome to TextBuddy. mytextfile.txt is ready for use
 * 
 * 		command: add little brown fox
 * 		added to mytextfile.txt: "little brown fox"
 * 		command: display
 * 		1. little brown fox
 * 		command: add jumped over the moon
 * 		added to mytextfile.txt: "jumped over the moon"
 * 		command: display
 * 		1. little brown fox
 * 		2. jumped over the moon
 * 		command: delete 2
 * 		deleted from mytextfile.txt: "jumped over the moon"
 * 		command: display
 * 		1. little brown fox
 * 		command: clear
 * 		all content deleted from mytextfile.txt
 * 		command: display
 * 		mytextfile.txt is empty
 * 		command: exit
 * 		c:>
 * 
 * @Pay Hao Jie
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class TextBuddy {
	
	// list of messages to be printed by the program
	private static final String MESSAGE_ERROR_INPUT = "Please enter a file name.";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use.";
	private static final String MESSAGE_COMMAND_LINE = "command: ";
	private static final String MESSAGE_ERROR_READING_FILE = "File is corrupted.";
	private static final String MESSAGE_WRONG_COMMAND = "Please enter a valid command.";
	private static final String MESSAGE_EMPTY_INPUT = "No input was added.";
	private static final String MESSAGE_ADD_SUCCESSFUL = "added to %s: \"%s\"";
	private static final String MESSAGE_EMPTY_FILE = "%s is empty.";
	private static final String MESSAGE_DISPLAY = "%d. %s";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	private static final String MESSAGE_DELETE_ERROR = "Input does not exist.";
	private static final String MESSAGE_DISPLAY_EMPTY_FILE = "%s is empty.";
	private static final String MESSAGE_DISPLAY_CLEARED_FILE = "all content deleted from %s.";
	
	// to indicate if the text file has been successfully update.
	private static final boolean FILE_WRITE_SUCCESSFUL = true;
	
	// to indicate if the line to be deleted is within range.
	private static final int INDEX_OUT_OF_BOUND = -1;
	
	// constructor
	private static String fileName;
	private static File file;
	private static Scanner sc;
	
	//data structure to store the text file
	private static ArrayList<String> dataFile;
	
	// to loop the command till an exit has been keyed in.
	private static boolean hasNoExit = true;
	private static int emptyFile = 0;

	public static void main(String[] args) throws IOException{
		processArgument(args);
		createConstructor(fileName);
		checkFileExistance(fileName);
		showToUser(MESSAGE_WELCOME);
		execute();
	}
	
	/*
	 * method for reading user's input
	 */
	private static void processArgument(String[] args) {
		if (args.length == 0) {
			System.out.println(MESSAGE_ERROR_INPUT);
		}
		else {
			fileName = args[0];
		}
	}
	
	/*
	 * method for initializing data structure.
	 */
	
	private static void createConstructor(String fileName) {
		sc = new Scanner(System.in);
		dataFile = new ArrayList<String>();
		file = new File(fileName);
	}
	
	/*
	 * Method to check if file for user's input exists.
	 */
	
	private static void checkFileExistance(String fileName) {
		try {
			file = new File(fileName);
			if (file.exists()) {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				while ((line = br.readLine()) != null) {
					dataFile.add(line);
				}
				br.close();
			}
		} catch (IOException e) {
			dataFile.clear();
			printMessage(MESSAGE_ERROR_READING_FILE);
		}
		
	}
	
	/*
	 * Print out welcome message
	 */
	
	private static void showToUser(String MESSAGE_WELCOME) {
		printMessage(MESSAGE_WELCOME);
	}
	
	/*
	 * Read user's input until exit has been entered.
	 */
	private static void execute() throws IOException {
		while (hasNoExit) {
			readCommandLine();
		}
	}
	
	/*
	 * Process user's command 
	 */
	
	private static void readCommandLine() throws IOException {
		System.out.printf(MESSAGE_COMMAND_LINE);
		String userInput;
		String input = null;
		
		userInput = sc.nextLine().trim();
		String[] temp = userInput.split(" ",2);
		String userCommand = temp[0].toLowerCase();
		
		if (temp.length > 1) {
			input = temp[1];
		}
	
		executeCommand(userCommand, input);
	}
	
	/*
	 * Handles printing of all commands.
	 */
	private static void executeCommand(String command, String input) throws IOException {
		switch (command) {
			case "add" :
				command_Add(input);
				break;
			case "display" :
				command_Display();
				break;
			case "delete" :
				command_Delete(input);
				break;
			case "clear" :
				command_Clear();
				break;
			case "exit" :
				System.exit(0);
				break;
			case "sort" :
				command_Sort();
				break;
			case "search" :
				command_Search(input);
				break;
			default :
				System.out.println(MESSAGE_WRONG_COMMAND);
				break;
		}
	}
	
	private static void command_Search(String input) {
		if (input == null) {
			System.out.println(MESSAGE_EMPTY_INPUT);
		}
		
		if (dataFile.isEmpty()) {
			printMessage(MESSAGE_DISPLAY_EMPTY_FILE);
		}
		else {
			printSearchResult(input);
		}
	}
	
	private static void printSearchResult(String input) {
		
		for (int i = 0; i < dataFile.size(); i++) {
			String inputToLowerCase = dataFile.get(i).toLowerCase();
			if (inputToLowerCase.contains(input.toLowerCase())) {
				System.out.println(dataFile.get(i));
			}
		}
	}
	/*
	 * Method to handle add command.
	 */
	private static void command_Add(String input) throws IOException {
		
		if (input == null) {
			System.out.println(MESSAGE_EMPTY_INPUT);
			readCommandLine();
		}
		
		dataFile.add(input);
		boolean isWriteSuccessful = writeToFile();
		
		if (isWriteSuccessful) {
			System.out.println(String.format(MESSAGE_ADD_SUCCESSFUL, fileName, input));
		}
		else {
			System.out.println(MESSAGE_EMPTY_INPUT);
		}	
	}
	
	/*
	 * Method to handle display command.
	 */
	private static void command_Display() {
		if (dataFile.isEmpty()) {
			printMessage(MESSAGE_DISPLAY_EMPTY_FILE);
		}
		else {
			for (int i = 0; i < dataFile.size(); i++) {
				System.out.println(String.format(MESSAGE_DISPLAY, i + 1, dataFile.get(i)));
			}
		}
	}
	
	/*
	 * Method to handle delete command.
	 */
	
	private static void command_Delete(String input) throws IOException {
		if (dataFile.isEmpty()) {
			printMessage(MESSAGE_EMPTY_FILE);
		}
		else if (input == null) {
			System.out.println(MESSAGE_WRONG_COMMAND);
			readCommandLine();
		}
		else {
			int index = getIndex(input) - 1;
			deleteInput(index);	
		}
	}
	
	/*
	 * Method to handle clear command.
	 */
	private static void command_Clear() {
		dataFile.clear();
		printMessage(MESSAGE_DISPLAY_CLEARED_FILE);
	
	}
	
	/*
	 * 
	 */
	private static void command_Sort() {
		Collections.sort(dataFile, String.CASE_INSENSITIVE_ORDER);
		command_Display();
	}
	
	/*
	 * Retrieve the index of the task from ArrayList.
	 */
	private static int getIndex(String input) throws IOException {
		
		try {
			if (Integer.parseInt(input) <= dataFile.size()) {
				return Integer.parseInt(input);
			}
			else {
				System.out.println(MESSAGE_DELETE_ERROR);
			}
		}
		catch (Exception e) {
		}
		return INDEX_OUT_OF_BOUND;
	}
	
	/*
	 * Delete the task from ArrayList.
	 */
	
	private static void deleteInput(int index) {
		
		if (index <= INDEX_OUT_OF_BOUND) {
			return;
		}
		
		try {
			String str = dataFile.get(index);
			dataFile.remove(index);
			System.out.println(String.format(MESSAGE_DELETE, file.getName(), str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writeToFile();
	
		}
	
	/*
	 * Update and save the latest version of textbuddy to txt file.
	 */
	private static boolean writeToFile() {
		try {
			if (dataFile.size() > 0) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
				for (String s: dataFile) {
					bw.write(s);
					bw.newLine();
				}
				bw.close();
			}
			else {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
				bw.close();
			}
		} catch (IOException e) {
		}
		return FILE_WRITE_SUCCESSFUL;
		
	}
	
	/*
	 * Handles printing of messages.
	 */
	
	private static void printMessage(String message) {
		System.out.println(String.format(message, file.getName()));
	}
	
	
}
