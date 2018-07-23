package main.java.com.mihalik.homework;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import main.java.com.mihalik.homework.exceptions.IllegalCurrencyFormatException;

public class MoneyHolder implements TextConstants {
	private static final String SPACE = " ";
	private static final String BRACKET_START = "(";
	private static final String USD = "USD";
	private static final String BRACKET_END = ")";
	private static final String MESSAGE_CURRENCY_LIST = "List of currencies and their amounts:";
	
	
	private TreeMap<String, BigDecimal> currencyAmmountMap = new TreeMap<>();
	private Map<String, BigDecimal> currencyRateMap = new HashMap<>();
	
	
	/**
	 * Validates a currency. It must be 3 uppercase letters.
	 * @param currency currency to validate
	 * @return true if currency is valid
	 */
	private boolean validateCurrency(String currency) {
		return currency != null && currency.matches("[A-Z]{3}+");
	}
	
	/**
	 * Adds amount of money.
	 * @param currency currency
	 * @param toAdd amount to be added
	 * @throws IllegalCurrencyFormatException
	 */
	public void addAmount(String currency, BigDecimal toAdd) throws IllegalCurrencyFormatException {
		if (!validateCurrency(currency)) {
			throw new IllegalCurrencyFormatException();
		}
		
		BigDecimal currentValue = currencyAmmountMap.get(currency);
		
		if (currentValue == null) {
			currentValue = new BigDecimal(0);
		}
		
		BigDecimal newValue = currentValue.add(toAdd);
		
		currencyAmmountMap.put(currency, newValue);
	}
	
	/**
	 * Adds currency exchange rate compared to USD.
	 * @param currency currency
	 * @param toAdd rate to be added
	 * @throws IllegalCurrencyFormatException
	 */
	public void addRate(String currency, BigDecimal toAdd) throws IllegalCurrencyFormatException {
		if (!validateCurrency(currency)) {
			throw new IllegalCurrencyFormatException();
		}
		
		currencyRateMap.put(currency, toAdd);
	}
	
	/**
	 * Returns the number in a locale format.
	 * @param dec number to format
	 * @return number in local format
	 */
	private String toLocaleString(BigDecimal dec) {
	      DecimalFormat format = new DecimalFormat();
	      return format.format(dec);
	}
	
	/**
	 * toString() method overriden. This method prints the output to the console.
	 * Printed are amounts of currencies which are not equal to zero and their USD equivalent (if available the rate for the currency)
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		boolean first = true;
		
		for (Entry<String, BigDecimal> entry : currencyAmmountMap.entrySet()) {
			String currency = entry.getKey();
			BigDecimal value = entry.getValue();
			BigDecimal rate = currencyRateMap.get(currency);
			
			if (value.signum() != 0) {
				if (first) {
					buffer.append(LINE_BREAK);
					buffer.append(MESSAGE_CURRENCY_LIST);
					buffer.append(LINE_BREAK);
					
					first = false;
				}
				
				buffer.append(currency);
				buffer.append(SPACE);
				buffer.append(toLocaleString(value));
				
				if (rate != null) {
					buffer.append(SPACE);
					buffer.append(BRACKET_START);
					buffer.append(USD);
					buffer.append(SPACE);
					buffer.append(toLocaleString(value.multiply(rate)));
					buffer.append(BRACKET_END);
				}
				
				buffer.append(LINE_BREAK);
			}
		}
		
		return buffer.toString();
	}
}
