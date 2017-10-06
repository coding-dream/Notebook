package application.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class MainPresenter implements Initializable {

	@FXML
	private ImageView imageView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void dragDroppedFile(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		List files = dragboard.getFiles();
		if (files.size() > 0) {
			File file = (File) files.get(0);
			Pattern pattern = Pattern.compile(".*(.jpg|.JPG|.jpeg|.JPEG|.png|.PNG|.gif|.GIF|.bmp|.BMP)$");
			Matcher matcher = pattern.matcher(file.getName());
			if (matcher.matches()) {
				// 获取拖拽的文件
				System.out.println(file.getName());
			} else {
				throw new IllegalArgumentException("文件必须是图片");
			}
		}

	}

	@FXML
	public void dragOverFile(DragEvent event) {
		 if(event.getGestureSource() != imageView) {
			 event.acceptTransferModes(TransferMode.ANY);
		 }
	}

}
