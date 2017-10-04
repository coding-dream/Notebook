package application;

import java.net.URL;

import application.util.Loaders;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			String xmlPath = Loaders.getRealPath("layout/activity_main.fxml");
			Parent root = FXMLLoader.load(new URL(xmlPath));

			Scene scene = new Scene(root,700,700);
			String cssPath = Loaders.getRealPath("css/application.css");
			scene.getStylesheets().add(cssPath);

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
