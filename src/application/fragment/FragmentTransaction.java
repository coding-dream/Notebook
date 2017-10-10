package application.fragment;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class FragmentTransaction {
	private Map<String,Fragment> fragmentMap = new HashMap<>();

	public FragmentTransaction add(StackPane main_center, Fragment to) {
		return add(main_center, to,null);
	}

	public FragmentTransaction add(StackPane main_center, Fragment to,String tag) {
		if(tag != null && !fragmentMap.containsKey(tag)){
			fragmentMap.put(to.getTag(), to);
		}

		to.isAdded = true;
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
		fragment.isAdded = true;
		fragment.onCreate(fragment.getArguments());
		Parent node = fragment.onCreateView(fragment.getArguments());
		fragment.setView(node);
		main_center.getChildren().clear();
		main_center.getChildren().add(node);
	}

	public Fragment findFragmentByTag(String tag){
		Fragment fragment = null;
		if(tag != null){
			fragment = fragmentMap.get(tag);
		}
		return fragment;
	}

	public void remove(String tag){
		if(tag != null && fragmentMap.containsKey(tag)){
			Fragment fragment = fragmentMap.get(tag);
			fragment.onDestroy();
			fragmentMap.remove(tag);
			fragment = null;
		}
	}
}
