package application.fragment;

import java.util.List;
import java.util.Map;

import application.bean.Article;
import application.bean.Result;
import application.dao.ArticleDao;
import application.dialog.LayoutInflater;
import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class ArticleFragment extends Fragment {
	private ListView<Article> listView;
	private Pagination pager_article;
	private Button btn_search;
	private TextField et_input;
	private boolean FIRST = true;

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

		btn_search.setOnAction(e->{
			System.out.println("search " + et_input.getText());
		});

		pager_article = (Pagination) node.lookup("#pager_article");
		pager_article.setCurrentPageIndex(0);
		pager_article.setVisible(false);
//		pager_article.setPageCount(result.pageCount);
//		pager_article.setPageCount(Pagination.INDETERMINATE);// 无限页模式,注意如果Pagination在显示后设置setPageCount则会导致call调用2次，所以这里用了一个FIRST变量控。
		pager_article.setPageFactory(new Callback<Integer, Node>() {

			@Override
			public Node call(Integer pageIndex) {
				// pageIndex 下标从0开始
				Label content = null;
				if (pageIndex >= 0 ) {
					content = new Label("当前页: " + (pageIndex + 1));
				}
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

				if(FIRST){
					pager_article.setPageCount(result.pageCount);
					pager_article.setVisible(true);
					FIRST = false;
				}
			});
		});

	}
}
