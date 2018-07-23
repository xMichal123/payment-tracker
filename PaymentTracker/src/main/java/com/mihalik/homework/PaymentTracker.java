package main.java.com.mihalik.homework;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentTracker implements TextConstants {
	private static final String MESSAGE_USER_ACTION_FAILED = "User action failed.";
	
	private static long PERIOD = 60000;
	//private static long PERIOD = 5000;

	private ConsoleManager manager;
	
	/**
	 * Constructor. Console Manager is created. Also first arg is passed to Console Manager if any in order to load from file.
	 * Console manager handles the user input.
	 * @param args
	 */
	PaymentTracker(String[] args) {
		manager = new ConsoleManager();
		
		if (args.length > 0) {
			try {
				manager.loadFile(args[0]);
			} catch (IOException e) {
				manager.errorlnToConsole(MESSAGE_FILE_NOT_FOUND);
			}
		}
	}
	
	/**
	 * Starts the timer task.
	 * It generates the output to the console periodically every one minute.
	 * Also user action listening cycle is launched.
	 */
	private void start() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				manager.generateOutput();
			}
			
		}, 0, PERIOD);
		
		try {
			manager.userActionCycle();
		} catch (IOException e) {
			manager.errorlnToConsole(MESSAGE_USER_ACTION_FAILED);
		}
		
		timer.cancel();
	}
	
	/**
	 * Main.
	 * @param args optional one arg: a file with commands can be passed
	 */
	public static void main(String[] args) {
		new PaymentTracker(args).start();
	}

}
