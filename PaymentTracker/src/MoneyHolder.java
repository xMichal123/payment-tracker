import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import exceptions.IllegalCurrencyFormatException;

public class MoneyHolder implements TextConstants {
	private static final String SPACE = " ";
	private static final String BRACKET_START = "(";
	private static final String USD = "USD";
	private static final String BRACKET_END = ")";
	private static final String MESSAGE_CURRENCY_LIST = "List of currencies and their amounts:";
	
	
	private Map<String, BigDecimal> currencyAmmountMap = new HashMap<>();
	private Map<String, BigDecimal> currencyRateMap = new HashMap<>();
	
	private boolean validateCurrency(String currency) {
		return currency.matches("[A-Z]{3}+");
	}
	
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
	
	public void addRate(String currency, BigDecimal toAdd) throws IllegalCurrencyFormatException {
		if (!validateCurrency(currency)) {
			throw new IllegalCurrencyFormatException();
		}
		
		BigDecimal currentValue = currencyRateMap.get(currency);
		
		if (currentValue == null) {
			currentValue = new BigDecimal(0);
		}
		
		currencyRateMap.put(currency, toAdd);
	}
	
	private String toLocaleString(BigDecimal dec) {
	      DecimalFormat format = new DecimalFormat();
	      return format.format(dec);
	}
	
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
