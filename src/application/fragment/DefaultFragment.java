package application.fragment;

import java.util.Map;

import javafx.scene.Parent;

public class DefaultFragment extends Fragment {

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public String getLayout() {
		return "fragment_default";
	}

	@Override
	public void onDestroy() {
		System.out.println("=====> onDestroy");
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {

	}
}
