package application.fragment;

import application.view.View;

public abstract class Fragment {
	abstract void onCreate();
	abstract View onCreateView();
	abstract void onDestroy();
	abstract String getLayout();
	abstract boolean isAdded();
}
