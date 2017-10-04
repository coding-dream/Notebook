package application;

import java.net.URL;

import application.util.IEProxys;
import application.util.Loaders;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			String xmlPath = Loaders.getRealPath("layout/activity_main.fxml");
			Parent root = FXMLLoader.load(new URL(xmlPath));
			Button btn_set = (Button) root.lookup("#btn_set");
			Button btn_cancel = (Button) root.lookup("#btn_cancel");
			btn_set.setOnAction(e->{
				String result = IEProxys.setProxy("192.168.0.1:8888".getBytes(), 1);
				System.out.println(result);
			});

			btn_cancel.setOnAction(e->{
				String result = IEProxys.setProxy("0.0.0.0:0000".getBytes(), 0);
				System.out.println(result);
			});
			Scene scene = new Scene(root,300,300);
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
