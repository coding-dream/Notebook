package application.dialog;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DialogHelper {

	public static void alert(String title, String message){
		new AlertDialog.Builder()
		.view("alert_dialog")
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
		.view("alert_dialog")
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
		.view("about")
		.title("¹ØÓÚ")
		.build()
		.show();
	}
}
