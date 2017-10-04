package application.util;

public class Loaders {

	public static String getRealPath(String filePath){
		return Loaders.class.getClassLoader().getResource(filePath).toExternalForm();
	}
}
