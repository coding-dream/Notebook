package application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileUtils {
	private static File justVisitedDir;

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

	public static File chooseFile(){

		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择文件");
        if(justVisitedDir == null) {
        	// 设置初始目录
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setInitialDirectory(justVisitedDir);
        }

        fileChooser.getExtensionFilters().addAll(new ExtensionFilter[]{new ExtensionFilter("Image Files", new String[]{"*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"}), new ExtensionFilter("JPG", new String[]{"*.jpg"}), new ExtensionFilter("JPEG", new String[]{"*.jpeg"}), new ExtensionFilter("BMP", new String[]{"*.bmp"}), new ExtensionFilter("PNG", new String[]{"*.png"}), new ExtensionFilter("GIF", new String[]{"*.gif"})});
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
        	justVisitedDir = file.getParentFile();
        }
        return file;
	}

	public static void openFile(File file) {
		if(file != null && file.exists()) {
            try {
				Runtime.getRuntime().exec("cmd.exe /c start \" \" \"" + file.getAbsolutePath() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

    }
}
