package application.fragment;

import java.util.Map;

import javafx.scene.Parent;

public class ArticleFragment extends Fragment {

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public Parent onCreateView(Map<String, String> bundle) {

		return null;
	}

	@Override
	public String getLayout() {
		return "fragment_article";
	}

	@Override
	public boolean isAdded() {
		return false;
	}

	@Override
	public void show() {
		this.node.setVisible(true);
	}

	@Override
	public void hide() {
		this.node.setVisible(false);
	}

	@Override
	public void onDestroy() {

	}
}
