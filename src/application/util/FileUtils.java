package application.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class FileUtils {

	public static void toClipboard(String message) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(message);
        clipboard.setContent(clipboardContent);
    }
}
