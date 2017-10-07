package application.view;

import application.dialog.LayoutInflater;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class LoginView implements View {

	@Override
	public Parent getView() {
		AnchorPane anchorPane = LayoutInflater.inflate("login", AnchorPane.class);
		return anchorPane;
	}

}
