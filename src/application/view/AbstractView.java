package application.view;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class AbstractView implements View {

	@Override
	public Parent getView() {
		VBox vBox = new VBox();
		return vBox;
	}

}
