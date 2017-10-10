package application.fragment;

import javafx.scene.layout.StackPane;

public class FragmentTransaction {

	public FragmentTransaction add(StackPane main_center, Fragment to) {
		return this;
	}

	public FragmentTransaction show(Fragment to){

		return this;
	}

	public FragmentTransaction hide(Fragment lastFragment){

		return this;
	}

	public void replace(StackPane main_center,Fragment fragment){
		// not implement
	}

	public void commit(){

	}

}
