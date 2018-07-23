package test.java.com.mihalik.homework;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import org.junit.Test;

import main.java.com.mihalik.homework.MoneyHolder;
import main.java.com.mihalik.homework.TextConstants;
import main.java.com.mihalik.homework.exceptions.IllegalCurrencyFormatException;

public class MoneyHolderTest implements TextConstants  {
	@Test
	public void validateCurrencyTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MoneyHolder moneyHolder = new MoneyHolder();
        Method method = MoneyHolder.class.getDeclaredMethod("validateCurrency", String.class);
        method.setAccessible(true);
        
        assertEquals(false, method.invoke(moneyHolder, (String)null));
        assertEquals(false, method.invoke(moneyHolder, ""));
        assertEquals(false, method.invoke(moneyHolder, "abc"));
        assertEquals(false, method.invoke(moneyHolder, "abcd"));
        assertEquals(true, method.invoke(moneyHolder, "ABC"));
        assertEquals(false, method.invoke(moneyHolder, "ABCD"));
        assertEquals(false, method.invoke(moneyHolder, "654"));
        assertEquals(false, method.invoke(moneyHolder, "VB4"));
        assertEquals(false, method.invoke(moneyHolder, "VBA4"));
        assertEquals(false, method.invoke(moneyHolder, "$%*"));
	}
	
	@Test
	public void addAmountTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IllegalCurrencyFormatException {
        MoneyHolder moneyHolder = new MoneyHolder();
        
        Field field = MoneyHolder.class.getDeclaredField("currencyAmmountMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
		Map<String, BigDecimal> map = (Map<String, BigDecimal>)field.get(moneyHolder);
        
        moneyHolder.addAmount("SVK", new BigDecimal(150));
        moneyHolder.addAmount("GBR", new BigDecimal(-100));
        moneyHolder.addAmount("USD", new BigDecimal(250.15));
        moneyHolder.addAmount("CZK", new BigDecimal(320));
        moneyHolder.addAmount("SVK", new BigDecimal(-150));
        moneyHolder.addAmount("CZK", new BigDecimal(-400));
        
        assertEquals(new BigDecimal(0), map.get("SVK"));
        assertEquals(new BigDecimal(-100), map.get("GBR"));
        assertEquals(new BigDecimal(250.15), map.get("USD"));
        assertEquals(new BigDecimal(-80), map.get("CZK"));
	}
	
	@Test
	public void addRateTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IllegalCurrencyFormatException {
        MoneyHolder moneyHolder = new MoneyHolder();
        
        Field field = MoneyHolder.class.getDeclaredField("currencyRateMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
		Map<String, BigDecimal> map = (Map<String, BigDecimal>)field.get(moneyHolder);
        
        moneyHolder.addRate("SVK", new BigDecimal(0.5));
        moneyHolder.addRate("GBR", new BigDecimal(1.5));
        moneyHolder.addRate("CZK", new BigDecimal(0.2345));
        moneyHolder.addRate("SVK", new BigDecimal(3.54));
        moneyHolder.addRate("CZK", new BigDecimal(2.92));
        
        assertEquals(new BigDecimal(3.54), map.get("SVK"));
        assertEquals(new BigDecimal(1.5), map.get("GBR"));
        assertEquals(new BigDecimal(2.92), map.get("CZK"));
	}
	
	@Test
	public void toLocaleStringTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IllegalCurrencyFormatException {
        MoneyHolder moneyHolder = new MoneyHolder();
        Method method = MoneyHolder.class.getDeclaredMethod("toLocaleString", BigDecimal.class);
        method.setAccessible(true);
        
        DecimalFormat format = new DecimalFormat();
        
        assertToLocaleString(moneyHolder, format, method, new BigDecimal(150));
        assertToLocaleString(moneyHolder, format, method, new BigDecimal(-123.45));
        assertToLocaleString(moneyHolder, format, method, new BigDecimal(145.8));
        assertToLocaleString(moneyHolder, format, method, new BigDecimal(23.247));
        assertToLocaleString(moneyHolder, format, method, new BigDecimal(-35));
	}
	
	private void assertToLocaleString(MoneyHolder moneyHolder, DecimalFormat format, Method method, BigDecimal number) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(format.format(number), method.invoke(moneyHolder, number));
	}
	
	@Test
	public void toStringTest() throws IllegalCurrencyFormatException {
        MoneyHolder moneyHolder = new MoneyHolder();
        
        DecimalFormat format = new DecimalFormat();
        char separator = format.getDecimalFormatSymbols().getDecimalSeparator();
		
        moneyHolder.addAmount("SVK", new BigDecimal(150));
        moneyHolder.addAmount("GBR", new BigDecimal(-100));
        moneyHolder.addAmount("USD", new BigDecimal(250.15));
        moneyHolder.addAmount("CZK", new BigDecimal(320));
        moneyHolder.addAmount("SVK", new BigDecimal(-150));
        moneyHolder.addAmount("CZK", new BigDecimal(-400));
        
        assertEquals(LINE_BREAK + "List of currencies and their amounts:" + LINE_BREAK
        	+ "CZK -80" + LINE_BREAK
        	+ "GBR -100" + LINE_BREAK
        	+ "USD 250" + separator + "15" + LINE_BREAK,
        	moneyHolder.toString());
        
        moneyHolder.addRate("SVK", new BigDecimal(0.5));
        moneyHolder.addRate("GBR", new BigDecimal(1.5));
        moneyHolder.addRate("CZK", new BigDecimal(0.2345));
        moneyHolder.addRate("SVK", new BigDecimal(3.54));
        moneyHolder.addRate("CZK", new BigDecimal(2.92));

        assertEquals(LINE_BREAK + "List of currencies and their amounts:" + LINE_BREAK
        	+ "CZK -80 (USD -233" + separator + "6)" + LINE_BREAK
        	+ "GBR -100 (USD -150)" + LINE_BREAK
        	+ "USD 250" + separator + "15" + LINE_BREAK,
        	moneyHolder.toString());
		
        moneyHolder.addAmount("SVK", new BigDecimal(150));
        moneyHolder.addAmount("GBR", new BigDecimal(-100));
        moneyHolder.addAmount("USD", new BigDecimal(250.15));
        moneyHolder.addAmount("ABC", new BigDecimal(320));

        System.out.println(moneyHolder.toString());
        assertEquals(LINE_BREAK + "List of currencies and their amounts:" + LINE_BREAK
            + "ABC 320" + LINE_BREAK
        	+ "CZK -80 (USD -233" + separator + "6)" + LINE_BREAK
        	+ "GBR -200 (USD -300)" + LINE_BREAK
        	+ "SVK 150 (USD 531)" + LINE_BREAK
        	+ "USD 500" + separator + "3" + LINE_BREAK,
        	moneyHolder.toString());
	}
}
