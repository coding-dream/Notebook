package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.util.FileUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class AboutController implements Initializable{
	@FXML
	private TextArea text_area_content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String content = FileUtils.read("about.txt");
		text_area_content.setText(content);
	}

	@FXML public void openWebSite() {
		this.openWebSite("https://github.com/wangli0");
	}

	@FXML public void openQQ() {
		this.openWebSite("http://wpa.qq.com/msgrd?v=3&uin=352085768&site=qq&menu=yes");
	}

	private void openWebSite(String url) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start \" \" \"" + url + "\"");
        } catch (IOException e) {
        	e.printStackTrace();
        }

    }

}
