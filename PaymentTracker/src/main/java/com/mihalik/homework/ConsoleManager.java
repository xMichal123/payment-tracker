package main.java.com.mihalik.homework;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.java.com.mihalik.homework.exceptions.IllegalCurrencyFormatException;
/**
 * This class handles all the console operations
 * @author Michal Mihalik
 *
 */
public class ConsoleManager implements TextConstants {
	
	private static final char DECIMAL_SEPARATOR;
	
	static {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		DECIMAL_SEPARATOR = symbols.getDecimalSeparator();
	}
	
	private static final String CMD_QUIT = "quit";
	private static final String CMD_RATE = "*";
	private static final String CMD_FILE = "+";
	
	private static final String MESSAGE_WRONG_INPUT = "Error: Your input is wrong.";
	private static final String MESSAGE_USAGE = "USAGE:" + LINE_BREAK + "1. add 3-letter currency with amount e.g.: USD 100" + DECIMAL_SEPARATOR + "5"
			 + LINE_BREAK + "2. add exchange rate for currency with '*' command e.g.: * CZK 22" + DECIMAL_SEPARATOR + "166"
			 + LINE_BREAK + "3. add file with commands like in 1st and 2nd step with '+' command e.g.: + input.txt"
			 + LINE_BREAK + "NOTE 1: please use the same decimal separator as defined in your system locale: '" + DECIMAL_SEPARATOR + "'"
			 + LINE_BREAK + "NOTE 2: You can also run this app with one arg which will be taken as a file name with the same behavior as in point 3.";
	private static final String MESSAGE_WELCOME = "Welcome to the Payment Tracker!";
	private static final String MESSAGE_GOODBYE = "Goodbye! Hope to see you on job interview!";
	private static final String MESSAGE_WRONG_NUM_ARGS = "Wrong number of arguments!";
	private static final String MESSAGE_FILE_LOADED = "File loaded.";
	private static final String MESSAGE_FILE_LOADED_ERRORS = "File loaded with errors.";
	
	private MoneyHolder holder;
	
	/**
	 * Constructor. Creates instance of MoneyHolder.
	 */
	public ConsoleManager() {
		holder = new MoneyHolder();
	}

	/**
	 * Generates output which is called by a timer task.
	 */
	public void generateOutput() {
		writeToConsole(holder.toString());
	}
	
	/**
	 * Writes to console a string
	 * @param toWrite a string to write
	 */
	public synchronized void writeToConsole(String toWrite) {
		System.out.print(toWrite);
	}

	/**
	 * Writes to console a string with line break
	 * @param toWrite a string to write
	 */
	public synchronized void writelnToConsole(String toWrite) {
		writeToConsole(toWrite + LINE_BREAK);
	}
	
	/**
	 * Writes to error console a string
	 * @param toWrite a string to write
	 */
	public synchronized void errorToConsole(String toWrite) {
		System.err.print(toWrite);
	}

	/**
	 * Writes to error console a string with line break
	 * @param toWrite a string to write
	 */
	public synchronized void errorlnToConsole(String toWrite) {
		writeToConsole(toWrite + LINE_BREAK);
	}
	
	/**
	 * This is an user action cycle.
	 * @throws IOException
	 */
	public void userActionCycle() throws IOException {
		writelnToConsole(MESSAGE_WELCOME);
		writelnToConsole(MESSAGE_USAGE);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		readStream(in, true);
		
		writelnToConsole(MESSAGE_GOODBYE);
	}

	/**
	 * This method reads the stream and processes the commands.
	 * It can be run both from user input or from file or any other input.
	 * @param in input to read from
	 * @param printOutput if true, output is printed to console
	 * @return true if input processed without errors
	 * @throws IOException
	 */
	private boolean readStream(BufferedReader in, boolean printOutput) throws IOException {
		String line = "";
		boolean valid = true;
		
		while ((line = in.readLine()) != null) {
			try {
				boolean rateMode = false;
				boolean fileMode = false;
				
				List<String> inputList = new ArrayList<>();
				
				if (line.equalsIgnoreCase(CMD_QUIT)) {
					break;
				} else if (line.matches("\\s*")) {
					continue;
				} else {
					Scanner sc = new Scanner(line);
					
					while (sc.hasNext()) {
						String word = sc.next();
						
						if (word.equals(CMD_RATE)) {
							rateMode = true;
						} else if (word.equals(CMD_FILE)) {
							fileMode = true;
						} else {
							inputList.add(word);
						}
					}
					
					sc.close();
				}
				
				if (fileMode) {
					if (inputList.size() != 1) {
						if (printOutput) {
							errorlnToConsole(MESSAGE_WRONG_NUM_ARGS);
							writelnToConsole(MESSAGE_USAGE);
						}
						
						valid = false;
						continue;
					}
					
					String filename = inputList.get(0);
					loadFile(filename);
				} else {
					if (inputList.size() != 2) {
						if (printOutput) {
							errorlnToConsole(MESSAGE_WRONG_NUM_ARGS);
							writelnToConsole(MESSAGE_USAGE);
						}
						
						valid = false;						
						continue;
					}
					
					String currency = inputList.get(0);

					DecimalFormat decimalFormat = new DecimalFormat();
					decimalFormat.setParseBigDecimal(true);
					BigDecimal amount = (BigDecimal) decimalFormat.parse(inputList.get(1));
					
					if (rateMode) {
						holder.addRate(currency, amount);
					} else {
						holder.addAmount(currency, amount);
					}
				}
			} catch (IllegalCurrencyFormatException e) {
				valid = false;						
				writelnToConsole(e.getMessage());
			} catch (Exception e) {
				if (printOutput) {
					errorlnToConsole(MESSAGE_WRONG_INPUT);
					writelnToConsole(MESSAGE_USAGE);
				}
				
				valid = false;
			}
		}
		
		in.close();
		
		return valid;
	}
	
	/**
	 * Loads and processes a command file.
	 * @param filename name of file
	 * @throws IOException
	 */
	public void loadFile(String filename) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		boolean valid = readStream(in, false);
		writelnToConsole(valid ? MESSAGE_FILE_LOADED : MESSAGE_FILE_LOADED_ERRORS);
	}
}
