package application.fragment;

import application.view.View;

public abstract class Fragment {
	public abstract void onCreate();
	public abstract View onCreateView();
	public abstract void onDestroy();
	public abstract String getLayout();
	public abstract boolean isAdded();
}
