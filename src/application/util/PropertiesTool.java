package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesTool {
	private static Properties properties ;

	static {
		try {
			properties = new Properties();
			InputStream inputStream = new FileInputStream(new File("config.properties"));
			properties.load(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String get(String key){
		String defaultValue = null;
		String value = properties.getProperty(key, defaultValue);
		if(value == null){
			throw new RuntimeException(key + " not exits");
		}
		return value;
	}
}
