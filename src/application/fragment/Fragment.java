package application.fragment;

import java.util.HashMap;
import java.util.Map;

import application.dialog.LayoutInflater;
import javafx.scene.Parent;

public abstract class Fragment {
	protected Map<String,String> bundle = new HashMap<String, String>();
	protected Parent node;
	protected boolean isAdded = false;
	private String sTag;

	public abstract void onCreate(Map<String,String> bundle);
	public abstract void initData(Parent node,Map<String,String> bundle);
	public abstract void onDestroy();
	public abstract String getLayout();

	public void setTag(String tag){
		this.sTag = tag;
	}

	public String getTag(){
		return sTag;
	}

	public boolean isAdded(){
		return isAdded;
	}

	public void show(){
		this.node.setVisible(true);
	}

	public void hide(){
		this.node.setVisible(false);
	}

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
	}

	public Parent onCreateView(Map<String,String> bundle){
		Parent parent = LayoutInflater.inflate(getLayout(), Parent.class);
		initData(parent,bundle);
		return parent;
	}

}
