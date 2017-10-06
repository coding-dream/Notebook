package application.view;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HelloView extends AbstractView {

	@Override
	public Parent getView() {
		VBox vBox = new VBox(new Button("hello"));
		return vBox;
	}

}
