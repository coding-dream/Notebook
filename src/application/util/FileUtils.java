package application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class FileUtils {

	public static void toClipboard(String message) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(message);
        clipboard.setContent(clipboardContent);
    }

	public static boolean isImage(File file){
		Pattern pattern = Pattern.compile(".*(.jpg|.JPG|.jpeg|.JPEG|.png|.PNG|.gif|.GIF|.bmp|.BMP)$");
        Matcher matcher = pattern.matcher(file.getName());
        return matcher.matches();
	}

	public static String read(String path){
		try {
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while((line = bufferedReader.readLine()) != null){
				buffer.append(line + "\r\n");
			}
			bufferedReader.close();
			fileReader.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
