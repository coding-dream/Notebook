package application.dialog;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DialogHelper {

	public static void alert(String title, String message){
		new AlertDialog.Builder()
		.view("dialog_alert")
		.title(title)
		.setText("#label_info", message)
		.click("#btn_confirm", new OnClickListener() {

			@Override
			public void onClick(Stage stage) {
				stage.close();
			}
		})
		.build()
		.show();
	}

	public static void alert(String title, String message,Image image){
		new AlertDialog.Builder()
		.view("dialog_alert")
		.title(title)
		.setText("#label_info", message)
		.setImage("#imageview_tip", image)
		.click("#btn_confirm", new OnClickListener() {

			@Override
			public void onClick(Stage stage) {
				stage.close();
			}
		})
		.build()
		.show();
	}

	public static void about(){
		new AlertDialog.Builder()
		.view("dialog_about")
		.title("关于")
		.build()
		.show();
	}

	public static void progressbar() {
		new AlertDialog.Builder()
		.view("dialog_progressbar")
		.title("数据加载中")
		.build()
		.show();
	}
}
