package main.java.com.mihalik.homework.exceptions;

public class IllegalCurrencyFormatException extends Exception {

	public IllegalCurrencyFormatException() {
		super("Illegal Currency Format. Please use 3 uppercase letters");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
