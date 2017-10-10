package application.fragment;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class FragmentTransaction {

	public FragmentTransaction add(StackPane main_center, Fragment to) {
		to.onCreate(to.getArguments());
		Parent node = to.onCreateView(to.getArguments());
		to.setView(node);
		main_center.getChildren().add(node);
		return this;
	}

	public FragmentTransaction show(Fragment to){
		to.show();
		return this;
	}

	public FragmentTransaction hide(Fragment lastFragment){
		lastFragment.hide();
		return this;
	}

	public void replace(StackPane main_center,Fragment fragment){
		main_center.getChildren().clear();
		fragment.onCreate(fragment.getArguments());
		Parent node = fragment.onCreateView(fragment.getArguments());
		fragment.setView(node);
		main_center.getChildren().add(node);
	}
}
