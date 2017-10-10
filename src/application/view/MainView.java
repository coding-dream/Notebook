package application.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.bean.Article;
import application.bean.Result;
import application.dao.ArticleDao;
import application.dialog.LayoutInflater;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class MainView implements View {
	private Map<String,Parent> viewMap = new HashMap<>();
	public ExecutorService executor = Executors.newCachedThreadPool();

	private final ImageView rootIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_root.png")));
	private final ImageView oneIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_article.png")));
	private final ImageView twoIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_category.png")));
	private final ImageView threeIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_setting.png")));

	interface Callback{
		void done();
	}

	@Override
	public Parent getView() {
			Parent parent = LayoutInflater.inflate("activity_main", Parent.class);
			parent.getStylesheets().add("css/main.css");

			AnchorPane main_left = (AnchorPane) parent.lookup("#main_left");
			AnchorPane main_center = (AnchorPane) parent.lookup("#main_center");

			// left
			TreeView treeView = new TreeView();
			// TreeView的内容也是非Node类型，所以不能用SceneBuilder绘图。
		    TreeItem<String> treeItemRoot = new TreeItem<String>("导航菜单",rootIcon);

			TreeItem<String> item_1 = new TreeItem<String>("文章管理",oneIcon);
			TreeItem<String> item_2 = new TreeItem<String>("类别管理",twoIcon);
			TreeItem<String> item_3 = new TreeItem<String>("系统管理",threeIcon);
		    treeItemRoot.getChildren().addAll(Arrays.asList(item_1,item_2,item_3));

		    TreeItem<String> item_3_1 = new TreeItem<String>("xx");
		    TreeItem<String> item_3_2 = new TreeItem<String>("xx");
		    TreeItem<String> item_3_3 = new TreeItem<String>("退出");
		    treeItemRoot.getChildren().get(2).getChildren().addAll(Arrays.asList(item_3_1,item_3_2,item_3_3));

		    treeItemRoot.setExpanded(true);

	        treeView.setShowRoot(true);
	        treeView.setRoot(treeItemRoot);
	        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

				@Override
				public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
						TreeItem<String> newValue) {
					Parent container = null;
					switch (newValue.getValue()) {
					case "文章管理":
						container = replace(main_center,"include_center_article");
						ListView<Article> listView = (ListView<Article>) container.lookup("#lv_article");
						listView.setPrefWidth(100);
						if(listView.getItems().size() == 0){
							loadArticleData(1,new Callback() {

								@Override
								public void done() {


								}
							});
						}


						break;
					case "类别管理":
						container = replace(main_center,"include_center_category");

						break;
					case "系统管理":
						container = replace(main_center,"include_center_setting");
						break;
					case "退出":
						Platform.exit();
						break;
					default:
						break;
					}
				}

			});
	        main_left.getChildren().add(treeView);

			// right
	        HBox lb_center = LayoutInflater.inflate("include_center", HBox.class);
	        main_center.getChildren().add(lb_center);
	        return parent;
	}

	protected void loadArticleData(int page, Callback callback) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Result<Article> result = ArticleDao.getInstance().getPage(1);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						listView.getItems().addAll(result.recordList);
						listView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {

							@Override
							public ListCell<Article> call(ListView<Article> listView) {

								return new ListCell<Article>(){
									private Label lb_id;
									private Label lb_title;
									private Label lb_update;
									private Button btn_edit;
									private Button btn_delete;

									protected void updateItem(Article item, boolean empty) {
										super.updateItem(item, empty);// 必须调用，否则错乱的bug
										// ======================
										Parent convertView = null;
										if(getGraphic() == null){
											convertView = LayoutInflater.inflate("item_article", Parent.class);
											lb_id = (Label) convertView.lookup("#lb_id");
											lb_title = (Label) convertView.lookup("#lb_title");
											lb_update = (Label) convertView.lookup("#lb_update");
											btn_edit = (Button) convertView.lookup("#btn_edit");
											btn_delete = (Button) convertView.lookup("#btn_delete");

										}else{
											convertView = (Parent) getGraphic().lookup("#root");
										}

										if(empty){
											setText(null);
											setGraphic(null);
										}else{
											lb_id.textProperty().setValue(item.getId() + "");
											lb_title.textProperty().setValue(item.getTitle());
											lb_update.textProperty().setValue(item.getUpdateTime());
											lb_id.setText(item.getId()+"");
											lb_title.setText(item.getTitle());
											lb_update.setText(item.getUpdateTime());
											// setText(null);
											setGraphic(convertView);
										}
										// ======================
									};
								};
							}
						});


					}
				});
			}
		});
	}

	protected Parent replace(AnchorPane main_center,String key) {
		main_center.getChildren().clear();
		Parent parent = null;
		if(viewMap.containsKey(key)){
			parent = viewMap.get(key);
		}else{
			parent = LayoutInflater.inflate(key, Parent.class);
			viewMap.put(key, parent);
		}
		main_center.getChildren().add(parent);
		return parent;
	}

}
