package application.util;

import java.io.File;
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
}
