package application.fragment;

import java.util.List;
import java.util.Map;

import application.bean.Article;
import application.bean.Result;
import application.dao.ArticleDao;
import application.dialog.AlertDialog;
import application.dialog.LayoutInflater;
import application.util.L;
import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.util.Callback;

public class ArticleFragment extends Fragment {
	private ListView<Article> listView;
	private Pagination pager_article;
	private Button btn_search;
	private Button btn_new;
	private TextField et_input;

	private boolean FIRST = true;

	private AlertDialog alertDialog;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public String getLayout() {
		return "fragment_article";
	}

	@Override
	public void onDestroy() {
		System.out.println("=====> onDestroy");
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		listView = (ListView<Article>) node.lookup("#lv_article");

		et_input = (TextField) node.lookup("#et_input");
		btn_search = (Button) node.lookup("#btn_search");
		btn_new = (Button) node.lookup("#btn_new");

		btn_search.setOnAction(e->{
			if(et_input.getText().equals("")){
				System.out.println("search can not null");
				return;
			}

			List<Article> list = ArticleDao.getInstance().search(et_input.getText());
			listView.getItems().clear();
			listView.getItems().addAll(list);

		});

		btn_new.setOnAction(e->{
			createDialog();
		});

		pager_article = (Pagination) node.lookup("#pager_article");
		pager_article.setCurrentPageIndex(0);
		pager_article.setVisible(false);
//		pager_article.setPageCount(result.pageCount);
//		pager_article.setPageCount(Pagination.INDETERMINATE);// 无限页模式,注意如果 setPageCount.的页数更新,则会多调用一次Node  call(0);不影响结果。
		pager_article.setPageFactory(new Callback<Integer, Node>() {

			@Override
			public Node call(Integer pageIndex) {
				// pageIndex 下标从0开始
				Label content = null;
				if (pageIndex >= 0 ) {
					content = new Label("当前页: " + (pageIndex + 1));
				}
				L.D("pageIndex " + pageIndex);
				loadData(pageIndex + 1);
				return content;
			}
		});


		listView.setPrefWidth(300);
		Label lb_nodata = new Label("暂无数据");
		listView.setPlaceholder(lb_nodata);

		loadData(1);
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
							btn_delete.setOnAction(e->{
								ArticleDao.getInstance().delete(item.getId());// delete database
								listView.getItems().remove(item);// remove listItem
							});

							btn_edit.setOnAction(e->{
								// update

							});
						}else{
							convertView = (Parent) getGraphic().lookup("#root");
						}

						if(empty){
							setText(null);
							setGraphic(null);
						}else{
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

	private void createDialog(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder();
		builder.title(title);
		builder.view("dialog_add")
		.build();
		alertDialog = builder.build();
		HTMLEditor htmlEditor = alertDialog.findView("#et_html", HTMLEditor.class);
		ChoiceBox<String> choiceBox = alertDialog.findView("#choiceBox", ChoiceBox.class);
		Button btn_confirm = alertDialog.findView("#btn_confirm", Button.class);
		Button btn_cancel = alertDialog.findView("#btn_cancel", Button.class);
		Label lb_category = alertDialog.findView("#lb_category", Label.class);
		Label lb_error = alertDialog.findView("#lb_error", Label.class);
		TextField et_title = alertDialog.findView("#et_title", TextField.class);

		btn_confirm.setOnAction(ee ->{
			String title = et_title.getText();
			String category = lb_category.getText();
			String content = htmlEditor.getHtmlText();

			if(title.equals("") || content.equals("")){
				lb_error.setText("标题或内容不能为空!");
				return;
			}else{
				lb_error.setText("");
			}

			Article article = new Article();
			article.setTitle(title);
			article.setContent(content);
			ArticleDao.getInstance().saveOrUpdate(article);
			alertDialog.close();
		});

		btn_cancel.setOnAction(eee->{
			alertDialog.close();
		});

		lb_category.textProperty().bind(choiceBox.valueProperty());

	    choiceBox.getItems().addAll("Dog", "Cat", "Horse");

	    choiceBox.getSelectionModel().selectFirst();
		alertDialog.show();
	}

	private void loadData(int page) {

		ThreadUtils.run(() ->{
			Result<Article> result;
			synchronized (ArticleFragment.class) {
				 result = ArticleDao.getInstance().getPage(page);
			}
			Platform.runLater(()->{
				listView.getItems().clear();
				List<Article> datas = result.recordList;
				listView.getItems().addAll(datas);

				pager_article.setPageCount(result.pageCount);// 注意页数的动态变化
				if(FIRST){
					pager_article.setVisible(true);
					FIRST = false;
				}
			});
		});

	}
}
