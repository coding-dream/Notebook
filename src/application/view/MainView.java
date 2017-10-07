package application.view;

import java.util.Arrays;

import application.dialog.LayoutInflater;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class MainView implements View {

	@Override
	public Parent getView() {
			Parent parent = LayoutInflater.inflate("activity_main", Parent.class);
			AnchorPane main_left = (AnchorPane) parent.lookup("#main_left");

			TreeView treeView = new TreeView();
			// TreeView的内容也是非Node类型，所以不能用SceneBuilder绘图。
		    TreeItem<String> treeItemRoot = new TreeItem<String>("Root node");
		    treeItemRoot.getChildren().addAll(Arrays.asList(
	                new TreeItem<String>("第1季"),
	                new TreeItem<String>("第2季"),
	                new TreeItem<String>("第3季")));

		    treeItemRoot.getChildren().get(2).getChildren().addAll(Arrays.asList(
	                new TreeItem<String>("第3季-1"),
	                new TreeItem<String>("第3季-1"),
	                new TreeItem<String>("第3季-1")
	                ));

		    treeItemRoot.setExpanded(true);

	        treeView.setShowRoot(true);
	        treeView.setRoot(treeItemRoot);

	        main_left.getChildren().add(treeView);
	        return parent;
	}

}
