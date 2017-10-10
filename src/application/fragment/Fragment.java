package application.fragment;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Parent;

public abstract class Fragment {
	protected Map<String,String> bundle = new HashMap<String, String>();
	protected Parent node;

	public abstract void onCreate(Map<String,String> bundle);
	public abstract Parent onCreateView(Map<String,String> bundle);
	public abstract void onDestroy();
	public abstract String getLayout();
	public abstract boolean isAdded();
	public abstract void show();
	public abstract void hide();

	public void putArguments(Map<String,String> params){
		this.bundle = params;
	}

	public Map<String,String> getArguments(){
		return bundle;
	}
	public void setView(Parent node) {
		if(node != null){
			this.node = node;
		}
	};
}
