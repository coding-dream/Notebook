package application.view;

import application.dialog.LayoutInflater;
import javafx.scene.Parent;

public class MainView implements View {

	@Override
	public Parent getView() {
		Parent parent = LayoutInflater.inflate("activity_main", Parent.class);
		return parent;
	}

}
