package application;

import application.dialog.AlertDialog;
import application.dialog.DialogHelper;
import application.dialog.LayoutInflater;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			VBox vBox = LayoutInflater.inflate("activity_main", VBox.class);
			Button button = new Button("button");
			button.setOnAction(e->{
				DialogHelper.alert("title", "this is message!", new Image("images/girl.jpg"));
			});
			vBox.getChildren().add(button);
			Scene scene = new Scene(vBox,500,500);
			scene.getStylesheets().add("css/application.css");

			primaryStage.setTitle(Constants.APP_NAME);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(Constants.APP_LOGO);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
