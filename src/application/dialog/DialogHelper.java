package application.dialog;

import javafx.scene.image.Image;

public class DialogHelper {

	public static void alert(String title, String message){
		new AlertDialog.Builder()
		.view("alert_dialog")
		.title(title)
		.setText("#label_info", message)
		.build()
		.show();
	}

	public static void alert(String title, String message,Image image){
		new AlertDialog.Builder()
		.view("alert_dialog")
		.title(title)
		.setText("#label_info", message)
		.setImage("#imageview_tip", image)
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
