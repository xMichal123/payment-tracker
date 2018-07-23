package test.java.com.mihalik.homework;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.junit.Test;

import main.java.com.mihalik.homework.ConsoleManager;

public class ConsoleManagerTest {
	@Test
	public void readStreamTest() throws NoSuchMethodException, SecurityException, URISyntaxException, FileNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = ConsoleManager.class.getDeclaredMethod("readStream", BufferedReader.class, boolean.class);
        method.setAccessible(true);
		
		ConsoleManager manager = new ConsoleManager();
        String test = (new File(this.getClass().getResource("/test/resources/test1.txt").toURI())).getAbsolutePath();
		BufferedReader in = new BufferedReader(new FileReader(test));
		boolean output = (boolean) method.invoke(manager, in, false);
        assertEquals(true, output);
		
		manager = new ConsoleManager();
        test = (new File(this.getClass().getResource("/test/resources/test2.txt").toURI())).getAbsolutePath();
		in = new BufferedReader(new FileReader(test));
		output = (boolean) method.invoke(manager, in, false);
        assertEquals(false, output);
		
		manager = new ConsoleManager();
        test = (new File(this.getClass().getResource("/test/resources/test3.txt").toURI())).getAbsolutePath();
		in = new BufferedReader(new FileReader(test));
		output = (boolean) method.invoke(manager, in, false);
        assertEquals(false, output);
	}
}
