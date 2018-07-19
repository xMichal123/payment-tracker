import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentTracker implements TextConstants {
	private static final String MESSAGE_USER_ACTION_FAILED = "User action failed.";
	
	//private static long PERIOD = 60000;
	private static long PERIOD = 5000;

	private ConsoleManager manager;
	
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
	
	public static void main(String[] args) {
		new PaymentTracker(args).start();
	}

}
